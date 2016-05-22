package net.samagames.survivalapi.modules.gameplay;

import net.samagames.api.games.GamePlayer;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Map;

/**
 * NineSlotsModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class NineSlotsModule extends AbstractSurvivalModule
{
    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public NineSlotsModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * Fill the players inventories with barriers
     *
     * @param game Game
     */
    @Override
    public void onGameStart(SurvivalGame game)
    {
        for (GamePlayer player : (Collection<GamePlayer>) game.getInGamePlayers().values())
        {
            Player bukkitPlayer = player.getPlayerIfOnline();
            for (int i = 9; i < 36; i++)
                bukkitPlayer.getInventory().setItem(i, new ItemStack(Material.BARRIER, 1));
        }
    }

    /**
     * Disable barrier taking in the players inventories
     *
     * @param event Event
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        if (event.getClickedInventory() != null && event.getClickedInventory().getType() == InventoryType.PLAYER && event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.BARRIER)
            event.setCancelled(true);
    }

    /**
     * Remove barrier on drop
     *
     * @param event Event
     */
    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event)
    {
        if (event.getEntityType() != EntityType.DROPPED_ITEM)
            return;

        if (event.getEntity().getItemStack().getType() == Material.BARRIER)
            event.setCancelled(true);
    }
}
