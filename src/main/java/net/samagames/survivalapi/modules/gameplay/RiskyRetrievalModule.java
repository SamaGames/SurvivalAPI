package net.samagames.survivalapi.modules.gameplay;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.survivalapi.utils.Meta;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * RiskyRetrievalModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class RiskyRetrievalModule extends AbstractSurvivalModule
{
    private Inventory inventory;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public RiskyRetrievalModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        this.inventory = plugin.getServer().createInventory(null, 54, "Minage");
    }

    @Override
    public void onGameStart(SurvivalGame game)
    {
        Location location = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
        location.setY(location.getWorld().getHighestBlockYAt(location));
        location.getBlock().setType(Material.ENDER_CHEST);
    }

    /**
     * Open public inventory
     *
     * @param event Interact Event instance
     */
    @EventHandler(ignoreCancelled = true)
    public void onChestOpen(PlayerInteractEvent event)
    {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK
                && event.getClickedBlock() != null
                && event.getClickedBlock().getType() == Material.ENDER_CHEST
                && event.getClickedBlock().getLocation().getBlockX() == 0
                && event.getClickedBlock().getLocation().getBlockZ() == 0)
        {
            event.setCancelled(true);
            event.getPlayer().openInventory(this.inventory);
        }
    }

    /**
     * Disable putting items in inventory
     *
     * @param event Click event instance
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        if (!event.getClickedInventory().equals(this.inventory))
            return ;
        switch (event.getAction())
        {
            case PICKUP_ALL:
            case PICKUP_HALF:
            case PICKUP_ONE:
            case PICKUP_SOME:
                event.setCancelled(false);
                break ;
            default:
                event.setCancelled(true);
        }
    }

    /**
     * Add ore to chest on drop
     *
     * @param event Item spawn event instance
     */
    @EventHandler(ignoreCancelled = true)
    public void onItemSpawn(ItemSpawnEvent event)
    {
        if (event.getEntityType() != EntityType.DROPPED_ITEM)
            return;

        if(Meta.hasMeta(event.getEntity().getItemStack()))
            return;

        ItemStack stack = event.getEntity().getItemStack();
        ItemStack newStack = stack.clone();
        this.inventory.addItem(Meta.addMeta(newStack));
    }

    /**
     * Disable block placing near chest
     *
     * @param event Block place event instance
     */
    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event)
    {
        if (event.getBlockPlaced().getLocation().distanceSquared(new Location(event.getBlockPlaced().getWorld(), 0, 0, 0)) < 25)
        {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "Il n'est pas possible de poser de blocs à proximité du coffre des minerais.");
        }
    }

    /**
     * Disable block breaking near chest
     *
     * @param event Block break event instance
     */
    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockBreakEvent event)
    {
        if (event.getBlock().getLocation().distanceSquared(new Location(event.getBlock().getWorld(), 0, 0, 0)) < 25)
        {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "Il n'est pas possible de casser de blocs à proximité du coffre des minerais.");
        }
    }
}
