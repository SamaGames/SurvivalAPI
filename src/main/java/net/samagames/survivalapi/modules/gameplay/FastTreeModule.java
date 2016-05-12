package net.samagames.survivalapi.modules.gameplay;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

/**
 * FastTreeModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class FastTreeModule extends AbstractSurvivalModule
{
    private final List<BlockFace> faces;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public FastTreeModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);

        this.faces = new ArrayList<>();
        this.faces.add(BlockFace.NORTH);
        this.faces.add(BlockFace.EAST);
        this.faces.add(BlockFace.SOUTH);
        this.faces.add(BlockFace.WEST);
        this.faces.add(BlockFace.UP);
        this.faces.add(BlockFace.DOWN);
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
    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event)
    {
        Material material = event.getBlock().getType();

        if (material == Material.LOG || material == Material.LOG_2)
            Bukkit.getScheduler().runTask(this.plugin, () -> removeTree(event.getBlock(), true, 3));

        event.getPlayer().giveExp(event.getExpToDrop() * 2);
    }

    private void removeTree(Block block, boolean nearwood, int range)
    {
        if (range < 0 || block.hasMetadata("placed"))
            return;

        this.plugin.getServer().getScheduler().runTask(this.plugin, () ->
        {
            if (block.getType() == Material.LEAVES || block.getType() == Material.LEAVES_2)
            {
                LeavesDecayEvent event = new LeavesDecayEvent(block);
                Bukkit.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled())
                    return;

                block.breakNaturally();

                if (10 > new Random().nextInt(100))
                    block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, Material.LEAVES);
            }

            if (block.getType() == Material.LOG || block.getType() == Material.LOG_2)
            {
                for (ItemStack item : block.getDrops())
                    block.getWorld().dropItemNaturally(block.getLocation(), item);

                block.setType(Material.AIR);
            }

            for (int y = -1; y <= 1; y++)
            {
                for (int z = -1; z <= 1; z++)
                {
                    for (int x = -1; x <= 1; x++)
                    {
                        Block block1 = block.getRelative(x, y, z);

                        if (block != null)
                        {
                            if (block1.getType() == Material.LOG || block1.getType() == Material.LOG_2)
                            {
                                removeTree(block1, nearwood, range - ((z == 0 && x == 0 || nearwood) ? 0 : 1));
                            }
                            else if (block1.getType() == Material.LEAVES || block1.getType() == Material.LEAVES_2)
                            {
                                int finalZ = z;
                                int finalX = x;

                                this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () ->
                                {
                                    if (!this.isNearWood(block1, 2))
                                        this.plugin.getServer().getScheduler().runTask(this.plugin, () -> removeTree(block1, false, nearwood ? 4 : (range - ((finalZ == 0 && finalX == 0) ? 0 : 1))));
                                });
                            }
                        }
                    }
                }
            }
        });
    }

    public boolean isNearWood(Block block, int range)
    {
        if(range <= 0)
            return false;

        for(BlockFace face : this.faces)
        {
            Block block1 = block.getRelative(face);

            if(block1.getType() == Material.LOG || block1.getType() == Material.LOG_2)
                return true;
            else if((block1.getType() == Material.LEAVES || block1.getType() == Material.LEAVES_2) && this.isNearWood(block1, range-1))
                return true;
        }

        return false;
    }
}