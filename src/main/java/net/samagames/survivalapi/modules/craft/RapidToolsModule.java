package net.samagames.survivalapi.modules.craft;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.HashMap;

public class RapidToolsModule extends AbstractSurvivalModule
{
    public RapidToolsModule(SurvivalPlugin plugin, SurvivalAPI api, HashMap<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * Craft stone tools than wood
     *
     * @param event Event
     */
    @EventHandler
    public void onCraftItem(CraftItemEvent event)
    {
        this.onCraftItem(event.getRecipe(), event.getInventory());
    }

    /**
     * Craft stone tools than wood
     *
     * @param event Event
     */
    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event)
    {
        this.onCraftItem(event.getRecipe(), event.getInventory());
    }

    private void onCraftItem(Recipe recipe, CraftingInventory inventory)
    {
        if (recipe.getResult().getType() == Material.WOOD_SWORD)
            inventory.setResult(new ItemStack(Material.STONE_SWORD));
        else if (recipe.getResult().getType() == Material.WOOD_PICKAXE)
            inventory.setResult(new ItemStack(Material.STONE_PICKAXE));
        else if (recipe.getResult().getType() == Material.WOOD_AXE)
            inventory.setResult(new ItemStack(Material.STONE_AXE));
        else if (recipe.getResult().getType() == Material.WOOD_SPADE)
            inventory.setResult(new ItemStack(Material.STONE_SPADE));
    }
}
