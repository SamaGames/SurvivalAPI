package net.samagames.survivalapi.modules.combat;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;
import java.util.Random;

/**
 * OneShootPassiveModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class OneShootPassiveModule extends AbstractSurvivalModule
{
    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public OneShootPassiveModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * One shoot passive mobs
     *
     * @param event Event
     */
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
    {
        if ((event.getEntity() instanceof Animals || event.getEntity() instanceof Ambient || event.getEntity() instanceof Squid) && (event.getDamager() instanceof Player || event.getDamager() instanceof Projectile))
        {
            ((LivingEntity)event.getEntity()).damage(150.0D);
            event.getDamager().getWorld().spawn(event.getEntity().getLocation().add(1, 0, 0), ExperienceOrb.class).setExperience(1 + new Random().nextInt(5));
        }
    }
}
