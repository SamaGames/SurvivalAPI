package net.samagames.survivalapi.modules.craft;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class WaterMovingBootsModule extends AbstractSurvivalModule
{
    public WaterMovingBootsModule(SurvivalPlugin plugin, SurvivalAPI api, HashMap<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * Added automatically Depth Strider
     * @param event
     */
    @EventHandler
    public void onCraftItem(CraftItemEvent event)
    {
        if (
                event.getRecipe().getResult().getType() == Material.LEATHER_BOOTS ||
                event.getRecipe().getResult().getType() == Material.IRON_BOOTS ||
                event.getRecipe().getResult().getType() == Material.CHAINMAIL_BOOTS ||
                event.getRecipe().getResult().getType() == Material.GOLD_BOOTS ||
                event.getRecipe().getResult().getType() == Material.DIAMOND_BOOTS
        )
        {
            ItemStack newStack = event.getRecipe().getResult();
            newStack.addEnchantment(Enchantment.DEPTH_STRIDER, 1);

            event.getInventory().setResult(newStack);
        }
    }
}
