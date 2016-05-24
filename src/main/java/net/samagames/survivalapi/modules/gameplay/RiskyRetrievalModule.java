package net.samagames.survivalapi.modules.gameplay;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.survivalapi.modules.utility.DropTaggingModule;
import net.samagames.survivalapi.utils.Meta;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * RiskyRetrievalModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class RiskyRetrievalModule extends AbstractSurvivalModule
{
    private static final Material MATERIALS[] = new Material[]{
            Material.IRON_ORE, Material.IRON_INGOT,
            Material.GOLD_ORE, Material.GOLD_INGOT,
            Material.DIAMOND_ORE, Material.DIAMOND,
            Material.QUARTZ
    };
    private Inventory inventory;
    private Location chestLocation;
    private Random random;

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
        this.random = new Random();
    }

    @Override
    public void onGameStart(SurvivalGame game)
    {
        this.chestLocation = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
        this.chestLocation.setY(this.chestLocation.getWorld().getHighestBlockYAt(this.chestLocation));
        this.chestLocation.getBlock().setType(Material.ENDER_CHEST);
        this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, () -> this.chestLocation.getWorld().spawnParticle(Particle.PORTAL, this.chestLocation.clone().add(random.nextDouble() % 3D - 1.5D, random.nextDouble() % 3D - 1.5D, random.nextDouble() % 3D - 1.5D), 1), 4, 4);
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
        if (event.getClickedInventory() == null || !event.getClickedInventory().equals(this.inventory))
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
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onItemSpawn(ItemSpawnEvent event)
    {
        if (event.getEntityType() != EntityType.DROPPED_ITEM
                || Meta.hasMeta(event.getEntity().getItemStack())
                || event.getEntity().hasMetadata("playerDrop"))
            return ;

        for (int i = 0; i < MATERIALS.length; i++)
        {
            ItemStack stack = event.getEntity().getItemStack();
            if (MATERIALS[i] != stack.getType())
                continue ;
            ItemStack newStack = stack.clone();
            this.inventory.addItem(Meta.addMeta(newStack));
            event.getEntity().setItemStack(Meta.addMeta(event.getEntity().getItemStack()));
        }
    }

    /**
     * Disable block placing near chest
     *
     * @param event Block place event instance
     */
    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event)
    {
        if (event.getBlock().getLocation().getWorld().equals(this.chestLocation.getWorld())
                && event.getBlock().getLocation().distanceSquared(this.chestLocation) < 25)
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
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onBlockPlace(BlockBreakEvent event)
    {
        if (event.getBlock().getLocation().getWorld().equals(this.chestLocation.getWorld())
                && event.getBlock().getLocation().distanceSquared(this.chestLocation) < 25)
        {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "Il n'est pas possible de casser de blocs à proximité du coffre des minerais.");
        }
    }

    @Override
    public List<Class<? extends AbstractSurvivalModule>> getRequiredModules()
    {
        ArrayList<Class<? extends AbstractSurvivalModule>> requiredModules = new ArrayList<>();

        requiredModules.add(DropTaggingModule.class);

        return requiredModules;
    }
}
