package net.samagames.survivalapi.game;

import net.samagames.survivalapi.SurvivalPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

/**
 * WorldLoader class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class WorldLoader
{
    private final SurvivalPlugin plugin;
   // private final int size;
    private BukkitTask task;
    private int lastShow;
    private int numberChunk;

    private int done = 0;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param size Size of the world
     */
    public WorldLoader(SurvivalPlugin plugin, int size)
    {
        this.plugin = plugin;
        //this.size = size + 100;

        this.lastShow = -1;
    }

    /**
     * Get the highest coordinate of a given location without any modification
     *
     * @param x Location X
     * @param z Location Z
     *
     * @return Y Coordinate
     */
    public static int getHighestNaturalBlockAt(int x, int z)
    {
        return Bukkit.getWorlds().get(0).getHighestBlockYAt(x,z);
    }

    /**
     * Start the world loading
     *
     * @param world World instance
     */
    public void begin(final World world, List<Location> spawns)
    {
        final long startTime = System.currentTimeMillis();
        final int size = 240;

        for(final Location loc : spawns)
        {
            new BukkitRunnable()
            {
                private int x = loc.getBlockX()-size;
                private int z = loc.getBlockZ()-size;

                @Override
                public void run()
                {
                    int i = 0;

                    while (i < 50)
                    {
                        world.getChunkAt(world.getBlockAt(this.x, 64, this.z)).load(true);

                        this.z += 16;

                        if (this.z >= size)
                        {
                            this.z = -size;
                            this.x += 16;
                        }

                        if (this.x >= size)
                        {
                            done++;
                            
                            plugin.getLogger().info("Spawn areas loaded: " + done + "/" + spawns.size());
                            
                            this.cancel();
                            return;
                        }

                        numberChunk++;
                        i++;
                    }
                }
            }, 1L, 1L);
        }.runTaskTimer(this.plugin, 1L, 1L);

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if(done >= spawns.size())
                {
                    this.cancel();
                    plugin.finishGeneration(world, System.currentTimeMillis() - startTime);
                }
            }
        }.runTaskTimer(this.plugin, 1L, 1L);

    }

    /**
     * Read the highest blocks of the world
     *
     * @param world World instance
     */
    public void computeTop(World world)
    {
        /*int x = -this.size;

        while (x < this.size)
        {
            int z = -this.size;

            while (z < this.size)
            {
                Pos.registerY(x, world.getHighestBlockYAt(x, z), z);
                z++;
            }

            x++;
        }*/
    }

    /**
     * Pos internal class
     */
    private static class Pos
    {
        private static final ArrayList<Pos> highestBlocks = new ArrayList<>();
        private final int x, y, z;

        /**
         * Constructor
         *
         * @param x Location X
         * @param y Location Y
         * @param z Location Z
         */
        public Pos(int x, int y, int z)
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        /**
         * Get X position
         *
         * @return Position
         */
        public int getX()
        {
            return this.x;
        }

        /**
         * Get Y position
         *
         * @return Position
         */
        public int getY()
        {
            return this.y;
        }

        /**
         * Get Z position
         *
         * @return Position
         */
        public int getZ()
        {
            return this.z;
        }

        /**
         * Get the highest Y position of a given location
         *
         * @param x X position
         * @param z Z position
         *
         * @return Y position
         */
        public static int getY(int x, int z)
        {
            for (Pos pos : highestBlocks)
                if (pos.getX() == x && pos.getZ() == z)
                    return pos.getY();

            return 255;
        }

        /**
         * Register a location
         *
         * @param x X position
         * @param y Y position
         * @param z Z position
         */
        public static void registerY(int x, int y, int z)
        {
            highestBlocks.add(new Pos(x, y, z));
        }
    }
}
