package net.samagames.survivalapi.games.uhcrandom;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

public class ChunkApocalypsePopulator extends BlockPopulator
{

    @Override
    public void populate(World world, Random random, Chunk chunk)
    {
        final int SPACING = 3;
        final int FREQUENCY = 1;

        final int chunkX = chunk.getX();
        final int chunkZ = chunk.getZ();

        // This way we can prevent two chunks from being next to each other.
        //if(chunkX % SPACING != SPACING - 1 && chunkZ % SPACING != SPACING - 1)
        //{
        final int seedX = chunkX - (chunkX % SPACING);
        final int seedZ = chunkZ - (chunkZ % SPACING);

        long seed = world.getSeed();
        seed *= LinearCongruentialGenerator.generate(world.getSeed());
        seed += seedX;
        seed *= LinearCongruentialGenerator.generate(world.getSeed());
        seed += seedZ;
        seed *= LinearCongruentialGenerator.generate(world.getSeed());

        Random sectionRng = new Random(seed);

        for(int i = 0; i < FREQUENCY; i++)
        {
            final int cX = seedX + sectionRng.nextInt(SPACING);
            final int cZ = seedZ + sectionRng.nextInt(SPACING);

            if(chunk.getX() == cX && chunk.getZ() == cZ)
            {
                System.out.println(chunkX + " " + chunkZ);

                for(int x = 0; x < 16; x++)
                {
                    for(int y = 0; y < 256; y++)
                    {
                        for(int z = 0; z < 16; z++)
                        {
                            chunk.getBlock(x, y, z).setType(Material.AIR);
                        }
                    }
                }
            }
        }
        //}
    }

    private static class LinearCongruentialGenerator
    {
        private final static long A = 2862933555777941757L;
        private final static long B = 3037000493L;

        private LinearCongruentialGenerator()
        {

        }

        public static long generate(final long seed)
        {
            return A * seed + B;
        }
    }
}