package net.samagames.survivalapi.gen;

import net.samagames.survivalapi.SurvivalGenerator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class WorldLoader
{
    private static final List<Biome> WHITELISTED_CENTER_BIOMES = Arrays.asList(
            Biome.PLAINS, Biome.SUNFLOWER_PLAINS, Biome.DESERT, Biome.DESERT_HILLS,
            Biome.MESA_PLATEAU, Biome.MESA_PLATEAU_FOREST, Biome.SAVANNA, Biome.SAVANNA_PLATEAU,
            Biome.FOREST, Biome.FOREST_HILLS, Biome.BIRCH_FOREST, Biome.BIRCH_FOREST_HILLS,
            Biome.FLOWER_FOREST, Biome.ROOFED_FOREST
    );

    private final SurvivalGenerator plugin;
    private final int size;
    private final boolean strict;
    private BukkitTask task;
    private int lastShow;
    private int numberChunk;
    private long startTime;

    public WorldLoader(SurvivalGenerator plugin, int size, boolean strict)
    {
        this.plugin = plugin;
        this.size = (size + 100);
        this.strict = strict;

        this.lastShow = -1;
    }

    public void begin(final World world)
    {
        this.startTime = System.currentTimeMillis();

        if (this.strict)
        {
            int accepted = 0;
            int refused = 0;
            int liquids = 0;

            for (int x = -64; x <= 64; x++)
            {
                for (int z = -64; z <= 64; z++)
                {
                    Block blockToTest = world.getBlockAt(x, 80, z);
                    blockToTest.getChunk().load(true);

                    Biome biomeToTest = blockToTest.getBiome();

                    if (WHITELISTED_CENTER_BIOMES.contains(biomeToTest))
                        accepted++;
                    else
                        refused++;

                    if (world.getHighestBlockAt(x, z).isLiquid())
                        liquids++;
                }
            }

            this.plugin.getLogger().info("Blocks accepted: " + accepted);
            this.plugin.getLogger().info("Blocks refused: " + refused);
            this.plugin.getLogger().info("Blocks liquids: " + liquids);

            int total = accepted + refused;

            if ((refused * 100 / total > 10) || (liquids * 100 / total > 10))
            {
                this.plugin.getLogger().info("Too much blocks refused. Cancelling generation.");

                try
                {
                    File invalidMarker = new File(world.getWorldFolder(), "invalid.tmp");
                    invalidMarker.createNewFile();

                    Bukkit.shutdown();
                    return;
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        this.task = Bukkit.getScheduler().runTaskTimer(this.plugin, new Runnable()
        {
            private int todo = ((size * 2) * (size * 2)) / 256;
            private int x = -size;
            private int z = -size;

            @Override
            public void run()
            {
                int i = 0;

                while (i < 50)
                {
                    world.getChunkAt(world.getBlockAt(this.x, 64, this.z)).load(true);

                    int percentage = numberChunk * 100 / todo;
                    if (percentage > lastShow && percentage % 10 == 0)
                    {
                        lastShow = percentage;
                        plugin.getLogger().info("Loading chunks (" + percentage + "%)");
                    }

                    this.z += 16;

                    if (this.z >= size)
                    {
                        this.z = -size;
                        this.x += 16;
                    }

                    if (this.x >= size)
                    {
                        task.cancel();
                        plugin.getGame().onFinish(world);
                        return;
                    }

                    numberChunk++;
                    i++;
                }
            }
        }, 1L, 1L);
    }

    public void computeTop(World world, Runnable callback)
    {
        this.plugin.getLogger().info("Computing map top height");
        File file = new File("world/tops.dat");
        FileOutputStream writer;
        try
        {
            if (!file.createNewFile())
            {
                this.plugin.getLogger().severe("Can't create tops file.");
                callback.run();
                return;
            }
            writer = new FileOutputStream(file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            this.plugin.getLogger().severe("Can't create tops file.");
            callback.run();
            return ;
        }
        this.task = this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, new Runnable()
        {
            private int x = -size;

            @Override
            public void run()
            {
                try
                {
                    if (x >= size)
                    {
                        writer.close();
                        task.cancel();
                        callback.run();

                        return;
                    }

                    for (int z = -size; z < size; z++)
                        writer.write(new byte[]{(byte)world.getHighestBlockAt(x, z).getY()});

                    x++;
                }
                catch (Exception exception)
                {
                    exception.printStackTrace();
                }
            }
        }, 1L, 1L);
    }

    public long getStartTime()
    {
        return startTime;
    }
}
