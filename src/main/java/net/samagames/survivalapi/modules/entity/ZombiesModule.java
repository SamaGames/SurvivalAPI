package net.samagames.survivalapi.modules.entity;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;

import java.util.Map;

/**
 * ZombiesModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class ZombiesModule extends AbstractSurvivalModule
{
    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public ZombiesModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * When a player is dead, spawn a zombie
     *
     * @param event Event
     */
    @EventHandler
    public void onEntityDeath(PlayerDeathEvent event)
    {
        Zombie zombie = (Zombie)event.getEntity().getWorld().spawnEntity(event.getEntity().getLocation(), EntityType.ZOMBIE);
        zombie.setCustomNameVisible(true);
        zombie.setCustomName(event.getEntity().getCustomName());

        event.getEntity().getActivePotionEffects().forEach(zombie::addPotionEffect);
    }
}
