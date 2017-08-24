package net.samagames.survivalapi.games.uhcrandom;

import net.samagames.survivalapi.SurvivalGenerator;
import net.samagames.survivalapi.games.AbstractGame;
import net.samagames.survivalapi.gen.WorldGenMonumentPatched;
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
public class UHCRandomGame extends AbstractGame
{
    public UHCRandomGame(SurvivalGenerator plugin)
    {
        super(plugin);
    }

    @Override
    public void preInit(){}

    @Override
    public void init(World world)
    {
        try
        {
            WorldGenMonumentPatched.load(world);
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
        }

        String gen = this.plugin.getConfig().getString("generator", null);

        if (gen == null)
            return;

        if (gen.equalsIgnoreCase("bigcrack"))
            world.getPopulators().add(new BigCrackPopulator());
        else if (gen.equalsIgnoreCase("chunkapocalypse"))
            world.getPopulators().add(new ChunkApocalypsePopulator());
        else
            return;

        this.plugin.getLogger().info("Added " + gen + " generator");
    }
}
