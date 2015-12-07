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

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RandomChestModule extends AbstractSurvivalModule
{
    private final HashMap<ItemStack, Integer> items;

    private RapidOresModule rapidOresModule;

    public RandomChestModule(SurvivalPlugin plugin, SurvivalAPI api, HashMap<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        Validate.notNull(moduleConfiguration, "Configuration cannot be null!");

        this.items = (HashMap<ItemStack, Integer>) moduleConfiguration.get("items");
        this.rapidOresModule = SurvivalAPI.get().getModule(RapidOresModule.class);
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

                int frequency = 0;

                for(Map.Entry<ItemStack, Integer> entry : this.items.entrySet())
                    if(entry.getKey().equals(item))
                        frequency = entry.getValue();

                SecureRandom random = new SecureRandom();

                if (random.nextInt(1000) <= frequency * 10)
                {
                    ItemStack stack = item.clone();
                    stack.setAmount((random.nextInt(3) + 1) * stack.getAmount());

                    slot = random.nextInt(inventory.getSize());

                    while (inventory.getItem(slot) != null)
                    {
                        slot++;

                        if (slot > inventory.getSize())
                            slot = 0;
                    }

                    inventory.setItem(slot, verifyStack(stack));
                    addedItems++;
                }
            }

            chest.setMetadata("playerInteracted", new FixedMetadataValue(this.plugin, true));
        }
    }

    public ItemStack verifyStack(ItemStack stack)
    {
        if(this.rapidOresModule != null)
            if (this.rapidOresModule.isDoubledType(stack.getType()))
                return this.rapidOresModule.addMeta(stack);

        return stack;
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
            event.getBlockPlaced().getState().setMetadata("playerInteracted", new FixedMetadataValue(this.plugin, true));
    }

    @Override
    public ArrayList<Class<? extends AbstractSurvivalModule>> getRequiredModules()
    {
        ArrayList<Class<? extends AbstractSurvivalModule>> requiredModules = new ArrayList<>();

        requiredModules.add(RapidOresModule.class);

        return requiredModules;
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
