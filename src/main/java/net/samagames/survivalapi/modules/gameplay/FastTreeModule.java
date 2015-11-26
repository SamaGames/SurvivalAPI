package net.samagames.survivalapi.modules.gameplay;

import net.minecraft.server.v1_8_R3.BlockLog1;
import net.minecraft.server.v1_8_R3.BlockWood;
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
import org.bukkit.material.Leaves;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

            removeTree(event.getBlock(), true, 6);
        }

        event.getPlayer().giveExp(event.getExpToDrop() * 2);
    }

    private void removeTree(Block block, boolean nearwood, int range)
    {
        if(range < 0 || block.hasMetadata("placed"))
            return;

        if (block.getType() == Material.LEAVES || block.getType() == Material.LEAVES_2) {
            LeavesDecayEvent event = new LeavesDecayEvent(block);
            Bukkit.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled())
                return;

            block.breakNaturally();

            if (10 > new Random().nextInt(100)) {
                block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, Material.LEAVES);
            }
        }

        if (block.getType() == Material.LOG || block.getType() == Material.LOG_2) {
            for (ItemStack item : block.getDrops())
                block.getWorld().dropItemNaturally(block.getLocation(), item);

            block.setType(Material.AIR);
        }

        for(int y = -1 ; y <= 1; y++)
        {
            for(int z = -1 ; z <= 1; z++)
            {
                for(int x = -1 ; x <= 1; x++)
                {
                    Block block1 = block.getRelative(x, y , z);
                    if (block != null) {
                        if (block1.getType() == Material.LOG || block1.getType() == Material.LOG_2)
                        {
                            removeTree(block1, nearwood, range - ((z == 0 && x == 0 || nearwood)?0:1));
                        }else if(block1.getType() == Material.LEAVES || block1.getType() == Material.LEAVES_2) {
                            final int finalZ = z;
                            final int finalX = x;
                            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                                if(!isNearWood(block1, 2))
                                {
                                    Bukkit.getScheduler().runTask(plugin, () -> removeTree(block1, false, (nearwood)?3:(range - ((finalZ == 0 && finalX == 0)?0:1))));
                                }
                            });
                        }
                    }
                }
            }
        }
    }

    public boolean isNearWood(Block block, int range)
    {
        for(int y = -range ; y <= range; y++)
        {
            for(int z = -range ; z <= range; z++)
            {
                for(int x = -range ; x <= range; x++)
                {
                    Block block1 = block.getRelative(x, y , z);
                    if(block1.getType() == Material.LOG || block1.getType() == Material.LOG_2)
                        return true;
                }
            }
        }

        return false;
    }
}
