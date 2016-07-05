package net.samagames.survivalapi.game.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.util.logging.Level;

public class AntiTowerListener implements Listener
{
    byte data[][];

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public AntiTowerListener(JavaPlugin plugin)
    {
        File file = new File("world/tops.dat");
        if (file.exists())
        {
            try
            {
                int length = (int)Math.sqrt(file.length());
                this.data = new byte[length][length];
                FileInputStream inputStream = new FileInputStream(file);
                byte buffer[] = new byte[1];
                int x = 0;
                int z = 0;
                while (inputStream.read(buffer) == 1)
                {
                    this.data[x][z] = buffer[0];
                    z++;
                    if (z == length)
                    {
                        z = 0;
                        x++;
                    }
                }
                inputStream.close();
                plugin.getServer().getPluginManager().registerEvents(this, plugin);
            }
            catch (Exception exception)
            {
                plugin.getLogger().log(Level.SEVERE, "Can't load AntiTower, disabling it.", exception);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event)
    {
        int x = event.getBlock().getX() + this.data.length / 2;
        int z = event.getBlock().getZ() + this.data.length / 2;
        if (x >= 0 && z >= 0 && x < this.data.length && z < this.data.length)
        {
            byte high = this.data[x][z];
            if (event.getBlock().getY() > high + 15
                    && event.getBlock().getRelative(BlockFace.EAST).getType() == Material.AIR
                    && event.getBlock().getRelative(BlockFace.WEST).getType() == Material.AIR
                    && event.getBlock().getRelative(BlockFace.SOUTH).getType() == Material.AIR
                    && event.getBlock().getRelative(BlockFace.NORTH).getType() == Material.AIR)
            {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "Les towers sont interdites.");
            }
        }
    }
}
