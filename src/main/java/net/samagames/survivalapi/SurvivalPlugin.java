package net.samagames.survivalapi;

import com.sk89q.bukkit.util.DynamicPluginCommand;
import io.netty.channel.Channel;
import net.samagames.api.shadows.EnumPacket;
import net.samagames.api.shadows.IPacketListener;
import net.samagames.api.shadows.Packet;
import net.samagames.api.shadows.ShadowsAPI;
import net.samagames.api.shadows.play.server.PacketLogin;
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
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
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
        this.api = new SurvivalAPI(this);

        try
        {
            NMSPatcher nmsPatcher = new NMSPatcher(this);
            nmsPatcher.patchBiomes();
            nmsPatcher.patchPotions();
        }
        catch (ReflectiveOperationException e)
        {
            e.printStackTrace();
        }

        this.getCommand("uhc").setExecutor(new CommandUHC());
        this.getCommand("nextevent").setExecutor(new CommandNextEvent());

        this.startTimer = this.getServer().getScheduler().runTaskTimer(this, this::postInit, 20L, 20L);
    }

    public void finishGeneration(World world, long time)
    {
        this.getLogger().info("Ready in " + time + "ms");

        long lastTime = System.currentTimeMillis();

        this.getLogger().info("Computing world top for tower detection...");
        this.worldLoader.computeTop(world);
        this.getLogger().info("Compute done in " + (System.currentTimeMillis() - lastTime) + " ms");
        this.getLogger().info("Done!");

        this.api.fireEvents(SurvivalAPI.EventType.AFTERGENERATION);
    }

    private void postInit()
    {
        this.startTimer.cancel();

        ShadowsAPI.get().registerListener(new IPacketListener()
        {
            @Override
            public List<Class<? extends Packet>> getWhiteListedPackets()
            {
                ArrayList<Class<? extends Packet>> packets = new ArrayList<>();

                packets.add(PacketLogin.class);

                return packets;
            }

            @Override
            public void onPacket(Player player, Channel channel, Packet packet, EnumPacket.EnumPacketDirection networkDirection)
            {
                if (packet instanceof PacketLogin)
                {
                    ((PacketLogin) packet).setHardcoreMode(true);
                    packet.markDirty();
                }
            }
        });

        this.worldLoader = new WorldLoader(this);
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
