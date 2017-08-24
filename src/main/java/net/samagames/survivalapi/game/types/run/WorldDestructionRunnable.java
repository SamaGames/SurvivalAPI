package net.samagames.survivalapi.game.types.run;

import net.samagames.survivalapi.game.WorldLoader;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/*
 * This file is part of SurvivalAPI.
 *
 * SurvivalAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SurvivalAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SurvivalAPI.  If not, see <http://www.gnu.org/licenses/>.
 */
public class WorldDestructionRunnable extends BukkitRunnable
{
    private final JavaPlugin plugin;
    private final World world;
    private final int size;
    private int y;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param size Zone size
     */
    public WorldDestructionRunnable(JavaPlugin plugin, int size)
    {
        this.plugin = plugin;
        this.world = this.plugin.getServer().getWorlds().get(0);
        this.size = size / 2;
        this.y = 0;
    }

    /**
     * Destruct a piece of the map
     */
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

        this.y++;
    }
}
