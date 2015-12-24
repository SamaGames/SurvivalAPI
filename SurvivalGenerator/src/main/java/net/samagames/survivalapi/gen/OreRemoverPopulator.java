package net.samagames.survivalapi.gen;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        /**for (int x = chunk.getX() * 16; x < x + 16; x++)
        {
            for (int z = chunk.getZ() * 16; z < z + 16; z++)
            {
                for (int y = 0; y < 250; y++)
                {
                    Block block = chunk.getBlock(x, y, z);

                    if (this.toRemove.contains(block.getType()))
                        block.setType(Material.STONE);
                }
            }
        }**/
    }

    public void removeOre(Material material)
    {
        this.toRemove.add(material);
    }
}
