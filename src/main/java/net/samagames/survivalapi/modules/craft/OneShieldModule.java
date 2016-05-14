package net.samagames.survivalapi.modules.craft;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * OneShieldModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class OneShieldModule extends AbstractSurvivalModule
{
    private final List<UUID> crafters;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public OneShieldModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);

        this.crafters = new ArrayList<>();
    }

    /**
     * Accept only one shield crafting per 5 minutes
     *
     * @param event Event
     */
    @EventHandler
    public void onCraftItem(CraftItemEvent event)
    {
        this.onCraftItem(event.getRecipe(), event.getInventory(), event.getWhoClicked());

        if (event.getRecipe().getResult().getType() == Material.SHIELD
                && !this.crafters.contains(event.getWhoClicked().getUniqueId()))
        {
            this.crafters.add(event.getWhoClicked().getUniqueId());
            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> this.crafters.remove(event.getWhoClicked().getUniqueId()), 6000);
        }
    }

    private void onCraftItem(Recipe recipe, CraftingInventory inventory, HumanEntity human)
    {
        if (recipe.getResult().getType() == Material.SHIELD && this.crafters.contains(human.getUniqueId()))
        {
            inventory.setResult(new ItemStack(Material.AIR, 1));
            human.sendMessage(ChatColor.RED + "Vous ne pouvez créer de bouclier que toutes les 5 minutes !");
        }
    }
}
