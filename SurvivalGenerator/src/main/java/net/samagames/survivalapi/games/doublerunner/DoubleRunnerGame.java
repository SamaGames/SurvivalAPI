package net.samagames.survivalapi.games.doublerunner;

import net.samagames.survivalapi.SurvivalGenerator;
import net.samagames.survivalapi.games.AbstractGame;
import net.samagames.survivalapi.gen.OrePopulator;
import net.samagames.survivalapi.gen.OreRemoverPopulator;
import org.bukkit.Material;
import org.bukkit.World;

public class DoubleRunnerGame extends AbstractGame
{
    public DoubleRunnerGame(SurvivalGenerator plugin)
    {
        super(plugin);
    }

    @Override
    public void preInit() {}

    @Override
    public void init(World world)
    {
        OrePopulator orePopulator = new OrePopulator();

        orePopulator.addRule(new OrePopulator.Rule(Material.DIAMOND_ORE, 4, 0, 64, 8));
        orePopulator.addRule(new OrePopulator.Rule(Material.IRON_ORE, 2, 0, 64, 15));
        orePopulator.addRule(new OrePopulator.Rule(Material.OBSIDIAN, 4, 0, 32, 10));
        orePopulator.addRule(new OrePopulator.Rule(Material.QUARTZ_BLOCK, 4, 0, 64, 5));

        world.getPopulators().add(orePopulator);

        OreRemoverPopulator oreRemoverPopulator = new OreRemoverPopulator();

        oreRemoverPopulator.removeOre(Material.GOLD_ORE);
        oreRemoverPopulator.removeOre(Material.LAPIS_ORE);

        world.getPopulators().add(oreRemoverPopulator);
    }
}
