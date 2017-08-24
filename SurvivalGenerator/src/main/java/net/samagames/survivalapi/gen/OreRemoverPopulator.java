package net.samagames.survivalapi.gen;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
public class OreRemoverPopulator extends BlockPopulator
{
    private final List<Material> toRemove;

    public OreRemoverPopulator()
    {
        this.toRemove = new ArrayList<>();
    }

    @Override
    public void populate(World world, Random random, Chunk chunk)
    {
        for (int x = chunk.getX(); x < chunk.getX() + 16; x++)
        {
            for (int z = chunk.getZ(); z < chunk.getZ() + 16; z++)
            {
                for (int y = 0; y < 250; y++)
                {
                    Block block = chunk.getBlock(x, y, z);

                    if (this.toRemove.contains(block.getType()))
                        block.setType(Material.STONE);
                }
            }
        }
    }

    public void removeOre(Material material)
    {
        this.toRemove.add(material);
    }
}
