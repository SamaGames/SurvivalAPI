package net.samagames.survivalapi.games.uhcrun;

import net.minecraft.server.v1_8_R3.BiomeBase;
import net.samagames.survivalapi.SurvivalGenerator;
import net.samagames.survivalapi.games.AbstractGame;
import net.samagames.survivalapi.gen.BetterCenter;
import net.samagames.survivalapi.gen.OrePopulator;
import net.samagames.survivalapi.gen.WorldGenCavesPatched;
import net.samagames.survivalapi.gen.WorldGenMonumentPatched;
import org.bukkit.Material;
import org.bukkit.World;

/*
 * This file is part of SurvivalAPI.
 *
 * SurvivalAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SurvivalAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SurvivalAPI.  If not, see <http://www.gnu.org/licenses/>.
 */
public class UHCRunGame extends AbstractGame
{
    public UHCRunGame(SurvivalGenerator plugin)
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

        orePopulator.addRule(new OrePopulator.Rule(Material.DIAMOND_ORE, 4, 0, 64, 5));
        orePopulator.addRule(new OrePopulator.Rule(Material.IRON_ORE, 2, 0, 64, 15));
        orePopulator.addRule(new OrePopulator.Rule(Material.GOLD_ORE, 2, 0, 64, 8));
        orePopulator.addRule(new OrePopulator.Rule(Material.LAPIS_ORE, 3, 0, 64, 4));
        orePopulator.addRule(new OrePopulator.Rule(Material.OBSIDIAN, 4, 0, 32, 6));

        world.getPopulators().add(orePopulator);
        world.getPopulators().add(new FortressPopulator(this.plugin));
    }
}
