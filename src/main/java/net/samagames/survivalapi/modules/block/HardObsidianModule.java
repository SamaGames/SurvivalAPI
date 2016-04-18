package net.samagames.survivalapi.modules.block;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * HardObsidianModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class HardObsidianModule extends AbstractSurvivalModule
{
    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public HardObsidianModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * Set obsidian only breakable by a diamond pickage
     *
     * @param event Event
     */
    @EventHandler
    public void onBlockDamage(BlockDamageEvent event)
    {
        if (event.getBlock().getType() == Material.OBSIDIAN && event.getItemInHand().getType() != Material.DIAMOND_PICKAXE)
            event.setCancelled(true);
    }

    /**
     * Damage the diamond pickage if an obsidian block is broken
     *
     * @param event Event
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        if (event.getBlock().getType() == Material.OBSIDIAN)
        {
            ItemStack item = event.getPlayer().getItemInHand();
            item.setDurability((short) (item.getDurability() + 500));
            if (item.getDurability() > item.getType().getMaxDurability())
            {
                event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                event.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR));
            }
            else
                event.getPlayer().getInventory().setItemInHand(item);
        }
    }
}
