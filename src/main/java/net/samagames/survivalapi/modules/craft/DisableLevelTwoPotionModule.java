package net.samagames.survivalapi.modules.craft;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.BrewEvent;

import java.util.Map;

public class DisableLevelTwoPotionModule extends AbstractSurvivalModule
{
    public DisableLevelTwoPotionModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * Disabling level II potions
     *
     * @param event Event
     */
    @EventHandler
    public void onBrew(BrewEvent event)
    {
        if(event.getContents().getIngredient().getType() == Material.GLOWSTONE_DUST)
            event.setCancelled(true);
    }
}
