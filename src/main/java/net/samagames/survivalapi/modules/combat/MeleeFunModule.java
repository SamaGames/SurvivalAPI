package net.samagames.survivalapi.modules.gameplay;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;

/**
 * MeleeFunModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class MeleeFunModule extends AbstractSurvivalModule
{
    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public MeleeFunModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * Set NoDamageTicks to 0 on player -> player damage
     *
     * @param event Event
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageByEntityEvent event)
    {
        if (!(event.getEntity() instanceof Player) || (!(event.getDamager() instanceof Player)) && !(event.getDamager() instanceof Projectile))
            return ;
        ((Player) event.getEntity()).setNoDamageTicks(0);
        this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> ((Player)event.getEntity()).setNoDamageTicks(0), 1);
    }
}
