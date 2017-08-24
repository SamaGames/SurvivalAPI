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
import java.util.List;
import java.util.Map;
import java.util.UUID;

/*
 * This file is part of SurvivalAPI.
 *
 * SurvivalAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SurvivalAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SurvivalAPI.  If not, see <http://www.gnu.org/licenses/>.
 */
public class OneWorkbenchModule extends AbstractSurvivalModule
{
    private final List<UUID> crafters;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public OneWorkbenchModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);

        this.crafters = new ArrayList<>();
    }

    /**
     * Accept only one bench crafting
     *
     * @param event Event
     */
    @EventHandler
    public void onCraftItem(CraftItemEvent event)
    {
        this.onCraftItem(event.getRecipe(), event.getInventory(), event.getWhoClicked());

        if (event.getRecipe().getResult().getType() == Material.WORKBENCH && !this.crafters.contains(event.getWhoClicked().getUniqueId()))
            this.crafters.add(event.getWhoClicked().getUniqueId());
    }

    /**
     * Accept only one bench crafting
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
        if (recipe.getResult().getType() == Material.WORKBENCH && this.crafters.contains(human.getUniqueId()))
            inventory.setResult(new ItemStack(Material.AIR, 1));
    }
}
