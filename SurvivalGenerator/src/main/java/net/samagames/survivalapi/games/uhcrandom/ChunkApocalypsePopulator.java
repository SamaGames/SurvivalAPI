package net.samagames.survivalapi.games.uhcrandom;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

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