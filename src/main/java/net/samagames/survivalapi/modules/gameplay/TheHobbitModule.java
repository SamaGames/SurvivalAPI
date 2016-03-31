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
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * TheHobbitModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class TheHobbitModule extends AbstractSurvivalModule
{
    private ItemStack ring;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public TheHobbitModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        this.ring = new ItemStack(Material.GOLD_NUGGET);
        ItemMeta meta = this.ring.getItemMeta();
        meta.setLore(Arrays.asList("Une fois utilisé, cet anneau vous", "permettra d'être invisible pendant", "30 secondes."));
        meta.setDisplayName(ChatColor.GOLD + "Anneau");
        this.ring.setItemMeta(meta);
    }

    /**
     * Give 1 ring to all the players
     *
     * @param game Game
     */
    @Override
    public void onGameStart(SurvivalGame game)
    {
        for (GamePlayer player : (Collection<GamePlayer>) game.getInGamePlayers().values())
            player.getPlayerIfOnline().getInventory().addItem(ring);
    }

    /**
     * Give invisibility on ring use
     * @param event Event
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        if (event.getItem() != null && event.getItem().isSimilar(ring))
        {
            event.getPlayer().addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(600, 1));
            event.getPlayer().setItemInHand(new ItemStack(Material.AIR));
            event.setCancelled(true);
        }
    }
}

