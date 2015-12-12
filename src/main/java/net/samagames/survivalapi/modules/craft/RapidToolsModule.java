package net.samagames.survivalapi.modules.craft;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
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

    public static class ConfigurationBuilder
    {
        public enum ToolMaterial { WOOD, STONE, IRON, GOLD, DIAMOND }

        private ToolMaterial material;

        public ConfigurationBuilder()
        {
            this.material = ToolMaterial.STONE;
        }

        public HashMap<String, Object> build()
        {
            HashMap<String, Object> moduleConfiguration = new HashMap<>();

            moduleConfiguration.put("material", this.material);

            return moduleConfiguration;
        }

        public ConfigurationBuilder setToolsMaterial(ToolMaterial material)
        {
            this.material = material;
            return this;
        }
    }
}
