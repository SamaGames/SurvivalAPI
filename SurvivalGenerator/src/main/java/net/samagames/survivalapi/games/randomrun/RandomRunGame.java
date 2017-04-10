package net.samagames.survivalapi.games.randomrun;

import net.samagames.survivalapi.SurvivalGenerator;
import net.samagames.survivalapi.games.AbstractGame;
import net.samagames.survivalapi.gen.OrePopulator;
import net.samagames.survivalapi.gen.WorldGenCaves;
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
    }

    @Override
    public void init(World world)
    {
        try
        {
            WorldGenCaves.loadForWorld(world, 9);
        }
        catch (NoSuchFieldException | IllegalAccessException e)
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
                world.getPopulators().add(new NoDiamondsPopulator());
                break;

            default:
                return;
        }

        this.plugin.getLogger().info("Added " + gen + " generator");
    }
}
