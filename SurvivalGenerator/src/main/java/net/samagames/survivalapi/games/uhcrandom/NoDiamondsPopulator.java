package net.samagames.survivalapi.games.uhcrandom;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

/**
 * Created by Rigner for project SurvivalGenerator.
 */
public class NoDiamondsPopulator extends BlockPopulator
{
    @Override
    public void populate(World world, Random random, Chunk chunk)
    {
        for(int x = 0; x < 16; x++)
        {
            for(int y = 0; y < 256; y++)
            {
                for(int z = 0; z < 16; z++)
                {
                    if (chunk.getBlock(x, y, z).getType() == Material.DIAMOND_ORE)
                        chunk.getBlock(x, y, z).setType(Material.DIAMOND_ORE);
                }
            }
        }
    }
}
