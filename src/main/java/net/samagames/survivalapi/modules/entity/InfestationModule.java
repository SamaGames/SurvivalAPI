package net.samagames.survivalapi.modules.entity;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Map;
import java.util.Random;

public class InfestationModule extends AbstractSurvivalModule
{
    private final Random random;

    public InfestationModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);

        this.random = new Random();
    }

    /**
     * When a player kill a mob there's a 40% chance to spawn the same at the same place
     *
     * @param event Event
     */
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event)
    {
        if (event.getEntity().getType() == EntityType.PLAYER)
            return;

        if (this.random.nextInt(100) < 40)
            event.getEntity().getWorld().spawnEntity(event.getEntity().getLocation(), event.getEntity().getType());
    }
}
