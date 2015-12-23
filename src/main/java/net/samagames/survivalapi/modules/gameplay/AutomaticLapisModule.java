package net.samagames.survivalapi.modules.gameplay;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class AutomaticLapisModule extends AbstractSurvivalModule
{
    public AutomaticLapisModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * Fill the lapis lazuli slot
     *
     * @param event Event
     */
    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event)
    {
        if (event.getInventory().getType() == InventoryType.ENCHANTING)
            event.getInventory().setItem(1, new ItemStack(Material.INK_SACK, 64, (short) 4));
    }

    /**
     * Remove the lapis lazuli (duplication catch)
     *
     * @param event Event
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event)
    {
        if (event.getInventory().getType() == InventoryType.ENCHANTING)
            event.getInventory().setItem(1, null);
    }

    /**
     * Cancel the click on the lapis lazuli (duplication catch)
     *
     * @param event Event
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        if (event.getInventory().getType() == InventoryType.ENCHANTING)
            if (event.getSlot() == 1)
                event.setCancelled(true);
    }

    /**
     * Fill the lapis lazuli slot
     *
     * @param event Event
     */
    @EventHandler
    public void enchantItemEvent(EnchantItemEvent event)
    {
        event.getInventory().setItem(1, new ItemStack(Material.INK_SACK, 64, (short) 4));
    }
}
