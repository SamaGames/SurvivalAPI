package net.samagames.survivalapi.gen;

import net.minecraft.server.v1_8_R3.*;
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
public class WorldGenMonumentPatched extends WorldGenMonument
{
    @Override
    protected boolean a(int var1, int var2)
    {
        return false;
    }

    public static void load(org.bukkit.World world) throws NoSuchFieldException, IllegalAccessException
    {
        World craftWorld = ((CraftWorld) world).getHandle();

        Field chunkProviderWorldField = World.class.getDeclaredField("chunkProvider");
        chunkProviderWorldField.setAccessible(true);

        Field chunkProviderChunkProviderField = ChunkProviderServer.class.getDeclaredField("chunkProvider");
        chunkProviderChunkProviderField.setAccessible(true);

        Field chunkProviderField = NormalChunkGenerator.class.getDeclaredField("provider");
        chunkProviderField.setAccessible(true);

        Field worldGenMonumentField = ChunkProviderGenerate.class.getDeclaredField("A");
        worldGenMonumentField.setAccessible(true);

        worldGenMonumentField.set(chunkProviderField.get(chunkProviderChunkProviderField.get(chunkProviderWorldField.get(craftWorld))), new WorldGenMonumentPatched());
    }
}