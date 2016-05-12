package net.samagames.survivalapi.modules.gameplay;

import net.samagames.api.games.GamePlayer;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * OneHealModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class OneHealModule extends AbstractSurvivalModule
{
    private ItemStack hoe;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public OneHealModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        this.hoe = new ItemStack(Material.GOLD_HOE);
        ItemMeta meta = this.hoe.getItemMeta();
        meta.setLore(Arrays.asList("Utilisez là pour vous regen toute votre vie.", "Attention, elle est à usage unique."));
        meta.setDisplayName(ChatColor.GOLD + "Heal");
        this.hoe.setItemMeta(meta);
    }

    /**
     * Give 1 hoe to all the players
     *
     * @param game Game
     */
    @Override
    public void onGameStart(SurvivalGame game)
    {
        for (GamePlayer player : (Collection<GamePlayer>) game.getInGamePlayers().values())
            player.getPlayerIfOnline().getInventory().addItem(hoe);
    }

    /**
     * Regen life on use
     * @param event Event
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        if (event.getItem() != null && event.getItem().isSimilar(hoe))
        {
            event.getPlayer().setHealth(event.getPlayer().getMaxHealth());
            event.getPlayer().setItemInHand(new ItemStack(Material.AIR));
            event.setCancelled(true);
        }
    }
}

