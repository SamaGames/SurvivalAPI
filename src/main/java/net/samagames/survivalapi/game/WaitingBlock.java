package net.samagames.survivalapi.game;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * WaitingBlock class
 *
 * Copyright (c) for SamaGames
 * All right reserved
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

        int xMin = baseLocation.clone().getBlockX() - 2;
        int xMax = baseLocation.clone().getBlockX() + 2;
        int zMin = baseLocation.clone().getBlockY() - 2;
        int zMax = baseLocation.clone().getBlockY() + 2;

        /**
         * #####
         * #   #  > # | Wall
         * # X #  > X | Spawn location
         * #   #
         * #####
         */

        // Base plate
        for (int x = xMin; x < xMax; x++)
            for (int z = zMin; z < zMax; z++)
                this.blocks.add(this.setBlock(Material.STAINED_GLASS, new Location(baseLocation.getWorld(), x, baseLocation.getY(), z)));

        // Walls
        for (int x = xMin; x < xMax; x++)
            for (int y = baseLocation.getBlockY() + 1; y < baseLocation.getBlockY() + 1 + 3; y++)
                this.blocks.add(this.setBlock(Material.BARRIER, new Location(baseLocation.getWorld(), x, y, zMin)));

        for (int x = xMin; x < xMax; x++)
            for (int y = baseLocation.getBlockY() + 1; y < baseLocation.getBlockY() + 1 + 3; y++)
                this.blocks.add(this.setBlock(Material.BARRIER, new Location(baseLocation.getWorld(), x, y, zMax)));

        for (int z = zMin + 1; z < zMax - 1; z++)
            for (int y = baseLocation.getBlockY() + 1; y < baseLocation.getBlockY() + 1 + 3; y++)
                this.blocks.add(this.setBlock(Material.BARRIER, new Location(baseLocation.getWorld(), xMin, y, z)));

        for (int z = zMin + 1; z < zMax - 1; z++)
            for (int y = baseLocation.getBlockY() + 1; y < baseLocation.getBlockY() + 1 + 3; y++)
                this.blocks.add(this.setBlock(Material.BARRIER, new Location(baseLocation.getWorld(), xMax, y, z)));
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
