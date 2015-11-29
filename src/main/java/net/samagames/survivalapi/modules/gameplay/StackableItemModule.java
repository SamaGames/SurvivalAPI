package net.samagames.survivalapi.modules.gameplay;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * Created by Silva on 29/11/2015.
 */
public class StackableItemModule extends AbstractSurvivalModule
{

    public StackableItemModule(SurvivalPlugin plugin, SurvivalAPI api, HashMap<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);

    }

    @EventHandler
    public void onInventory(InventoryClickEvent event)
    {
        if(event.getCursor() != null && event.getCurrentItem() != null)
        {
            ItemStack cursor = event.getCursor();
            ItemStack clicked = event.getCurrentItem();
            if(clicked.isSimilar(cursor) && clicked.getAmount() + cursor.getAmount() <= clicked.getMaxStackSize() )
            {
                event.setResult(Event.Result.ALLOW);
            }
        }
    }
}
