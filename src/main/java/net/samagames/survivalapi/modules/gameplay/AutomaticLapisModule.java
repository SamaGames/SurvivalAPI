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
public class AutomaticLapisModule extends AbstractSurvivalModule
{
    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
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
        if (event.getClickedInventory() != null && event.getClickedInventory().getType() == InventoryType.ENCHANTING && event.getSlot() == 1)
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
