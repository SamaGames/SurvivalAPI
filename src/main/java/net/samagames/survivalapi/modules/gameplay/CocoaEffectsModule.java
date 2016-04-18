package net.samagames.survivalapi.modules.gameplay;

import net.samagames.api.games.GamePlayer;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
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
 * CocoaEffectsModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class CocoaEffectsModule extends AbstractSurvivalModule
{
    private ItemStack cocoa;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public CocoaEffectsModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        this.cocoa = new ItemStack(Material.INK_SACK, 5, (short)3);
        ItemMeta meta = this.cocoa.getItemMeta();
        meta.setLore(Arrays.asList("Chaque utilisation vous donne 30 secondes", " de force et vitesse améliorées", " mais les effets contraires par la suite", " pendant 30 secondes."));
        meta.setDisplayName("Coco");
        this.cocoa.setItemMeta(meta);
    }

    /**
     * Give 5 coca beans to all the players
     *
     * @param game Game
     */
    @Override
    public void onGameStart(SurvivalGame game)
    {
        for (GamePlayer player : (Collection<GamePlayer>) game.getInGamePlayers().values())
            player.getPlayerIfOnline().getInventory().addItem(cocoa);
    }

    /**
     * Give speed 1 & strenght 1 effect to all players, then give them slowness & weakness after 30sec.
     * @param event Event
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        if (event.getItem() != null && event.getItem().isSimilar(cocoa))
        {
            event.getPlayer().addPotionEffect(PotionEffectType.SPEED.createEffect(600, 1));
            event.getPlayer().addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(600, 1));
            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> {
                event.getPlayer().addPotionEffect(PotionEffectType.SLOW.createEffect(600, 1));
                event.getPlayer().addPotionEffect(PotionEffectType.WEAKNESS.createEffect(600, 1));
            }, 600);
            if (event.getItem().getAmount() > 1)
                event.getItem().setAmount(event.getItem().getAmount() - 1);
            else
                event.getPlayer().setItemInHand(new ItemStack(Material.AIR));
            event.setCancelled(true);
        }
    }
}

