package net.samagames.survivalapi.gen;

import net.minecraft.server.v1_9_R2.*;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R2.generator.NormalChunkGenerator;

import java.lang.reflect.Field;

/**
 * @author Florian Cassayre (6infinity8)
 */
public class WorldGenCaves extends net.minecraft.server.v1_9_R2.WorldGenCaves
{
    private final int amount;

    /**
     * <p>Used to control the world generation.
     * This module can amplify or reduce the amount of caves in the world.</p>
     * <p>Setting the parameter to 0 will totally remove caves from the world. Setting it to 2 will double the amount of caves.</p>
     * <p>Note: this may affect your server performance by <code>amountOfCaves * -5%</code>.</p>
     * @param amountOfCaves number of times the cave generator method should be called.
     */
    public WorldGenCaves(int amountOfCaves)
    {
        this.amount = amountOfCaves;
    }

    @Override
    public void a(World world, int chunkX, int chunkZ, ChunkSnapshot chunksnapshot)
    {
        for(int i = 0; i < this.amount; i++)
        {
            int k = this.e;

            this.g = world;
            this.f.setSeed(world.getSeed() + i);
            long l = this.f.nextLong();
            long i1 = this.f.nextLong();

            for (int j1 = chunkX - k; j1 <= chunkX + k; ++j1)
            {
                for (int k1 = chunkZ - k; k1 <= chunkZ + k; ++k1)
                {
                    long l1 = (long) j1 * l;
                    long i2 = (long) k1 * i1;

                    this.f.setSeed(l1 ^ i2 ^ world.getSeed());
                    this.a(world, j1, k1, chunkX, chunkZ, chunksnapshot);
                }
            }
        }
    }

    /**
     * Loads the module for a world with a parameter.
     * @param world the world to alter generation.
     * @param amountOfCaves amount of caves.
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static void loadForWorld(org.bukkit.World world, int amountOfCaves) throws NoSuchFieldException, IllegalAccessException
    {
        World craftworld = ((CraftWorld) world).getHandle();

        Object chunkProviderServer = getFieldValue(World.class, "chunkProvider", craftworld);
        Object normalChunkProvider = getFieldValue(ChunkProviderServer.class, "chunkProvider", chunkProviderServer);
        Object chunkProviderGenerate = getFieldValue(NormalChunkGenerator.class, "provider", normalChunkProvider);

        changeFieldValue(ChunkProviderGenerate.class, "v", chunkProviderGenerate, new WorldGenCaves(amountOfCaves));
    }

    private static void changeFieldValue(Class clazz, String fieldName, Object object, Object value) throws NoSuchFieldException, IllegalAccessException
    {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }

    private static Object getFieldValue(Class clazz, String fieldName, Object object) throws NoSuchFieldException, IllegalAccessException
    {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }
}