package net.samagames.survivalapi.modules.combat;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;

public class OneShootPassiveModule extends AbstractSurvivalModule
{
    public OneShootPassiveModule(SurvivalPlugin plugin, SurvivalAPI api, HashMap<String, Object> moduleConfiguration)
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
        if (event.getDamager() instanceof Player)
            if (event.getEntity() instanceof Animals || event.getEntity() instanceof Ambient)
                ((LivingEntity) event.getEntity()).damage(Double.MAX_VALUE);
    }
}
