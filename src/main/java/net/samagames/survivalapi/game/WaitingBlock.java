package net.samagames.survivalapi.game;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

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
public class WaitingBlock
{
    private final List<Location> blocks;

    /**
     * Constructor
     *
     * @param spawnLocation Location where the player(s) will spawn
     */
    public WaitingBlock(Location spawnLocation)
    {
        this.blocks = new ArrayList<>();
        this.generate(spawnLocation);
    }

    /**
     * Remove the waiting block
     */
    public void remove()
    {
        for (Location block : this.blocks)
            block.getBlock().setType(Material.AIR);
    }

    /**
     * Place the waiting block in the world
     *
     * @param spawnLocation Location where the player(s) will spawn
     */
    private void generate(Location spawnLocation)
    {
        Location baseLocation = spawnLocation.clone().subtract(0.0, 1.0, 0.0);

        int xMin = baseLocation.clone().getBlockX() - 3;
        int xMax = baseLocation.clone().getBlockX() + 3;
        int zMin = baseLocation.clone().getBlockZ() - 3;
        int zMax = baseLocation.clone().getBlockZ() + 3;

        /**
         * #######
         * #     #  > # | Wall
         * #  X  #  > X | Spawn location
         * #     #
         * #######
         */

        // Base plate
        for (int x = xMin+1; x < xMax-1; x++)
            for (int z = zMin+1; z < zMax-1; z++)
                this.blocks.add(this.setBlock(Material.STAINED_GLASS, new Location(baseLocation.getWorld(), x, baseLocation.getY(), z)));

        // Walls
        for (int x = xMin; x < xMax; x++)
            for (int y = baseLocation.getBlockY() + 1; y < baseLocation.getBlockY() + 1 + 5; y++)
                this.blocks.add(this.setBlock(Material.BARRIER, new Location(baseLocation.getWorld(), x, y, zMin)));

        for (int x = xMin; x < xMax; x++)
            for (int y = baseLocation.getBlockY() + 1; y < baseLocation.getBlockY() + 1 + 5; y++)
                this.blocks.add(this.setBlock(Material.BARRIER, new Location(baseLocation.getWorld(), x, y, zMax - 1)));

        for (int z = zMin + 1; z < zMax; z++)
            for (int y = baseLocation.getBlockY() + 1; y < baseLocation.getBlockY() + 1 + 5; y++)
                this.blocks.add(this.setBlock(Material.BARRIER, new Location(baseLocation.getWorld(), xMin, y, z)));

        for (int z = zMin + 1; z < zMax; z++)
            for (int y = baseLocation.getBlockY() + 1; y < baseLocation.getBlockY() + 1 + 5; y++)
                this.blocks.add(this.setBlock(Material.BARRIER, new Location(baseLocation.getWorld(), xMax - 1, y, z)));
    }

    /**
     * Set the given location as a given material
     *
     * @param material Block's material
     * @param block Location
     *
     * @return Same location
     */
    private Location setBlock(Material material, Location block)
    {
        block.getBlock().setType(material);
        return block;
    }
}
