package net.samagames.survivalapi.games.randomrun;

import net.minecraft.server.v1_8_R3.BiomeBase;
import net.samagames.survivalapi.SurvivalGenerator;
import net.samagames.survivalapi.games.AbstractGame;
import net.samagames.survivalapi.gen.*;
import net.samagames.survivalapi.games.uhcrandom.*;
import net.samagames.survivalapi.games.uhcrun.*;
import org.bukkit.Material;
import org.bukkit.World;

public class RandomRunGame extends AbstractGame
{
    public RandomRunGame(SurvivalGenerator plugin)
    {
        super(plugin);
    }

    @Override
    public void preInit()
    {
        this.plugin.saveResource("uhcrun_nether_1.schematic", true);
        this.plugin.saveResource("uhcrun_nether_2.schematic", true);

        this.plugin.addBiomeToRemove(BiomeBase.FROZEN_RIVER);
        this.plugin.addBiomeToRemove(BiomeBase.ICE_PLAINS);
        this.plugin.addBiomeToRemove(BiomeBase.ICE_MOUNTAINS);
        this.plugin.addBiomeToRemove(BiomeBase.MUSHROOM_ISLAND);
        this.plugin.addBiomeToRemove(BiomeBase.MUSHROOM_SHORE);
        this.plugin.addBiomeToRemove(BiomeBase.JUNGLE);
        this.plugin.addBiomeToRemove(BiomeBase.JUNGLE_HILLS);
        this.plugin.addBiomeToRemove(BiomeBase.JUNGLE_EDGE);
        this.plugin.addBiomeToRemove(BiomeBase.COLD_BEACH);
        this.plugin.addBiomeToRemove(BiomeBase.COLD_TAIGA);
        this.plugin.addBiomeToRemove(BiomeBase.COLD_TAIGA_HILLS);
        this.plugin.addBiomeToRemove(BiomeBase.MEGA_TAIGA);
        this.plugin.addBiomeToRemove(BiomeBase.MEGA_TAIGA_HILLS);
        this.plugin.addBiomeToRemove(BiomeBase.MESA);
        this.plugin.addBiomeToRemove(BiomeBase.STONE_BEACH);
    }

    @Override
    public void init(World world)
    {
        try
        {
            BetterCenter.load();

            WorldGenCavesPatched.load(world, 9);
            WorldGenMonumentPatched.load(world);
        }
        catch (ReflectiveOperationException e)
        {
            e.printStackTrace();
        }

        OrePopulator orePopulator = new OrePopulator();

        orePopulator.addRule(new OrePopulator.Rule(Material.DIAMOND_ORE, 4, 0, 64, 6));
        orePopulator.addRule(new OrePopulator.Rule(Material.IRON_ORE, 2, 0, 64, 15));
        orePopulator.addRule(new OrePopulator.Rule(Material.GOLD_ORE, 2, 0, 64, 8));
        orePopulator.addRule(new OrePopulator.Rule(Material.LAPIS_ORE, 3, 0, 64, 4));
        orePopulator.addRule(new OrePopulator.Rule(Material.OBSIDIAN, 4, 0, 32, 6));

        world.getPopulators().add(orePopulator);
        world.getPopulators().add(new FortressPopulator(this.plugin));

        String gen = this.plugin.getConfig().getString("generator", null);

        if (gen == null)
            return;

        switch (gen.toLowerCase())
        {
            case "bigcrack":
                world.getPopulators().add(new BigCrackPopulator());
                break;

            case "chunkapocalypse":
                world.getPopulators().add(new ChunkApocalypsePopulator());
                break;

            case "nodiamonds":
                OreRemoverPopulator oreRemoverPopulator = new OreRemoverPopulator();
                oreRemoverPopulator.removeOre(Material.DIAMOND_ORE);
                world.getPopulators().add(oreRemoverPopulator);
                break;

            default:
                return;
        }

        this.plugin.getLogger().info("Added " + gen + " generator");
    }
}
