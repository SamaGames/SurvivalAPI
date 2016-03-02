package net.samagames.survivalapi.gen.biomes;

import net.minecraft.server.v1_9_R1.BiomeBase;
import net.minecraft.server.v1_9_R1.MinecraftKey;
import net.minecraft.server.v1_9_R1.RegistryID;
import net.minecraft.server.v1_9_R1.RegistryMaterials;
import net.samagames.survivalapi.utils.Reflection;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;

public class BiomeRegistry extends RegistryMaterials<MinecraftKey, BiomeBase> implements IRegistry<MinecraftKey, BiomeBase>
{
    protected final Map<MinecraftKey, BiomeBase> registry = new HashMap<>();
    protected final IDRegistry<BiomeBase> idRegistry = new IDRegistry(256);

    public static BiomeRegistry getInstance()
    {
        if (!(BiomeBase.REGISTRY_ID instanceof BiomeRegistry))
            init();

        return (BiomeRegistry) BiomeBase.REGISTRY_ID;
    }

    @Override
    public void a(int id, MinecraftKey entry, BiomeBase value)
    {
        this.register(id, entry, value);
    }

    @Override
    public void a(MinecraftKey entry, BiomeBase value)
    {
        Validate.notNull(entry);
        Validate.notNull(value);

        if(this.registry.containsKey(entry))
            Bukkit.getLogger().log(Level.FINE, "Adding duplicate key \'" + entry + "\' to registry");

        this.registry.put(entry, value);
    }

    @Override
    public void register(int id, MinecraftKey entry, BiomeBase value)
    {
        this.idRegistry.put(value, id);
        this.a(entry, value);
    }

    @Override
    public BiomeBase get(MinecraftKey var1)
    {
        return this.getObject(var1);
    }

    @Override
    public MinecraftKey b(BiomeBase var1)
    {
        return this.getNameForObject(var1);
    }

    @Override
    public boolean d(MinecraftKey var1)
    {
        return this.containsKey(var1);
    }

    @Override
    public int a(BiomeBase var1)
    {
        return this.getIDForObject(var1);
    }

    @Override
    public BiomeBase getId(int var1)
    {
        return this.getObjectById(var1);
    }

    @Override
    public BiomeBase getObject(MinecraftKey name)
    {
        return registry.get(name);
    }

    @Override
    public MinecraftKey getNameForObject(BiomeBase obj)
    {
        MinecraftKey result = null;
        for (Map.Entry<MinecraftKey, BiomeBase> entryset : registry.entrySet())
        {
            if (entryset.getValue() != null && entryset.getValue().equals(obj))
            {
                if (result != null)
                {
                    Bukkit.getLogger().warning("DUPLICATE ENTRY FOR BIOME " + obj.getClass().getName());
                    break;
                }
                result = entryset.getKey();
            }

        }
        return result;
    }

    @Override
    public boolean containsKey(MinecraftKey key)
    {
        return registry.containsKey(key);
    }

    @Override
    public int getIDForObject(BiomeBase obj)
    {
        return this.idRegistry.get(obj);
    }

    @Override
    public BiomeBase getObjectById(int id)
    {
        return this.idRegistry.getByValue(id);
    }

    @Override
    public Iterator<BiomeBase> iterator()
    {
        return this.idRegistry.iterator();
    }

    public static boolean init()
    {
        try
        {
            BiomeRegistry registry = new BiomeRegistry();
            RegistryMaterials<MinecraftKey, BiomeBase> oldRegistry = BiomeBase.REGISTRY_ID;
            RegistryID<BiomeBase> oldIDRegistry = (RegistryID<BiomeBase>) Reflection.getValue(oldRegistry, "a");;
            Map<BiomeBase, MinecraftKey> oldDataRegistry = (Map<BiomeBase, MinecraftKey>) Reflection.getValue(oldRegistry, "b");

            for (Map.Entry<BiomeBase, MinecraftKey> entry : oldDataRegistry.entrySet())
            {
                int id = oldIDRegistry.getId(entry.getKey());
                if (id == -1 || entry.getKey() == null)
                    continue;
                registry.register(id, entry.getValue(), entry.getKey());
            }

            Reflection.setFinalStatic(BiomeBase.class.getDeclaredField("REGISTRY_ID"), registry);
        }
        catch (ReflectiveOperationException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
