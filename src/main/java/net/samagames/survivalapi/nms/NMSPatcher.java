package net.samagames.survivalapi.nms;

import net.minecraft.server.v1_10_R1.BiomeBase;
import net.minecraft.server.v1_10_R1.BiomeDecorator;
import net.minecraft.server.v1_10_R1.EntityChicken;
import net.minecraft.server.v1_10_R1.EntityCow;
import net.minecraft.server.v1_10_R1.EntityPig;
import net.minecraft.server.v1_10_R1.EntityRabbit;
import net.minecraft.server.v1_10_R1.EntitySheep;
import net.minecraft.server.v1_10_R1.EntityWolf;
import net.minecraft.server.v1_10_R1.Item;
import net.minecraft.server.v1_10_R1.Items;
import net.minecraft.server.v1_10_R1.MinecraftKey;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.nms.stack.CustomAxe;
import net.samagames.survivalapi.nms.stack.CustomPotion;
import net.samagames.survivalapi.nms.stack.CustomSoup;
import net.samagames.tools.Reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.logging.Level;
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
     * Modify the Strength potion to do less damages
     *
     * @throws ReflectiveOperationException
     */
    public void patchPotions() throws ReflectiveOperationException
    {
        /*Reflection.setFinalStatic(PotionEffectType.class.getDeclaredField("acceptingNew"), true);

        Field byIdField = Reflection.getField(PotionEffectType.class, true, "byId");
        Field byNameField = Reflection.getField(PotionEffectType.class, true, "byName");
        ((Map) byNameField.get(null)).remove("increase_damage");
        ((PotionEffectType[]) byIdField.get(null))[5] = null;

        this.logger.info("Patching Strength Potion (130% => 43.3%, 260% => 86.6%)");
        //MobEffectList.REGISTRY.a(5, new MinecraftKey("strength"), new PotionAttackDamageNerf());
        //No need to patch in 1.9 :D
        this.logger.info("Potions patched");*/
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
            register.invoke(null, 271, "wooden_axe", new CustomAxe(Item.EnumToolMaterial.WOOD));
            register.invoke(null, 275, "stone_axe", new CustomAxe(Item.EnumToolMaterial.STONE));
            register.invoke(null, 258, "iron_axe", new CustomAxe(Item.EnumToolMaterial.IRON));
            register.invoke(null, 286, "golden_axe", new CustomAxe(Item.EnumToolMaterial.GOLD));
            register.invoke(null, 279, "diamond_axe", new CustomAxe(Item.EnumToolMaterial.DIAMOND));

            Reflection.setFinalStatic(Items.class.getDeclaredField("POTION"), potion);
            Reflection.setFinalStatic(Items.class.getDeclaredField("MUSHROOM_STEW"), soup);
        }
        catch (ReflectiveOperationException e)
        {
            logger.log(Level.SEVERE, "Reflection error", e);
        }
    }

    /**
     * Modify the spawn rate of the animals
     *
     * @throws ReflectiveOperationException
     */
    public void patchAnimals() throws ReflectiveOperationException
    {
        Field defaultMobField = BiomeBase.class.getDeclaredField("v");
        defaultMobField.setAccessible(true);

        ArrayList<BiomeBase.BiomeMeta> mobs = new ArrayList<>();

        mobs.add(new BiomeBase.BiomeMeta(EntitySheep.class, 15, 10, 10));
        mobs.add(new BiomeBase.BiomeMeta(EntityRabbit.class, 4, 3, 5));
        mobs.add(new BiomeBase.BiomeMeta(EntityPig.class, 15, 10, 20));
        mobs.add(new BiomeBase.BiomeMeta(EntityChicken.class, 20, 10, 20));
        mobs.add(new BiomeBase.BiomeMeta(EntityCow.class, 20, 10, 20));
        mobs.add(new BiomeBase.BiomeMeta(EntityWolf.class, 5, 5, 10));

        for (MinecraftKey biomeKey : BiomeBase.REGISTRY_ID.keySet())
            defaultMobField.set(BiomeBase.REGISTRY_ID.get(biomeKey), mobs);
    }

    /**
     * Add more reeds in a chunk of a given biome
     *
     * @throws ReflectiveOperationException
     */
    public void patchReeds() throws ReflectiveOperationException
    {
        for (MinecraftKey biomeKey : BiomeBase.REGISTRY_ID.keySet())
        {
            BiomeBase biome = BiomeBase.REGISTRY_ID.get(biomeKey);
            Reflection.setValue(biome.t, BiomeDecorator.class, true, "E", (int) Reflection.getValue(biome.t, BiomeDecorator.class, true, "E") * 2);
        }
    }
}
