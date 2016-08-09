package net.samagames.survivalapi.games.ultraflagkeeper;

/**
 * ╱╲＿＿＿＿＿＿╱╲
 * ▏╭━━╮╭━━╮▕
 * ▏┃＿＿┃┃＿＿┃▕
 * ▏┃＿▉┃┃▉＿┃▕
 * ▏╰━━╯╰━━╯▕
 * ╲╰╰╯╲╱╰╯╯╱  Created by Silvanosky on 09/08/2016
 * ╱╰╯╰╯╰╯╰╯╲
 * ▏▕╰╯╰╯╰╯▏▕
 * ▏▕╯╰╯╰╯╰▏▕
 * ╲╱╲╯╰╯╰╱╲╱
 * ＿＿╱▕▔▔▏╲＿＿
 * ＿＿▔▔＿＿▔▔＿＿
 */
import net.minecraft.server.v1_9_R2.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;

public class WorldGeneratorUFK
{
    public WorldGeneratorUFK()
    {
        patchBiomeParams();
    }
    public static void patchBiomeParams()
    {
        try
        {
            /** Step 1 : get access to the biome registry and lists and clear them to prevent oddities */

            // Access and clear the list of biomes in BiomeBase.REGISTRY_ID
            Field f = BiomeBase.class.getDeclaredField("REGISTRY_ID");
            // REGISTRY_ID is BiomeBase.class :  public static final RegistryMaterials<MinecraftKey, BiomeBase> REGISTRY_ID = new RegistryMaterials();
            f.setAccessible(true);
            Field m = Field.class.getDeclaredField("modifiers");
            m.setAccessible(true); // bypass private/protected
            m.setInt(f, f.getModifiers() & ~Modifier.FINAL);// bypass final
            f.set( null, new RegistryMaterials<MinecraftKey, BiomeBase>() );
            // Access and clear the list of biomes in BiomeBase.j
            f = BiomeBase.class.getDeclaredField("j");
            // j is BiomeBase.class : public static final RegistryBlockID<BiomeBase> j = new RegistryBlockID();
            f.setAccessible(true);
            m.setInt(f, f.getModifiers() & ~Modifier.FINAL);
            f.set( null, new RegistryBlockID<BiomeBase>() );
            /** Step 2 : modify the biome parameters and refill REGISTRY_ID */
            //This is where the data for each biome is set, including terrain height
            //Set all the new biome data - this needs to include EVERY biome, or stuff will get all mushroomy.
            //If you really want to see your world on shrooms, leave some stuff out.
            registerBiome(0, "ocean", Plains.get(false, new BiomeData("Plains").height(0.125F).hills(0.05F).temp(0.8F).rain(0.4F)));
            registerBiome(1, "plains", Plains.get(false, new BiomeData("Plains").height(0.125F).hills(0.05F).temp(0.8F).rain(0.4F)));
            registerBiome(2, "desert", new BiomeDesert(new BiomeData("Desert").height(0.05F).hills(0.05F).temp(2.0F).rain(0.0F)));
            registerBiome(3, "extreme_hills", Plains.get(false, new BiomeData("Plains").height(0.125F).hills(0.05F).temp(0.8F).rain(0.4F)));
            registerBiome(4, "forest", new BiomeForest(BiomeForest.Type.NORMAL, new BiomeData("Forest").temp(0.7F).rain(0.8F).hills(0.05F)));
            registerBiome(5, "taiga", new BiomeForest(BiomeForest.Type.NORMAL, new BiomeData("Forest").temp(0.7F).rain(0.8F).hills(0.05F)));
            // Example: adjust swamp depth to be less deep, giving a depth of about 1 water block with this preset
            // normal swamp depth is -0.2, set it to 0.11 instead, a good deal higher in the world preset this was made for
            registerBiome(6, "swampland", new BiomeForest(BiomeForest.Type.NORMAL, new BiomeData("Forest").temp(0.7F).rain(0.8F).hills(0.05F)));
            registerBiome(7, "river", new BiomeRiver(new BiomeData("River").height(-0.65F).hills(0.0F).rain(0.0F)));
            registerBiome(8, "hell", new BiomeHell(new BiomeData("Hell").temp(2.0F).rain(0.0F)));
            registerBiome(9, "sky", new BiomeTheEnd(new BiomeData("The End")));
            registerBiome(10, "frozen_ocean", new BiomeForest(BiomeForest.Type.NORMAL, new BiomeData("Forest").temp(0.7F).rain(0.8F).hills(0.05F)));
            registerBiome(11, "frozen_river", new BiomeForest(BiomeForest.Type.NORMAL, new BiomeData("Forest").temp(0.7F).rain(0.8F).hills(0.05F)));
            registerBiome(12, "ice_flats", new BiomeForest(BiomeForest.Type.NORMAL, new BiomeData("Forest").temp(0.7F).rain(0.8F).hills(0.05F)));
            registerBiome(13, "ice_mountains", new BiomeForest(BiomeForest.Type.NORMAL, new BiomeData("Forest").temp(0.7F).rain(0.8F).hills(0.05F)));
            registerBiome(14, "mushroom_island", new BiomeForest(BiomeForest.Type.NORMAL, new BiomeData("Forest").temp(0.7F).rain(0.8F).hills(0.05F)));
            registerBiome(15, "mushroom_island_shore", new BiomeForest(BiomeForest.Type.NORMAL, new BiomeData("Forest").temp(0.7F).rain(0.8F).hills(0.05F)));
            registerBiome(16, "beaches", Plains.get(false, new BiomeData("Plains").height(0.125F).hills(0.05F).temp(0.8F).rain(0.4F)));
            registerBiome(17, "desert_hills", new BiomeDesert(new BiomeData("DesertHills").height(0.45F).hills(0.05F).temp(2.0F).rain(0.0F)));
            registerBiome(18, "forest_hills", new BiomeForest(BiomeForest.Type.NORMAL, new BiomeData("ForestHills").height(0.45F).hills(0.05F).temp(0.7F).rain(0.8F)));
            registerBiome(19, "taiga_hills", new BiomeForest(BiomeForest.Type.NORMAL, new BiomeData("Forest").temp(0.7F).rain(0.8F)));
            registerBiome(20, "smaller_extreme_hills", Plains.get(false, new BiomeData("Plains").height(0.125F).hills(0.05F).temp(0.8F).rain(0.4F)));
            registerBiome(21, "jungle", new BiomeForest(BiomeForest.Type.NORMAL, new BiomeData("Forest").temp(0.7F).rain(0.8F).hills(0.05F)));
            registerBiome(22, "jungle_hills", Plains.get(false, new BiomeData("Plains").height(0.125F).hills(0.05F).temp(0.8F).rain(0.4F)));
            registerBiome(23, "jungle_edge", Plains.get(false, new BiomeData("Plains").height(0.125F).hills(0.05F).temp(0.8F).rain(0.4F)));
            registerBiome(24, "deep_ocean", Plains.get(false, new BiomeData("Plains").height(0.125F).hills(0.05F).temp(0.8F).rain(0.4F)));
            registerBiome(25, "stone_beach", new BiomeStoneBeach(new BiomeData("Stone Beach").height(0.1F).hills(0.05F).temp(0.2F).rain(0.3F)));
            registerBiome(26, "cold_beach", new BiomeBeach(new BiomeData("Cold Beach").height(0.0F).hills(0.025F).temp(0.05F).rain(0.3F)));
            registerBiome(27, "birch_forest", new BiomeForest(BiomeForest.Type.BIRCH, new BiomeData("Birch Forest").temp(0.6F).rain(0.6F).hills(0.05F)));
            registerBiome(28, "birch_forest_hills", new BiomeForest(BiomeForest.Type.BIRCH, new BiomeData("Birch Forest Hills").height(0.45F).hills(0.05F).temp(0.6F).rain(0.6F)));
            registerBiome(29, "roofed_forest", new BiomeForest(BiomeForest.Type.ROOFED, new BiomeData("Roofed Forest").temp(0.7F).rain(0.8F)));
            registerBiome(30, "taiga_cold", new BiomeTaiga(BiomeTaiga.Type.NORMAL, new BiomeData("Cold Taiga").height(0.2F).hills(0.05F).temp(-0.5F).rain(0.4F)));
            registerBiome(31, "taiga_cold_hills", new BiomeTaiga(BiomeTaiga.Type.NORMAL, new BiomeData("Cold Taiga Hills").height(0.45F).hills(0.05F).temp(-0.5F).rain(0.4F)));
            registerBiome(32, "redwood_taiga", new BiomeTaiga(BiomeTaiga.Type.MEGA, new BiomeData("Mega Taiga").temp(0.3F).rain(0.8F).height(0.2F).hills(0.05F)));
            registerBiome(33, "redwood_taiga_hills", new BiomeTaiga(BiomeTaiga.Type.MEGA, new BiomeData("Mega Taiga Hills").height(0.45F).hills(0.05F).temp(0.3F).rain(0.8F)));
            registerBiome(34, "extreme_hills_with_hillss", BigHills.get(BiomeBigHills.Type.EXTRA_TREES, new BiomeData("Extreme Hills+").height(1.0F).hills(0.05F).temp(0.2F).rain(0.3F)));
            registerBiome(35, "savanna", Plains.get(false, new BiomeData("Plains").height(0.125F).hills(0.05F).temp(0.8F).rain(0.4F)));
            registerBiome(36, "savanna_rock", new BiomeForest(BiomeForest.Type.NORMAL, new BiomeData("Forest").temp(0.7F).rain(0.8F).hills(0.05F)));
            registerBiome(37, "mesa", new BiomeForest(BiomeForest.Type.NORMAL, new BiomeData("Forest").temp(0.7F).rain(0.8F).hills(0.05F)));
            registerBiome(38, "mesa_rock", new BiomeForest(BiomeForest.Type.NORMAL, new BiomeData("Forest").temp(0.7F).rain(0.8F).hills(0.05F)));
            registerBiome(39, "mesa_clear_rock", new BiomeForest(BiomeForest.Type.NORMAL, new BiomeData("Forest").temp(0.7F).rain(0.8F).hills(0.05F)));
            registerBiome(127, "void", new BiomeVoid(new BiomeData("The Void")));
            registerBiome(129, "mutated_plains", Plains.get(true, new BiomeData("Sunflower Plains").name("plains").height(0.125F).hills(0.05F).temp(0.8F).rain(0.4F)));
            registerBiome(130, "mutated_desert", new BiomeDesert(new BiomeData("Desert M").name("desert").height(0.225F).hills(0.05F).temp(2.0F).rain(0.0F)));
            registerBiome(131, "mutated_extreme_hills", BigHills.get(BiomeBigHills.Type.MUTATED, new BiomeData("Extreme Hills M").name("extreme_hills").height(1.0F).hills(0.05F).temp(0.2F).rain(0.3F)));
            registerBiome(132, "mutated_forest", new BiomeForest(BiomeForest.Type.FLOWER, new BiomeData("Flower Forest").name("forest").hills(0.05F).temp(0.7F).rain(0.8F)));
            registerBiome(133, "mutated_taiga", new BiomeTaiga(BiomeTaiga.Type.NORMAL, new BiomeData("Taiga M").name("taiga").height(0.3F).hills(0.05F).temp(0.25F).rain(0.8F)));
            registerBiome(134, "mutated_swampland", Swamp.get(new BiomeData("Swampland M").name("swampland").height(-0.1F).hills(0.05F).temp(0.8F).rain(0.9F).spcseed(14745518)));
            registerBiome(140, "mutated_ice_flats", new BiomeIcePlains(true, new BiomeData("Ice Plains Spikes").name("ice_flats").height(0.425F).hills(0.05F).temp(0.0F).rain(0.5F)));
            registerBiome(149, "mutated_jungle", new BiomeForest(BiomeForest.Type.NORMAL, new BiomeData("Forest").temp(0.7F).rain(0.8F).hills(0.05F)));
            registerBiome(151, "mutated_jungle_edge", new BiomeForest(BiomeForest.Type.NORMAL, new BiomeData("Forest").temp(0.7F).rain(0.8F).hills(0.05F)));
            registerBiome(155, "mutated_birch_forest", new BiomeForestMutated(new BiomeData("Birch Forest M").name("birch_forest").height(0.2F).hills(0.05F).temp(0.6F).rain(0.6F)));
            registerBiome(156, "mutated_birch_forest_hills", new BiomeForestMutated(new BiomeData("Birch Forest Hills M").name("birch_forest").height(0.55F).hills(0.05F).temp(0.6F).rain(0.6F)));
            registerBiome(157, "mutated_roofed_forest", new BiomeForest(BiomeForest.Type.ROOFED, new BiomeData("Roofed Forest M").name("roofed_forest").height(0.2F).hills(0.05F).temp(0.7F).rain(0.8F)));
            registerBiome(158, "mutated_taiga_cold", new BiomeTaiga(BiomeTaiga.Type.NORMAL, new BiomeData("Cold Taiga M").name("taiga_cold").height(0.3F).hills(0.05F).temp(-0.5F).rain(0.4F)));
            registerBiome(160, "mutated_redwood_taiga", new BiomeTaiga(BiomeTaiga.Type.MEGA_SPRUCE, new BiomeData("Mega Spruce Taiga").name("redwood_taiga").height(0.2F).hills(0.05F).temp(0.25F).rain(0.8F)));
            registerBiome(161, "mutated_redwood_taiga_hills", new BiomeTaiga(BiomeTaiga.Type.MEGA_SPRUCE, new BiomeData("Redwood Taiga Hills M").name("redwood_taiga_hills").height(0.2F).hills(0.05F).temp(0.25F).rain(0.8F)));
            registerBiome(162, "mutated_extreme_hills_with_hillss", BigHills.get(BiomeBigHills.Type.MUTATED, new BiomeData("Extreme Hills+ M").name("extreme_hills_with_hillss").height(1.0F).hills(0.05F).temp(0.2F).rain(0.3F)));
            registerBiome(163, "mutated_savanna", new BiomeSavannaMutated(new BiomeData("Savanna M").name("savanna").height(0.3625F).hills(0.05F).temp(1.1F).rain(0.0F)));
            registerBiome(164, "mutated_savanna_rock", new BiomeSavannaMutated(new BiomeData("Savanna Plateau M").name("savanna_rock").height(1.05F).hills(0.05F).temp(1.0F).rain(0.0F)));
            registerBiome(165, "mutated_mesa", new BiomeForest(BiomeForest.Type.NORMAL, new BiomeData("Forest").temp(0.7F).rain(0.8F).hills(0.05F)));
            registerBiome(166, "mutated_mesa_rock", Plains.get(false, new BiomeData("Plains").height(0.125F).hills(0.05F).temp(0.8F).rain(0.4F)));
            registerBiome(167, "mutated_mesa_clear_rock", Plains.get(false, new BiomeData("Plains").height(0.125F).hills(0.05F).temp(0.8F).rain(0.4F)));


            /** Step 3 : fix the biome constant fields in Biomes.class */
            // Biomes.class contains 62 biome constants which are set to various biomes by name when the server starts.
            // All the fields are static final, and can only be modified by reflection.
            // This is a list of the name of each field an the matching biome.
            // If the MC version changes, these fields and names may be inaccurate. Copy the new ones out of net.minecraft.server.Biomes
            //net.minecraft.server.v1_9_R2.Biomes
            ArrayList<KV<String,String>> biomes = new ArrayList<>(62);
            biomes.add(new KV<>("a", "ocean"));
            biomes.add(new KV<>("b", "ocean")); // this seems to be an identical "error" version of ocean, used when an impossible ID turns up
            biomes.add(new KV<>("c", "plains"));
            biomes.add(new KV<>("d", "desert"));
            biomes.add(new KV<>("e", "extreme_hills"));
            biomes.add(new KV<>("f", "forest"));
            biomes.add(new KV<>("g","taiga"));
            biomes.add(new KV<>("h","swampland"));
            biomes.add(new KV<>("i","river"));
            biomes.add(new KV<>("j","hell"));
            biomes.add(new KV<>("k","sky"));
            biomes.add(new KV<>("l","frozen_ocean"));
            biomes.add(new KV<>("m","frozen_river"));
            biomes.add(new KV<>("n","ice_flats"));
            biomes.add(new KV<>("o","ice_mountains"));
            biomes.add(new KV<>("p","mushroom_island"));
            biomes.add(new KV<>("q","mushroom_island_shore"));
            biomes.add(new KV<>("r","beaches"));
            biomes.add(new KV<>("s","desert_hills"));
            biomes.add(new KV<>("t","forest_hills"));
            biomes.add(new KV<>("u","taiga_hills"));
            biomes.add(new KV<>("v","smaller_extreme_hills"));
            biomes.add(new KV<>("w","jungle"));
            biomes.add(new KV<>("x","jungle_hills"));
            biomes.add(new KV<>("y","jungle_edge"));
            biomes.add(new KV<>("z","deep_ocean"));
            biomes.add(new KV<>("A","stone_beach"));
            biomes.add(new KV<>("B","cold_beach"));
            biomes.add(new KV<>("C","birch_forest"));
            biomes.add(new KV<>("D","birch_forest_hills"));
            biomes.add(new KV<>("E","roofed_forest"));
            biomes.add(new KV<>("F","taiga_cold"));
            biomes.add(new KV<>("G","taiga_cold_hills"));
            biomes.add(new KV<>("H","redwood_taiga"));
            biomes.add(new KV<>("I","redwood_taiga_hills"));
            biomes.add(new KV<>("J","extreme_hills_with_trees"));
            biomes.add(new KV<>("K","savanna"));
            biomes.add(new KV<>("L","savanna_rock"));
            biomes.add(new KV<>("M","mesa"));
            biomes.add(new KV<>("N","mesa_rock"));
            biomes.add(new KV<>("O","mesa_clear_rock"));
            biomes.add(new KV<>("P","void"));
            biomes.add(new KV<>("Q","mutated_plains"));
            biomes.add(new KV<>("R","mutated_desert"));
            biomes.add(new KV<>("S","mutated_extreme_hills"));
            biomes.add(new KV<>("T","mutated_forest"));
            biomes.add(new KV<>("U","mutated_taiga"));
            biomes.add(new KV<>("V","mutated_swampland"));
            biomes.add(new KV<>("W","mutated_ice_flats"));
            biomes.add(new KV<>("X","mutated_jungle"));
            biomes.add(new KV<>("Y","mutated_jungle_edge"));
            biomes.add(new KV<>("Z","mutated_birch_forest"));
            biomes.add(new KV<>("aa","mutated_birch_forest_hills"));
            biomes.add(new KV<>("ab","mutated_roofed_forest"));
            biomes.add(new KV<>("ac","mutated_taiga_cold"));
            biomes.add(new KV<>("ad","mutated_redwood_taiga"));
            biomes.add(new KV<>("ae","mutated_redwood_taiga_hills"));
            biomes.add(new KV<>("af","mutated_extreme_hills_with_trees"));
            biomes.add(new KV<>("ag","mutated_savanna"));
            biomes.add(new KV<>("ah","mutated_savanna_rock"));
            biomes.add(new KV<>("ai","mutated_mesa"));
            biomes.add(new KV<>("aj","mutated_mesa_rock"));
            biomes.add(new KV<>("ak", "mutated_mesa_clear_rock"));

            // Loop through the list of key-values and update each field with the new biome data
            for(KV<String,String> kv : biomes)
            {
                try{
                    f = Biomes.class.getDeclaredField(kv.key); // get the field from Biomes.class
                    m.setInt(f, f.getModifiers() & ~Modifier.FINAL); // bypass final
                    f.set(null, getByName(kv.value)); // get the biome by name and set it to the field
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            /** Step 4 : Clear BiomeBase.i ( a public Set<BiomeBase> )  and refill it with the modified biomes */

            BiomeBase.i.clear(); // yet another list of biomes, clear it

            Collections.addAll(BiomeBase.i, new BiomeBase[] // replace contents of i with the updated Biomes.class fields
                    {
                            Biomes.a, Biomes.c, Biomes.d, Biomes.e, Biomes.f, Biomes.g, Biomes.h, Biomes.i, Biomes.m, Biomes.n, Biomes.o, Biomes.p,
                            Biomes.q, Biomes.r, Biomes.s, Biomes.t, Biomes.u, Biomes.w, Biomes.x, Biomes.y, Biomes.z, Biomes.A, Biomes.B, Biomes.C,
                            Biomes.D, Biomes.E, Biomes.F, Biomes.G, Biomes.H, Biomes.I, Biomes.J, Biomes.K, Biomes.L, Biomes.M, Biomes.N, Biomes.O
                    });

        }
        catch(IllegalArgumentException
                | IllegalAccessException
                | NoSuchFieldException
                | SecurityException m)
        {
            m.printStackTrace(); // If this throws an error, chances are MC updated and some of the field names changed.
        }
    }

    private static void registerBiome(int paramInt, String paramString, BiomeBase newBiomeBase)
    {
        Field refH;
        try
        {
            refH = BiomeBase.class.getDeclaredField("H");
            refH.setAccessible(true);

            Field m = Field.class.getDeclaredField("modifiers");
            m.setAccessible(true);
            m.setInt(refH, refH.getModifiers() & ~Modifier.FINAL);

            BiomeBase.REGISTRY_ID.a(paramInt, new MinecraftKey(paramString), newBiomeBase);

            if (newBiomeBase.b()) // name is not null
            {
                BiomeBase.j.a(newBiomeBase, BiomeBase.a((BiomeBase)BiomeBase.REGISTRY_ID.get(new MinecraftKey((java.lang.String) refH.get(newBiomeBase)))));
            }
        }
        catch(NoSuchFieldException|SecurityException | IllegalArgumentException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    /** This extends the mostly private class "a" inside BiomeBase.class, and lets parameters for biomes be set without pages of messy reflection */
    public static class BiomeData extends net.minecraft.server.v1_9_R2.BiomeBase.a // blame mojang for this OOP horror
    {
        // constructor with biome name
        public BiomeData(String arg0)
        {
            super(arg0);
        }
        // parameter set methods
        BiomeData height(float c) // the base terrain height, -1.8 in deep ocean up to 1.0 or higher in mountains
        {
            return (BiomeData) super.c(c);
        }
        BiomeData hills(float d) // amount of hilly terrain,  0.0 to 1.0
        {
            return (BiomeData) super.d(d);
        }
        BiomeData temp(float a) // sets the temperature, -1.0 to 2.0
        {
            return (BiomeData) super.a(a);
        }
        BiomeData rain(float b) // sets the rainfall level, as far as i can tell  (determines various other things, not actual rain weather)
        {
            return (BiomeData) super.b(b);
        }
        BiomeData spcseed(int i) // sets some sort of special seed used for features in this biome
        {
            return (BiomeData) super.a(i);
        }
        BiomeData name(String s)
        {
            return (BiomeData) super.a(s);
        }
    }

    /** These four classes only exist to allow access to protected constructors in a few biomes: more inconsistency trolling from mojang*/
    static class Plains extends BiomePlains // mojang makes this OOP heresy required.
    {
        protected Plains(boolean b, a data) { super(b, data); }
        static BiomePlains get(boolean b, a data){ return new Plains(b, data); }
    }
    static class Swamp extends BiomeSwamp
    {
        protected Swamp(a data) { super(data); }
        static BiomeSwamp get(a data) { return new Swamp(data); }
    }
    static class BigHills extends BiomeBigHills
    {

        protected BigHills(Type type, a data) { super(type, data); }
        static BiomeBigHills get(Type t, a data) { return new BigHills(t,data); }
    }
    static class Savanna extends BiomeSavanna
    {
        protected Savanna(a data) { super(data); }
        static BiomeSavanna get(a data) { return new Savanna(data); }
    }

    /**  This is a private method copied from BiomeBase.a so we can actually use it */
    private static BiomeBase getByName(String name)
    {
        BiomeBase getbiome = (BiomeBase)BiomeBase.REGISTRY_ID.get(new MinecraftKey(name));
        if (getbiome == null)
        {
            throw new IllegalStateException("Invalid Biome requested: " + name);
        }
        return getbiome;
    }
}

class KV<K,V> // just a simpler Pair class
{
    public K key;
    public V value;
    public KV(K key, V value)
    {
        this.key=key;
        this.value=value;
    }
}


