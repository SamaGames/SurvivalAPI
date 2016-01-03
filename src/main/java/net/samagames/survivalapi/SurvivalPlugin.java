package net.samagames.survivalapi;

import com.google.gson.JsonPrimitive;
import com.sk89q.bukkit.util.DynamicPluginCommand;
import net.samagames.api.SamaGamesAPI;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.game.WorldDownloader;
import net.samagames.survivalapi.game.WorldLoader;
import net.samagames.survivalapi.game.commands.CommandNextEvent;
import net.samagames.survivalapi.game.commands.CommandUHC;
import net.samagames.survivalapi.nms.NMSPatcher;
import net.samagames.tools.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SurvivalPlugin extends JavaPlugin
{
    private SurvivalAPI api;
    private BukkitTask startTimer;
    private WorldLoader worldLoader;

    @Override
    public void onEnable()
    {
        File worldDir = new File(this.getDataFolder().getAbsoluteFile().getParentFile().getParentFile(), "world");
        this.getLogger().info("Checking wether world exists at : " + worldDir.getAbsolutePath());

        if (!worldDir.exists())
        {
            this.getLogger().severe("World's folder not found. Aborting!");
            Bukkit.shutdown();
        }

        this.getLogger().info("World's folder found... Checking for arena file...");
        WorldDownloader worldDownloader = new WorldDownloader(this);

        if (!worldDownloader.checkAndDownloadWorld(worldDir))
        {
            this.getLogger().severe("Error during map downloading. Aborting!");
            Bukkit.shutdown();
        }

        this.api = new SurvivalAPI(this);

        try
        {
            NMSPatcher nmsPatcher = new NMSPatcher(this);
            nmsPatcher.patchBiomes();
            nmsPatcher.patchPotions();

            if (SamaGamesAPI.get().getGameManager().getGameProperties().getOption("patch-stackable", new JsonPrimitive(false)).getAsBoolean())
                nmsPatcher.patchStackable();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        this.getCommand("uhc").setExecutor(new CommandUHC());
        this.getCommand("nextevent").setExecutor(new CommandNextEvent());

        this.startTimer = this.getServer().getScheduler().runTaskTimer(this, this::postInit, 20L, 20L);
    }

    @Override
    public void onDisable()
    {
        if (SamaGamesAPI.get().getGameManager().getGame() != null)
            ((SurvivalGame) SamaGamesAPI.get().getGameManager().getGame()).dump();
    }

    public void finishGeneration(World world, long time)
    {
        this.getLogger().info("Ready in " + time + "ms");

        long lastTime = System.currentTimeMillis();

        this.getLogger().info("Computing world top for tower detection...");
        this.worldLoader.computeTop(world);
        this.getLogger().info("Compute done in " + (System.currentTimeMillis() - lastTime) + " ms");
        this.getLogger().info("Done!");

        this.getServer().setSpawnRadius(0);

        this.api.fireEvents(SurvivalAPI.EventType.AFTERGENERATION);
    }

    private void postInit()
    {
        this.startTimer.cancel();

        this.worldLoader = new WorldLoader(this, SamaGamesAPI.get().getGameManager().getGameProperties().getOption("size", new JsonPrimitive(1000)).getAsInt());
        this.worldLoader.begin(Bukkit.getWorlds().get(0));

        try
        {
            this.removeWorldEditCommand();
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
        }

        this.api.fireEvents(SurvivalAPI.EventType.POSTINIT);
    }

    private void removeWorldEditCommand() throws NoSuchFieldException, IllegalAccessException
    {
        SimpleCommandMap scm = ((CraftServer) Bukkit.getServer()).getCommandMap();
        Map<String, Command> knownCommands = (Map) Reflection.getValue(scm, true, "knownCommands");
        List<String> toRemove = knownCommands.entrySet().stream().filter(entry -> entry.getValue() instanceof DynamicPluginCommand).map(Map.Entry::getKey).collect(Collectors.toList());

        toRemove.forEach(knownCommands::remove);
    }
}
