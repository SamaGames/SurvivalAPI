package net.samagames.survivalapi.modules.entity;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

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
        event.getEntity().getWorld().spawnEntity(event.getEntity().getLocation(), EntityType.ZOMBIE);
    }
}
