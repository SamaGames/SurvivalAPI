package net.samagames.survivalapi.game.types.run;

import net.samagames.survivalapi.game.WorldLoader;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class WorldDestructionRunnable extends BukkitRunnable
{
    private final JavaPlugin plugin;
    private final World world;
    private final int size;
    private int y;

    public WorldDestructionRunnable(JavaPlugin plugin, int size)
    {
        this.plugin = plugin;
        this.world = this.plugin.getServer().getWorlds().get(0);
        this.size = size / 2;
        this.y = 0;
    }

    @Override
    public void run()
    {
        if (this.y >= 200)
            this.cancel();

        for (int x = -this.size; x < this.size + 1; x++)
        {
            for (int z = -this.size; z < this.size + 1; z++)
            {
                if (WorldLoader.getHighestNaturalBlockAt(x, z) >= this.y)
                    continue;

                this.world.getBlockAt(x, this.y, z).setType(Material.OBSIDIAN);
            }
        }
    }
}
