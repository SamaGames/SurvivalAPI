package net.samagames.survivalapi.modules.combat;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * KillSwitchModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class KillSwitchModule extends AbstractSurvivalModule
{
    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public KillSwitchModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * When a player is dead, swap inventories
     *
     * @param event Event
     */
    @EventHandler
    public void onEntityDeath(PlayerDeathEvent event)
    {
        if (event.getEntity().getKiller() != null)
        {
            ItemStack[] content = event.getEntity().getInventory().getContents();
            ItemStack[] armor = event.getEntity().getInventory().getArmorContents();

            event.getEntity().getKiller().getInventory().setContents(content);
            event.getEntity().getKiller().getInventory().setArmorContents(armor);

            event.getDrops().clear();

            event.getEntity().getKiller().sendMessage(ChatColor.GOLD + "Vous avez échangé votre inventaire avec celui de " + event.getEntity().getName() + ".");
        }
    }
}
