package net.samagames.survivalapi.modules.gameplay;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * Created by Silva on 28/11/2015.
 */
public class RemoveItemOnUse extends AbstractSurvivalModule
{

    public RemoveItemOnUse(SurvivalPlugin plugin, SurvivalAPI api, HashMap<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    @EventHandler
    public void onPlayerEat(PlayerItemConsumeEvent event)
    {
        if(event.getItem() == null)
            return;

        ItemStack stack = event.getItem();
        if(stack.getType().equals(Material.MUSHROOM_SOUP))
        {
            event.getPlayer().setItemInHand(new ItemStack(Material.AIR));
        }
    }
}
