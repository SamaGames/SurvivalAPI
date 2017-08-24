package net.samagames.survivalapi.gen;

import net.minecraft.server.v1_8_R3.*;
import net.samagames.survivalapi.utils.Reflection;

import java.lang.reflect.Field;
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
public class BetterCenter
{
    public static void load() throws ReflectiveOperationException
    {
        Field worldGenTreesField = BiomeBase.class.getDeclaredField("aA");
        worldGenTreesField.setAccessible(true);

        Field worldGenBigTreeField = BiomeBase.class.getDeclaredField("aB");
        worldGenBigTreeField.setAccessible(true);

        Field worldGenSwampTreeField = BiomeBase.class.getDeclaredField("aC");
        worldGenSwampTreeField.setAccessible(true);

        for (BiomeBase biome : BiomeBase.getBiomes())
        {
            if (biome == null)
                continue;

            worldGenTreesField.set(biome, new WorldGenTreesPatched(false));
            worldGenBigTreeField.set(biome, new WorldGenBigTreePatched(false));
            worldGenSwampTreeField.set(biome, new WorldGenSwampTreePatched());
        }

        Field worldGenForestFirstField = BiomeForest.class.getDeclaredField("aD");
        worldGenForestFirstField.setAccessible(true);

        Field worldGenForestSecondField = BiomeForest.class.getDeclaredField("aE");
        worldGenForestSecondField.setAccessible(true);

        Field worldGenForestTreeField = BiomeForest.class.getDeclaredField("aF");
        worldGenForestTreeField.setAccessible(true);

        Reflection.setFinalStatic(worldGenForestFirstField, new WorldGenForestPatched(false, true));
        Reflection.setFinalStatic(worldGenForestSecondField, new WorldGenForestPatched(false, false));
        Reflection.setFinalStatic(worldGenForestTreeField, new WorldGenForestTreePatched(false));
    }

    private static class WorldGenTreesPatched extends WorldGenTrees
    {
        WorldGenTreesPatched(boolean b)
        {
            super(b);
        }

        @Override
        public boolean generate(World world, Random random, BlockPosition blockPosition)
        {
            return !(blockPosition.getX() > -64 && blockPosition.getX() < 64 && blockPosition.getZ() > -64 && blockPosition.getZ() < 64) && super.generate(world, random, blockPosition);
        }
    }

    private static class WorldGenBigTreePatched extends WorldGenBigTree
    {
        WorldGenBigTreePatched(boolean b)
        {
            super(b);
        }

        @Override
        public boolean generate(World world, Random random, BlockPosition blockPosition)
        {
            return !(blockPosition.getX() > -64 && blockPosition.getX() < 64 && blockPosition.getZ() > -64 && blockPosition.getZ() < 64) && super.generate(world, random, blockPosition);
        }
    }

    private static class WorldGenSwampTreePatched extends WorldGenSwampTree
    {
        @Override
        public boolean generate(World world, Random random, BlockPosition blockPosition)
        {
            return !(blockPosition.getX() > -64 && blockPosition.getX() < 64 && blockPosition.getZ() > -64 && blockPosition.getZ() < 64) && super.generate(world, random, blockPosition);
        }
    }

    private static class WorldGenForestPatched extends WorldGenForest
    {
        WorldGenForestPatched(boolean b, boolean b1)
        {
            super(b, b1);
        }

        @Override
        public boolean generate(World world, Random random, BlockPosition blockPosition)
        {
            return !(blockPosition.getX() > -64 && blockPosition.getX() < 64 && blockPosition.getZ() > -64 && blockPosition.getZ() < 64) && super.generate(world, random, blockPosition);
        }
    }

    private static class WorldGenForestTreePatched extends WorldGenForestTree
    {
        WorldGenForestTreePatched(boolean flag)
        {
            super(flag);
        }

        @Override
        public boolean generate(World world, Random random, BlockPosition blockPosition)
        {
            return !(blockPosition.getX() > -64 && blockPosition.getX() < 64 && blockPosition.getZ() > -64 && blockPosition.getZ() < 64) && super.generate(world, random, blockPosition);
        }
    }
}
