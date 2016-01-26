package net.samagames.survivalapi.modules.gameplay;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * RemoveItemOnUseModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class RemoveItemOnUseModule extends AbstractSurvivalModule
{
    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public RemoveItemOnUseModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * Delete the ItemStack
     *
     * @param event Event
     */
    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event)
    {
        if(event.getItem() == null)
            return;

        ItemStack stack = event.getItem();

        if(stack.getType().equals(Material.MUSHROOM_SOUP))
            Bukkit.getScheduler().runTask(plugin, () -> event.getPlayer().getInventory().remove(Material.BOWL));
    }
}
