package net.samagames.survivalapi.nms;

import net.minecraft.server.v1_8_R3.*;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.nms.potions.PotionAttackDamageNerf;
import net.samagames.survivalapi.nms.stack.CustomPotion;
import net.samagames.survivalapi.nms.stack.CustomSoup;
import net.samagames.tools.Reflection;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;

/**
 * NMSPatcher class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class NMSPatcher
{
    private final Logger logger;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     */
    public NMSPatcher(SurvivalPlugin plugin)
    {
        this.logger = plugin.getLogger();
    }

    /**
     * Remove undesirable biomes
     *
     * @throws Exception
     */
    public void patchBiomes() throws Exception
    {
        BiomeBase[] biomes = BiomeBase.getBiomes();
        Map<String, BiomeBase> biomesMap = BiomeBase.o;
        BiomeBase defaultBiome = BiomeBase.FOREST;

        this.fixAnimals();

        Reflection.setFinalStatic(BiomeBase.class.getDeclaredField("ad"), defaultBiome);

        for (int i = 0; i < biomes.length; i++)
        {
            if (biomes[i] != null)
            {
                if (!biomesMap.containsKey(biomes[i].ah))
                    biomes[i] = defaultBiome;

                this.setReedsPerChunk(biomes[i], (int) Reflection.getValue(biomes[i].as, BiomeDecorator.class, true, "F") * 2);
            }
        }

        Reflection.setFinalStatic(BiomeBase.class.getDeclaredField("biomes"), biomes);
    }

    /**
     * Modify the Strength potion to do less damages
     *
     * @throws ReflectiveOperationException
     */
    public void patchPotions() throws ReflectiveOperationException
    {
        Reflection.setFinalStatic(PotionEffectType.class.getDeclaredField("acceptingNew"), true);

        Field byIdField = Reflection.getField(PotionEffectType.class, true, "byId");
        Field byNameField = Reflection.getField(PotionEffectType.class, true, "byName");
        ((Map) byNameField.get(null)).remove("increase_damage");
        ((PotionEffectType[]) byIdField.get(null))[5] = null;

        this.logger.info("Patching Strength Potion (130% => 43.3%, 260% => 86.6%)");
        Reflection.setFinalStatic(MobEffectList.class.getDeclaredField("INCREASE_DAMAGE"), (new PotionAttackDamageNerf(5, new MinecraftKey("strength"), false, 9643043)).c("potion.damageBoost").a(GenericAttributes.ATTACK_DAMAGE, "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9", 2.5D, 2));
        this.logger.info("Potions patched");
    }

    /**
     * Replace certain ItemStack to our customs
     */
    public void patchStackable()
    {
        this.logger.info("Patching Potion and Soup to change their stack size...");

        try
        {
            Method register = Item.class.getDeclaredMethod("a", int.class, String.class, Item.class);
            register.setAccessible(true);

            Item potion = new CustomPotion();
            Item soup = new CustomSoup();

            register.invoke(null, 373, "potion", potion);
            register.invoke(null, 282, "mushroom_stew", soup);

            Reflection.setFinalStatic(Items.class.getDeclaredField("POTION"), potion);
            Reflection.setFinalStatic(Items.class.getDeclaredField("MUSHROOM_STEW"), soup);
        }
        catch (ReflectiveOperationException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Add more animals in these biomes
     *
     * @throws ReflectiveOperationException
     */
    private void fixAnimals() throws ReflectiveOperationException
    {
        this.addAnimalsSpawn("PLAINS", BiomeBase.PLAINS);
        this.addAnimalsSpawn("DESERT", BiomeBase.DESERT);
        this.addAnimalsSpawn("EXTREME_HILLS", BiomeBase.EXTREME_HILLS);
        this.addAnimalsSpawn("FOREST", BiomeBase.FOREST);
        this.addAnimalsSpawn("TAIGA", BiomeBase.TAIGA);
        this.addAnimalsSpawn("SWAMPLAND", BiomeBase.SWAMPLAND);
        this.addAnimalsSpawn("RIVER", BiomeBase.RIVER);
        this.addAnimalsSpawn("FROZEN_OCEAN", BiomeBase.FROZEN_OCEAN);
        this.addAnimalsSpawn("FROZEN_RIVER", BiomeBase.FROZEN_RIVER);
        this.addAnimalsSpawn("MUSHROOM_ISLAND", BiomeBase.MUSHROOM_ISLAND);
        this.addAnimalsSpawn("MUSHROOM_SHORE", BiomeBase.MUSHROOM_SHORE);
        this.addAnimalsSpawn("BEACH", BiomeBase.BEACH);
        this.addAnimalsSpawn("DESERT_HILLS", BiomeBase.DESERT_HILLS);
        this.addAnimalsSpawn("FOREST_HILLS", BiomeBase.FOREST_HILLS);
        this.addAnimalsSpawn("TAIGA_HILLS", BiomeBase.TAIGA_HILLS);
        this.addAnimalsSpawn("SMALL_MOUNTAINS", BiomeBase.SMALL_MOUNTAINS);
        this.addAnimalsSpawn("JUNGLE", BiomeBase.JUNGLE);
        this.addAnimalsSpawn("JUNGLE_HILLS", BiomeBase.JUNGLE_HILLS);
        this.addAnimalsSpawn("JUNGLE_EDGE", BiomeBase.JUNGLE_EDGE);
        this.addAnimalsSpawn("STONE_BEACH", BiomeBase.STONE_BEACH);
        this.addAnimalsSpawn("COLD_BEACH", BiomeBase.COLD_BEACH);
        this.addAnimalsSpawn("BIRCH_FOREST", BiomeBase.BIRCH_FOREST);
        this.addAnimalsSpawn("BIRCH_FOREST_HILLS", BiomeBase.BIRCH_FOREST_HILLS);
        this.addAnimalsSpawn("ROOFED_FOREST", BiomeBase.ROOFED_FOREST);
        this.addAnimalsSpawn("COLD_TAIGA", BiomeBase.COLD_TAIGA);
        this.addAnimalsSpawn("COLD_TAIGA_HILLS", BiomeBase.COLD_TAIGA_HILLS);
        this.addAnimalsSpawn("MEGA_TAIGA", BiomeBase.MEGA_TAIGA);
        this.addAnimalsSpawn("MEGA_TAIGA_HILLS", BiomeBase.MEGA_TAIGA_HILLS);
        this.addAnimalsSpawn("EXTREME_HILLS_PLUS", BiomeBase.EXTREME_HILLS_PLUS);
        this.addAnimalsSpawn("SAVANNA", BiomeBase.SAVANNA);
        this.addAnimalsSpawn("SAVANNA_PLATEAU", BiomeBase.SAVANNA_PLATEAU);
        this.addAnimalsSpawn("MESA", BiomeBase.MESA);
        this.addAnimalsSpawn("MESA_PLATEAU_F", BiomeBase.MESA_PLATEAU_F);
        this.addAnimalsSpawn("MESA_PLATEAU", BiomeBase.MESA_PLATEAU);
        this.addAnimalsSpawn("FOREST", BiomeBase.FOREST);
    }

    /**
     * Modify the spawn rate of the animals in a given biome
     *
     * @param name Biome's name
     * @param biomeBase Biome's type
     *
     * @throws ReflectiveOperationException
     */
    private void addAnimalsSpawn(String name, BiomeBase biomeBase) throws ReflectiveOperationException
    {
        Field biome = BiomeBase.class.getDeclaredField(name);
        Field defaultMobField = BiomeBase.class.getDeclaredField("au");
        defaultMobField.setAccessible(true);

        ArrayList<BiomeBase.BiomeMeta> mobs = new ArrayList<>();

        mobs.add(new BiomeBase.BiomeMeta(EntitySheep.class, 15, 10, 10));
        mobs.add(new BiomeBase.BiomeMeta(EntityRabbit.class, 4, 3, 5));
        mobs.add(new BiomeBase.BiomeMeta(EntityPig.class, 15, 10, 20));
        mobs.add(new BiomeBase.BiomeMeta(EntityChicken.class, 20, 10, 20));
        mobs.add(new BiomeBase.BiomeMeta(EntityCow.class, 20, 10, 20));
        mobs.add(new BiomeBase.BiomeMeta(EntityWolf.class, 5, 5, 10));

        defaultMobField.set(biomeBase, mobs);
        Reflection.setFinalStatic(biome, biomeBase);
    }

    /**
     * Add more reeds in a chunk of a given biome
     *
     * @param biome Biome to modify
     * @param value Rate
     *
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private void setReedsPerChunk(BiomeBase biome, int value) throws NoSuchFieldException, IllegalAccessException
    {
        Reflection.setValue(biome.as, BiomeDecorator.class, true, "F", value);
    }
}