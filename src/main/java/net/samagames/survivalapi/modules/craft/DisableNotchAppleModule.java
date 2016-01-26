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

import java.util.Map;

/**
 * DisableNotchAppleModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class DisableNotchAppleModule extends AbstractSurvivalModule
{
    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public DisableNotchAppleModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
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
        onCraftItem(event.getRecipe(), event.getInventory());
    }

    /**
     * Disable Notch's apple
     *
     * @param event Event
     */
    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event)
    {
        onCraftItem(event.getRecipe(), event.getInventory());
    }

    private static void onCraftItem(Recipe recipe, CraftingInventory inventory)
    {
        if (recipe.getResult().getType() == Material.GOLDEN_APPLE && recipe.getResult().getDurability() == 1)
            inventory.setResult(new ItemStack(Material.AIR));
    }
}
