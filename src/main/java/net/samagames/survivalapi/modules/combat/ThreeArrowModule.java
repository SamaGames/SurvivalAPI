package net.samagames.survivalapi.modules.combat;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import java.util.HashMap;

public class ThreeArrowModule extends AbstractSurvivalModule
{
    public ThreeArrowModule(SurvivalPlugin plugin, SurvivalAPI api, HashMap<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * Launch 2 more arrows when one is launched
     *
     * @param event Event
     */
    @EventHandler(ignoreCancelled = true)
    public void onProjectileLaunch(ProjectileLaunchEvent event)
    {
        if (event.getEntity().getType() != EntityType.ARROW || !(event.getEntity().getShooter() instanceof Player))
            return;

        for(int i = 0; i < 2; i++)
        {
            Bukkit.getScheduler().runTaskLater(this.plugin, () ->
                    event.getEntity().getShooter().launchProjectile(Arrow.class, event.getEntity().getVelocity()), 5L * (i + 1));
        }
    }
}
