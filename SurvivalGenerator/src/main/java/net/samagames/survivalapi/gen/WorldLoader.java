package net.samagames.survivalapi.gen;

import net.samagames.survivalapi.SurvivalGenerator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class WorldLoader
{
    private final SurvivalGenerator plugin;
    private final int size;
    private BukkitTask task;
    private int lastShow;
    private int numberChunk;

    public WorldLoader(SurvivalGenerator plugin, int size)
    {
        this.plugin = plugin;
        this.size = (size + 100);

        this.lastShow = -1;
    }

    public static int getHighestNaturalBlockAt(int x, int z)
    {
        return Pos.getY(x, z);
    }

    public void begin(final World world)
    {
        long startTime = System.currentTimeMillis();

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
                        computeTop(world, startTime);
                        return;
                    }

                    numberChunk++;
                    i++;
                }
            }
        }, 1L, 1L);
    }

    public void computeTop(World world, long startTime)
    {
        File file = new File("world/tops.dat");
        FileOutputStream writer;
        try
        {
            if (!file.createNewFile())
            {
                this.plugin.getLogger().severe("Can't create tops file.");
                Bukkit.shutdown();
                return;
            }
            writer = new FileOutputStream(file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            this.plugin.getLogger().severe("Can't create tops file.");
            Bukkit.shutdown();
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
                        plugin.finishGeneration(world, System.currentTimeMillis() - startTime);
                        return ;
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


    private static final class Pos
    {
        private static ArrayList<Pos> highestBlocks = new ArrayList<>();
        private int x, y, z;

        Pos(int x, int y, int z)
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }


        public int getX()
        {
            return this.x;
        }

        public int getY()
        {
            return this.y;
        }

        public int getZ()
        {
            return this.z;
        }

        public static int getY(int x, int z)
        {
            for (Pos pos : highestBlocks)
                if (pos.getX() == x && pos.getZ() == z)
                    return pos.getY();

            return 255;
        }

        public static void registerY(int x, int y, int z)
        {
            highestBlocks.add(new Pos(x, y, z));
        }
    }
}
