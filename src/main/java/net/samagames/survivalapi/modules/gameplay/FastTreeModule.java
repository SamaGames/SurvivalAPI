package net.samagames.survivalapi.modules.gameplay;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class FastTreeModule extends AbstractSurvivalModule
{
    private SurvivalGame game;

    public FastTreeModule(SurvivalPlugin plugin, SurvivalAPI api, HashMap<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    @Override
    public void onGameStart(SurvivalGame game)
    {
        this.game = game;
    }

    /**
     * Tagging placed logs
     *
     * @param event Event
     */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        Block block = event.getBlock();

        if (block.getType().equals(Material.LOG) || block.getType().equals(Material.LOG_2))
            block.setMetadata("placed", new FixedMetadataValue(this.plugin, event.getPlayer().getUniqueId()));
    }

    /**
     * Breaking all the tree
     *
     * @param event Event
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        Material material = event.getBlock().getType();

        if (material == Material.LOG || material == Material.LOG_2)
        {
            ArrayList<Block> bList = new ArrayList<>();
            bList.add(event.getBlock());

            this.checkLeaves(event.getBlock());

            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    for (int i = 0; i < bList.size(); i++)
                    {
                        Block block = bList.get(i);

                        if (block.hasMetadata("placed"))
                        {
                            bList.remove(block);
                            continue;
                        }
                        if (block.getType() == Material.LOG || block.getType() == Material.LOG_2)
                        {
                            for (ItemStack item : block.getDrops())
                                block.getWorld().dropItemNaturally(block.getLocation(), item);

                            block.setType(Material.AIR);
                            checkLeaves(block);
                        }
                        for (BlockFace face : BlockFace.values())
                            if (block.getRelative(face).getType() == Material.LOG || block.getRelative(face).getType() == Material.LOG_2)
                                bList.add(block.getRelative(face));

                        bList.remove(block);
                    }

                    if (bList.isEmpty())
                        this.cancel();
                }
            }.runTaskTimer(this.plugin, 2, 1);
        }

        event.getPlayer().giveExp(event.getExpToDrop() * 2);
    }

    private void checkLeaves(Block block)
    {
        Location loc = block.getLocation();
        final World world = loc.getWorld();
        final int x = loc.getBlockX();
        final int y = loc.getBlockY();
        final int z = loc.getBlockZ();
        final int range = 4;
        final int off = range + 1;

        if (!validChunk(world, x - off, y - off, z - off, x + off, y + off, z + off))
            return;

        this.plugin.getServer().getScheduler().runTask(this.plugin, () ->
        {
            for (int offX = -range; offX <= range; offX++)
                for (int offY = -range; offY <= range; offY++)
                    for (int offZ = -range; offZ <= range; offZ++)
                        if (world.getBlockAt(x + offX, y + offY, z + offZ).getType() == Material.LEAVES || world.getBlockAt(x + offX, y + offY, z + offZ).getType() == Material.LEAVES_2)
                            breakLeaf(world, x + offX, y + offY, z + offZ);
        });
    }

    private void breakLeaf(World world, int x, int y, int z)
    {
        Block block = world.getBlockAt(x, y, z);

        byte range = 4;
        byte max = 32;
        int[] blocks = new int[max * max * max];
        int off = range + 1;
        int mul = max * max;
        int div = max / 2;


        if (validChunk(world, x - off, y - off, z - off, x + off, y + off, z + off))
        {
            int offX;
            int offY;
            int offZ;

            for (offX = -range; offX <= range; offX++)
            {
                for (offY = -range; offY <= range; offY++)
                {
                    for (offZ = -range; offZ <= range; offZ++)
                    {
                        Material mat = world.getBlockAt(x + offX, y + offY, z + offZ).getType();
                        blocks[(offX + div) * mul + (offY + div) * max + offZ + div] = mat == Material.LOG || mat == Material.LOG_2 ? 0 : mat == Material.LEAVES || mat == Material.LEAVES_2 ? -2 : -1;
                    }
                }
            }

            for (offX = 1; offX <= 4; offX++)
            {
                for (offY = -range; offY <= range; offY++)
                {
                    for (offZ = -range; offZ <= range; offZ++)
                    {
                        for (int i = -range; i <= range; i++)
                        {
                            if (blocks[(offY + div) * mul + (offZ + div) * max + i + div] == offX - 1)
                            {
                                if (blocks[(offY + div - 1) * mul + (offZ + div) * max + i + div] == -2)
                                    blocks[(offY + div - 1) * mul + (offZ + div) * max + i + div] = offX;

                                if (blocks[(offY + div + 1) * mul + (offZ + div) * max + i + div] == -2)
                                    blocks[(offY + div + 1) * mul + (offZ + div) * max + i + div] = offX;

                                if (blocks[(offY + div) * mul + (offZ + div - 1) * max + i + div] == -2)
                                    blocks[(offY + div) * mul + (offZ + div - 1) * max + i + div] = offX;

                                if (blocks[(offY + div) * mul + (offZ + div + 1) * max + i + div] == -2)
                                    blocks[(offY + div) * mul + (offZ + div + 1) * max + i + div] = offX;

                                if (blocks[(offY + div) * mul + (offZ + div) * max + (i + div - 1)] == -2)
                                    blocks[(offY + div) * mul + (offZ + div) * max + (i + div - 1)] = offX;

                                if (blocks[(offY + div) * mul + (offZ + div) * max + i + div + 1] == -2)
                                    blocks[(offY + div) * mul + (offZ + div) * max + i + div + 1] = offX;
                            }
                        }
                    }
                }
            }
        }

        if (blocks[div * mul + div * max + div] < 0)
        {
            LeavesDecayEvent event = new LeavesDecayEvent(block);
            Bukkit.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled())
                return;

            block.breakNaturally();

            if (10 > new Random().nextInt(100))
                world.playEffect(block.getLocation(), Effect.STEP_SOUND, Material.LEAVES);
        }
    }

    private boolean validChunk(World world, int minX, int minY, int minZ, int maxX, int maxY, int maxZ)
    {
        if (maxY >= 0 && minY < world.getMaxHeight())
        {
            minX >>= 4;
            minZ >>= 4;
            maxX >>= 4;
            maxZ >>= 4;

            for (int x = minX; x <= maxX; x++)
                for (int z = minZ; z <= maxZ; z++)
                    if (!world.isChunkLoaded(x, z))
                        return false;

            return true;
        }

        return false;
    }
}
