package net.samagames.survivalapi.modules.craft;

import com.google.gson.JsonElement;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.survivalapi.modules.IConfigurationBuilder;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.HashMap;
import java.util.Map;

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
public class RapidToolsModule extends AbstractSurvivalModule
{
    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public RapidToolsModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        Validate.notNull(moduleConfiguration, "Configuration cannot be null!");
    }

    /**
     * Craft stone tools than wood and add durability enchantment
     *
     * @param event Event
     */
    @EventHandler
    public void onCraftItem(CraftItemEvent event)
    {
        this.onCraftItem(event.getRecipe(), event.getInventory());
    }

    /**
     * Craft stone tools than wood add durability enchantment
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
            inventory.setResult(new ItemStack(this.getMaterial(Material.WOOD_SWORD)));
        else if (recipe.getResult().getType() == Material.WOOD_PICKAXE)
            inventory.setResult(new ItemStack(this.getMaterial(Material.WOOD_PICKAXE)));
        else if (recipe.getResult().getType() == Material.WOOD_AXE)
            inventory.setResult(new ItemStack(this.getMaterial(Material.WOOD_AXE)));
        else if (recipe.getResult().getType() == Material.WOOD_SPADE)
            inventory.setResult(new ItemStack(this.getMaterial(Material.WOOD_SPADE)));
    }

    public Material getMaterial(Material material)
    {
        return Material.valueOf(((ConfigurationBuilder.ToolMaterial) this.moduleConfiguration.get("material")).name().toUpperCase() + "_" + material.name().split("_")[1]);
    }

    public static class ConfigurationBuilder implements IConfigurationBuilder
    {
        public enum ToolMaterial { WOOD, STONE, IRON, GOLD, DIAMOND }

        private ToolMaterial material;

        public ConfigurationBuilder()
        {
            this.material = ToolMaterial.STONE;
        }

        @Override
        public Map<String, Object> build()
        {
            Map<String, Object> moduleConfiguration = new HashMap<>();

            moduleConfiguration.put("material", this.material);

            return moduleConfiguration;
        }

        @Override
        public Map<String, Object> buildFromJson(Map<String, JsonElement> configuration) throws Exception
        {
            if (configuration.containsKey("material"))
                this.setToolsMaterial(ToolMaterial.valueOf(configuration.get("material").getAsString().toUpperCase()));

            return this.build();
        }

        public ConfigurationBuilder setToolsMaterial(ToolMaterial material)
        {
            this.material = material;
            return this;
        }
    }
}
