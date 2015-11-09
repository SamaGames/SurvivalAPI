package net.samagames.survivalapi.modules.craft;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class DisableNotchAppleModule extends AbstractSurvivalModule
{
    public DisableNotchAppleModule(SurvivalPlugin plugin, SurvivalAPI api, HashMap<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * Disable Notch's apple
     *
     * @param event Event
     */
    @EventHandler
    public void onCraftItem(CraftItemEvent event)
    {
        if (event.getRecipe().getResult().getType() == Material.GOLDEN_APPLE && event.getRecipe().getResult().getDurability() == 1)
            event.getInventory().setResult(new ItemStack(Material.AIR));
    }
}
