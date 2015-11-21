package net.samagames.survivalapi.modules.craft;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

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
        this.onCraftItem(event.getRecipe(), event.getInventory(), event.getWhoClicked());
    }

    /**
     * Disable Notch's apple
     *
     * @param event Event
     */
    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event)
    {
        this.onCraftItem(event.getRecipe(), event.getInventory(), event.getView().getPlayer());
    }

    private void onCraftItem(Recipe recipe, CraftingInventory inventory, HumanEntity human)
    {
        if (recipe.getResult().getType() == Material.GOLDEN_APPLE && recipe.getResult().getDurability() == 1)
            inventory.setResult(new ItemStack(Material.AIR));
    }
}
