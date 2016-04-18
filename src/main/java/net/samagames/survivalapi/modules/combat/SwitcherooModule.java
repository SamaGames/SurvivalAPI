package net.samagames.survivalapi.modules.combat;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Map;

/**
 * SwitchherooModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class SwitcherooModule extends AbstractSurvivalModule
{
    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public SwitcherooModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * Switch players on arrow hit
     *
     * @param event Event instance
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageByEntityEvent event)
    {
        if (!event.isCancelled()
                && event.getDamager() instanceof Arrow
                && event.getEntity() instanceof Player)
        {
            ProjectileSource source = ((Arrow)event.getDamager()).getShooter();
            if (source == null || !(source instanceof Player))
                return ;
            Player player = (Player)source;
            Location tmp = player.getLocation();
            player.teleport(event.getEntity());
            event.getEntity().teleport(tmp);
        }
    }
}
