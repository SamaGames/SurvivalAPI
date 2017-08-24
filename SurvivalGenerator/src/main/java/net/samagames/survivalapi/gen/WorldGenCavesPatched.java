package net.samagames.survivalapi.gen;

import net.minecraft.server.v1_8_R3.*;
import net.minecraft.server.v1_8_R3.ChunkSnapshot;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.generator.NormalChunkGenerator;

import java.lang.reflect.Field;

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
public class WorldGenCavesPatched extends WorldGenCaves
{
    private final int amount;

    public WorldGenCavesPatched(int amountOfCaves)
    {
        this.amount = amountOfCaves;
    }

    @Override
    public void a(IChunkProvider chunkProvider, World world, int chunkX, int chunkZ, ChunkSnapshot chunksnapshot)
    {
        for(int i = 0; i < this.amount; i++)
        {
            int k = this.a;

            this.c = world;
            this.b.setSeed(world.getSeed() + i);
            long l = this.b.nextLong();
            long i1 = this.b.nextLong();

            for (int j1 = chunkX - k; j1 <= chunkX + k; ++j1)
            {
                for (int k1 = chunkZ - k; k1 <= chunkZ + k; ++k1)
                {
                    long l1 = (long) j1 * l;
                    long i2 = (long) k1 * i1;

                    this.b.setSeed(l1 ^ i2 ^ world.getSeed());
                    this.a(world, j1, k1, chunkX, chunkZ, chunksnapshot);
                }
            }
        }
    }

    public static void load(org.bukkit.World world, int amountOfCaves) throws NoSuchFieldException, IllegalAccessException
    {
        World craftWorld = ((CraftWorld) world).getHandle();

        Field chunkProviderWorldField = World.class.getDeclaredField("chunkProvider");
        chunkProviderWorldField.setAccessible(true);

        Field chunkProviderChunkProviderField = ChunkProviderServer.class.getDeclaredField("chunkProvider");
        chunkProviderChunkProviderField.setAccessible(true);

        Field chunkProviderField = NormalChunkGenerator.class.getDeclaredField("provider");
        chunkProviderField.setAccessible(true);

        Field worldGenCaveField = ChunkProviderGenerate.class.getDeclaredField("u");
        worldGenCaveField.setAccessible(true);

        worldGenCaveField.set(chunkProviderField.get(chunkProviderChunkProviderField.get(chunkProviderWorldField.get(craftWorld))), new WorldGenCavesPatched(amountOfCaves));
    }
}