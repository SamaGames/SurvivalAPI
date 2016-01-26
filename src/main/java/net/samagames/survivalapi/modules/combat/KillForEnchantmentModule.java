package net.samagames.survivalapi.modules.combat;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.Map;

/**
 * KillForEnchantmentModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class KillForEnchantmentModule extends AbstractSurvivalModule
{
    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public KillForEnchantmentModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * Drop an enchantment table when a player die
     *
     * @param event Event
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        event.getDrops().add(new ItemStack(Material.ENCHANTMENT_TABLE, 1));
    }

    /**
     * Disable enchantment table craft
     *
     * @param event Event
     */
    @EventHandler
    public void onCraftItem(CraftItemEvent event)
    {
        onCraftItem(event.getRecipe(), event.getInventory());
    }

    /**
     * Disable enchantment table craft
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
        if (recipe.getResult().getType() == Material.ENCHANTMENT_TABLE)
            inventory.setResult(new ItemStack(Material.AIR));
    }
}
