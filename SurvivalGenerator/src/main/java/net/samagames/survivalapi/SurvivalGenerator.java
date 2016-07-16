package net.samagames.survivalapi;

import net.minecraft.server.v1_9_R2.*;
import net.samagames.survivalapi.games.AbstractGame;
import net.samagames.survivalapi.games.Game;
import net.samagames.survivalapi.gen.WorldLoader;
import net.samagames.survivalapi.gen.biomes.BiomeRegistry;
import net.samagames.survivalapi.utils.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

public class SurvivalGenerator extends JavaPlugin
{
    public static final MinecraftKey BIOME_FOREST = new MinecraftKey("forest");
    public static final MinecraftKey BIOME_PLAINS = new MinecraftKey("plains");

    private AbstractGame game;
    private BiomeRegistry biomeRegistry;
    private BukkitTask startTimer;
    private boolean worldLoaded;

    @Override
    public void onEnable()
    {
        this.saveDefaultConfig();

        String gameRaw = this.getConfig().getString("game", "UHC");

        try
        {
            this.game = Game.valueOf(gameRaw).getGameClass().getConstructor(SurvivalGenerator.class).newInstance(this);

            this.patchBiomes();
            this.game.preInit();

            this.getServer().getPluginManager().registerEvents(this.game, this);

            this.startTimer = getServer().getScheduler().runTaskTimer(this, this::postInit, 20L, 20L);
        }
        catch (ReflectiveOperationException e)
        {
            e.printStackTrace();
        }
    }

    public void postInit()
    {
        this.startTimer.cancel();
        this.worldLoaded = true;

        WorldLoader worldLoader = new WorldLoader(this, this.getConfig().getInt("size", 1000));
        worldLoader.begin(this.getServer().getWorld("world"));
    }

    public void finishGeneration(World world, long time)
    {
        this.getLogger().info("Ready in " + time + "ms");
        Bukkit.shutdown();
    }

    public void removeBiome(int id, MinecraftKey from, MinecraftKey to)
    {
        this.biomeRegistry.register(id, from, this.biomeRegistry.getObject(to));
    }

    public boolean isWorldLoaded()
    {
        return this.worldLoaded;
    }

    private void patchBiomes() throws ReflectiveOperationException
    {
        this.biomeRegistry = BiomeRegistry.getInstance();

        this.removeBiome(0, new MinecraftKey("ocean"), BIOME_FOREST);
        this.removeBiome(10, new MinecraftKey("frozen_ocean"), BIOME_FOREST);
        this.removeBiome(14, new MinecraftKey("mushroom_island"), BIOME_PLAINS);
        this.removeBiome(15, new MinecraftKey("mushroom_island_shore"), BIOME_FOREST);
        this.removeBiome(24, new MinecraftKey("deep_ocean"), BIOME_FOREST);

        Reflection.setFinalStatic(Reflection.getField(WorldGenMonument.class, "a"), new ArrayList<BiomeBase>());
        Reflection.setFinalStatic(Reflection.getField(WorldGenMonument.class, "b"), new ArrayList<BiomeBase>());
    }

    public AbstractGame getGame()
    {
        return this.game;
    }
}
