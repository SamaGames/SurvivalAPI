package net.samagames.survivalapi.games.uhcrun;

import net.samagames.survivalapi.SurvivalGenerator;
import net.samagames.survivalapi.games.AbstractGame;
import net.samagames.survivalapi.generation.OrePopulator;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.world.WorldInitEvent;

public class UHCRunGame extends AbstractGame
{
    public UHCRunGame(SurvivalGenerator plugin)
    {
        super(plugin);
    }

    @Override
    public void preInit() {}

    @Override
    public void init(WorldInitEvent event)
    {
        World world = event.getWorld();

        if (world.getEnvironment() == World.Environment.NORMAL)
        {
            OrePopulator orePopulator = new OrePopulator();

            orePopulator.addRule(new OrePopulator.Rule(Material.DIAMOND_ORE, 4, 0, 64, 5));
            orePopulator.addRule(new OrePopulator.Rule(Material.IRON_ORE, 2, 0, 64, 15));
            orePopulator.addRule(new OrePopulator.Rule(Material.GOLD_ORE, 2, 0, 64, 8));
            orePopulator.addRule(new OrePopulator.Rule(Material.LAPIS_ORE, 3, 0, 64, 4));
            orePopulator.addRule(new OrePopulator.Rule(Material.OBSIDIAN, 4, 0, 32, 6));

            FortressPopulator fortressPopulator = new FortressPopulator(this.plugin);

            fortressPopulator.addRule(new FortressPopulator.Rule(Material.GOLD_INGOT, 2, 1, 12, 30));
            fortressPopulator.addRule(new FortressPopulator.Rule(Material.NETHER_STALK, 4, 1, 10, 30));
            fortressPopulator.addRule(new FortressPopulator.Rule(Material.GLOWSTONE_DUST, 1, 1, 4, 30));
            fortressPopulator.addRule(new FortressPopulator.Rule(Material.DIAMOND, 1, 1, 9, 30));

            world.getPopulators().add(orePopulator);
            world.getPopulators().add(fortressPopulator);
        }
    }
}
