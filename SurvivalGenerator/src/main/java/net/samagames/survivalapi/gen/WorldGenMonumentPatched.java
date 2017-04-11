package net.samagames.survivalapi.gen;

import net.minecraft.server.v1_8_R3.*;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.generator.NormalChunkGenerator;

import java.lang.reflect.Field;

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