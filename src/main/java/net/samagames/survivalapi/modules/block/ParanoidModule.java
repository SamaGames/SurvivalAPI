package net.samagames.survivalapi.modules.block;

import net.md_5.bungee.api.ChatColor;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Map;

/**
 * ParanoidModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class ParanoidModule extends AbstractSurvivalModule
{
    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public ParanoidModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * Write a message when a player mine a diamond
     *
     * @param event Event
     */
    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event)
    {
        if (event.getBlock().getType() != Material.DIAMOND_ORE)
            return;

        Location location = event.getBlock().getLocation();

        StringBuilder builder = new StringBuilder();
        builder.append(ChatColor.GOLD).append("[").append(ChatColor.YELLOW);
        builder.append("Paranoïa");
        builder.append(ChatColor.GOLD).append("]").append(ChatColor.YELLOW);
        builder.append(" Le joueur ").append(ChatColor.GOLD).append(event.getPlayer().getName()).append(ChatColor.YELLOW);
        builder.append(" a miné un bloc de diamant aux coordonnées ").append(ChatColor.GOLD);
        builder.append(location.getBlockX()).append("; ").append(location.getBlockY()).append("; ").append(location.getBlockZ());
        builder.append(ChatColor.YELLOW).append(" !");

        Bukkit.broadcastMessage(builder.toString());
    }
}
