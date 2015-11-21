package net.samagames.survivalapi.modules.gameplay;

import net.samagames.api.games.GamePlayer;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;

public class NineSlotsModule extends AbstractSurvivalModule
{
    public NineSlotsModule(SurvivalPlugin plugin, SurvivalAPI api, HashMap<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * Fill the players inventories with barriers
     *
     * @param game Game
     */
    @Override
    public void onGameStart(SurvivalGame game)
    {
        for (GamePlayer player : (Collection<GamePlayer>) game.getInGamePlayers())
            for (int i = 9; i < player.getPlayerIfOnline().getInventory().getSize(); i++)
                player.getPlayerIfOnline().getInventory().setItem(i, new ItemStack(Material.BARRIER, 1));
    }

    /**
     * Disable barrier taking in the players inventories
     *
     * @param event Event
     */
    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event)
    {
        if (event.getClickedInventory().getType() == InventoryType.PLAYER)
            if (event.getCurrentItem().getType() == Material.BARRIER)
                event.setCancelled(true);
    }
}
