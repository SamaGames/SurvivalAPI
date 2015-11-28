package net.samagames.survivalapi.modules.block;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;
import java.util.Random;

public class RandomChestModule extends AbstractSurvivalModule
{
    private final HashMap<ItemStack, Integer> items;

    public RandomChestModule(SurvivalPlugin plugin, SurvivalAPI api, HashMap<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        Validate.notNull(moduleConfiguration, "Configuration cannot be null!");

        this.items = (HashMap<ItemStack, Integer>) moduleConfiguration.get("items");
    }

    /**
     * Fill a legacy chest
     *
     * @param event Event
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getClickedBlock().getType().equals(Material.CHEST))
        {
            Chest chest = (Chest) event.getClickedBlock().getState();

            if (chest.hasMetadata("playerInteracted"))
                return;

            Inventory inventory = chest.getInventory();
            inventory.clear();

            int addedItems = 0;
            int slot = 0;

            for (ItemStack item : this.items.keySet())
            {
                if (addedItems > 20)
                    break;

                int frequency = this.items.get(item);

                Random random = new Random();
                if (random.nextInt(100) <= frequency)
                {
                    ItemStack stack = item.clone();
                    stack.setAmount((random.nextInt(3) + 1) * stack.getAmount());

                    while (inventory.getItem(slot) != null)
                        slot++;

                    inventory.setItem(slot, stack);
                    addedItems++;
                }

                slot++;

                if (slot > 26)
                    slot = 0;
            }

            chest.setMetadata("playerInteracted", new FixedMetadataValue(this.plugin, true));
        }
    }

    /**
     * Don't fill a chest placed by player
     *
     * @param event Event
     */
    @EventHandler
    public void onPlayerPlaceBlock(BlockPlaceEvent event)
    {
        if(event.getBlockPlaced().getType().equals(Material.CHEST))
        {
            event.getBlockPlaced().getState().setMetadata("playerInteracted", new FixedMetadataValue(this.plugin, true));
        }
    }

    public static class ConfigurationBuilder
    {
        private HashMap<ItemStack, Integer> items;

        public ConfigurationBuilder()
        {
            this.items = new HashMap<>();
        }

        public HashMap<String, Object> build()
        {
            HashMap<String, Object> moduleConfiguration = new HashMap<>();

            moduleConfiguration.put("items", this.items);

            return moduleConfiguration;
        }

        public ConfigurationBuilder addItemWithPercentage(ItemStack itemStack, int probability)
        {
            this.items.put(itemStack, probability);
            return this;
        }
    }
}
