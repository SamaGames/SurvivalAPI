package net.samagames.survivalapi.modules.block;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Map;

/**
 * LightsOutModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class LightsOutModule extends AbstractSurvivalModule
{
    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public LightsOutModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * Disable placing torchs
     * @param event The event
     */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        if (event.getBlockPlaced().getType() == Material.TORCH
                || event.getBlockPlaced().getType() == Material.REDSTONE_TORCH_ON
                || event.getBlockPlaced().getType() == Material.REDSTONE_TORCH_OFF)
        {
            event.getPlayer().sendMessage(ChatColor.RED + "Les torches sont désactivées dans cette partie.");
            event.setCancelled(true);
        }
    }
}
