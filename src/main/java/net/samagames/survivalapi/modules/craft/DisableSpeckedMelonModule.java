package net.samagames.survivalapi.modules.craft;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.Material;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class DisableSpeckedMelonModule extends AbstractSurvivalModule
{
    public DisableSpeckedMelonModule(SurvivalPlugin plugin, SurvivalAPI api, HashMap<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * Disable specked melon
     *
     * @param event Event
     */
    public void onCraftItem(CraftItemEvent event)
    {
        if(event.getRecipe().getResult().getType() == Material.SPECKLED_MELON)
            if(event.getInventory().contains(Material.GOLD_NUGGET))
                event.getInventory().setResult(new ItemStack(Material.AIR));
    }
}
