package net.samagames.survivalapi.modules.craft;

import net.md_5.bungee.api.ChatColor;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.CraftItemEvent;

import java.util.Map;

/**
 * InventorsModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class InventorsModule extends AbstractSurvivalModule
{
    private boolean first;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public InventorsModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        this.first = true;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCraft(CraftItemEvent event)
    {
        if (!first || event.isCancelled())
            return ;

        first = false;
        StringBuilder builder = new StringBuilder();
        builder.append(ChatColor.GOLD).append("[").append(ChatColor.YELLOW);
        builder.append("Inventors");
        builder.append(ChatColor.GOLD).append("]").append(ChatColor.YELLOW);
        builder.append(" Le joueur ").append(ChatColor.GOLD).append(event.getWhoClicked().getName()).append(ChatColor.YELLOW);
        builder.append(" a crafté : ").append(ChatColor.GOLD);
        builder.append(event.getRecipe().getResult().getType());
        builder.append(ChatColor.YELLOW).append(" !");

        Bukkit.broadcastMessage(builder.toString());
    }
}
