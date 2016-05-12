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
 * PopeyeModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class PopeyeModule extends AbstractSurvivalModule
{
    private ItemStack spinash;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public PopeyeModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        this.spinash = new ItemStack(Material.INK_SACK, 1, (short)2);
        ItemMeta meta = this.spinash.getItemMeta();
        meta.setLore(Arrays.asList("Mangez les et gagnez de la force pour 10 secondes."));
        meta.setDisplayName(ChatColor.DARK_GREEN + "Epinards");
        this.spinash.setItemMeta(meta);
    }

    /**
     * Give spinash to all the players
     *
     * @param game Game
     */
    @Override
    public void onGameStart(SurvivalGame game)
    {
        for (GamePlayer player : (Collection<GamePlayer>) game.getInGamePlayers().values())
            player.getPlayerIfOnline().getInventory().addItem(spinash);
    }

    /**
     * Give strength on spinash use
     * @param event Event
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        if (event.getItem() != null && event.getItem().isSimilar(spinash))
        {
            event.getPlayer().addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(100, 1));
            event.getPlayer().setItemInHand(new ItemStack(Material.AIR));
            event.setCancelled(true);
        }
    }
}

