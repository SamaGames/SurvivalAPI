package net.samagames.survivalapi.modules.gameplay;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CookieHeadModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class CookieHeadModule extends AbstractSurvivalModule
{
    private Map<String, List<PotionEffect>> effects;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public CookieHeadModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        this.effects = new HashMap<>();
    }

    /**
     * Drop player head on kill
     *
     * @param event Event
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);

        SkullMeta skullMeta = (SkullMeta)head.getItemMeta();
        skullMeta.setOwner(event.getEntity().getName());
        skullMeta.setDisplayName(ChatColor.AQUA + "Tête de " + event.getEntity().getName());

        head.setItemMeta(skullMeta);
        event.getDrops().add(head);

        List<PotionEffect> effectList = new ArrayList<>();
        effectList.addAll(event.getEntity().getActivePotionEffects());

        this.effects.put(event.getEntity().getName(), effectList);
    }

    /**
     * Give old player enchants on head eating
     *
     * @param event Event
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (event.getItem() == null || event.getItem().getType() != Material.SKULL_ITEM || event.getItem().getDurability() != 3
                || (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK))
            return;

        SkullMeta skullMeta = (SkullMeta)event.getItem().getItemMeta();
        List<PotionEffect> effectList = this.effects.get(skullMeta.getOwner());

        if (effectList != null)
        {
            effectList.forEach(event.getPlayer()::addPotionEffect);
            this.effects.remove(skullMeta.getOwner());
            event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.BURP, 1F, 1F);
        }
    }
}
