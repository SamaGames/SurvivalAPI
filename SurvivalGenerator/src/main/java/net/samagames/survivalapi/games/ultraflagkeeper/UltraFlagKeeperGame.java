package net.samagames.survivalapi.games.ultraflagkeeper;

import net.samagames.survivalapi.SurvivalGenerator;
import net.samagames.survivalapi.games.AbstractGame;
import net.samagames.survivalapi.gen.OrePopulator;
import net.samagames.survivalapi.gen.OreRemoverPopulator;
import net.samagames.survivalapi.gen.WorldGenCaves;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;

public class UltraFlagKeeperGame extends AbstractGame
{
    public UltraFlagKeeperGame(SurvivalGenerator plugin)
    {
        super(plugin);
    }

    @Override
    public void preInit()
    {
        /*this.plugin.removeBiome(11, new MinecraftKey("frozen_river"), SurvivalGenerator.BIOME_PLAINS);
        this.plugin.removeBiome(12, new MinecraftKey("ice_flats"), SurvivalGenerator.BIOME_PLAINS);
        this.plugin.removeBiome(13, new MinecraftKey("ice_mountains"), SurvivalGenerator.BIOME_FOREST);
        this.plugin.removeBiome(14, new MinecraftKey("mushroom_island"), SurvivalGenerator.BIOME_FOREST);
        this.plugin.removeBiome(15, new MinecraftKey("mushroom_island_shore"), SurvivalGenerator.BIOME_FOREST);
        this.plugin.removeBiome(21, new MinecraftKey("jungle"), SurvivalGenerator.BIOME_FOREST);
        this.plugin.removeBiome(22, new MinecraftKey("jungle_hills"), SurvivalGenerator.BIOME_PLAINS);
        this.plugin.removeBiome(23, new MinecraftKey("jungle_edge"), SurvivalGenerator.BIOME_PLAINS);
        this.plugin.removeBiome(26, new MinecraftKey("cold_beach"), SurvivalGenerator.BIOME_PLAINS);
        this.plugin.removeBiome(30, new MinecraftKey("taiga_cold"), SurvivalGenerator.BIOME_FOREST);
        this.plugin.removeBiome(31, new MinecraftKey("taiga_cold_hills"), SurvivalGenerator.BIOME_FOREST);
        this.plugin.removeBiome(32, new MinecraftKey("redwood_taiga"), SurvivalGenerator.BIOME_FOREST);
        this.plugin.removeBiome(33, new MinecraftKey("redwood_taiga_hills"), SurvivalGenerator.BIOME_PLAINS);
        this.plugin.removeBiome(35, new MinecraftKey("savanna"), SurvivalGenerator.BIOME_FOREST);
        this.plugin.removeBiome(36, new MinecraftKey("savanna_rock"), SurvivalGenerator.BIOME_PLAINS);
        this.plugin.removeBiome(37, new MinecraftKey("mesa"), SurvivalGenerator.BIOME_FOREST);
        this.plugin.removeBiome(38, new MinecraftKey("mesa_rock"), SurvivalGenerator.BIOME_PLAINS);
        this.plugin.removeBiome(39, new MinecraftKey("mesa_clear_rock"), SurvivalGenerator.BIOME_PLAINS);*/

        WorldGeneratorUFK.patchBiomeParams();

        this.plugin.saveResource("ufk_flag.schematic", true);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void init(World world)
    {
        try
        {
            WorldGenCaves.loadForWorld(world, 2);
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
        }

        OrePopulator orePopulator = new OrePopulator();
        OreRemoverPopulator oreRemoverPopulator = new OreRemoverPopulator();

        orePopulator.addRule(new OrePopulator.Rule(Material.DIAMOND_ORE, 4, 0, 64, 8));
        orePopulator.addRule(new OrePopulator.Rule(Material.IRON_ORE, 2, 0, 64, 15));
        orePopulator.addRule(new OrePopulator.Rule(Material.OBSIDIAN, 4, 0, 64, 10));
        orePopulator.addRule(new OrePopulator.Rule(Material.QUARTZ_BLOCK, 3, 0, 64, 6));

        oreRemoverPopulator.removeOre(Material.GOLD_ORE);
        oreRemoverPopulator.removeOre(Material.LAPIS_ORE);

        world.getPopulators().add(orePopulator);
        world.getPopulators().add(oreRemoverPopulator);
    }

    @Override
    public void onFinish(World world)
    {
        Bukkit.getScheduler().runTaskLater(plugin, () -> new FlagPopulator(plugin).populate(world), 40L);

    }
}
