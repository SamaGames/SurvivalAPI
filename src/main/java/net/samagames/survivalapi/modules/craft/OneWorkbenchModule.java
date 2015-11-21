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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class OneWorkbenchModule extends AbstractSurvivalModule
{
    private final ArrayList<UUID> crafters;

    public OneWorkbenchModule(SurvivalPlugin plugin, SurvivalAPI api, HashMap<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);

        this.crafters = new ArrayList<>();
    }

    /**
     * Accept only one bench crafting
     *
     * @param event Event
     */
    @EventHandler(ignoreCancelled = true)
    public void onCraftItem(CraftItemEvent event)
    {
        this.onCraftItem(event.getRecipe(), event.getInventory(), event.getWhoClicked());

        if (!this.crafters.contains(event.getWhoClicked().getUniqueId()))
            this.crafters.add(event.getWhoClicked().getUniqueId());
    }

    /**
     * Accept only one bench crafting
     *
     * @param event Event
     */
    @EventHandler(ignoreCancelled = true)
    public void onPrepareItemCraft(PrepareItemCraftEvent event)
    {
        this.onCraftItem(event.getRecipe(), event.getInventory(), event.getView().getPlayer());
    }

    private void onCraftItem(Recipe recipe, CraftingInventory inventory, HumanEntity human)
    {
        if (recipe.getResult().getType() == Material.WORKBENCH)
            if (this.crafters.contains(human.getUniqueId()))
                inventory.setResult(new ItemStack(Material.AIR, 1));
    }
}
