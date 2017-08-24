package net.samagames.survivalapi.modules.block;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.survivalapi.modules.IConfigurationBuilder;
import net.samagames.survivalapi.utils.Meta;
import net.samagames.tools.ItemUtils;
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
import java.util.HashMap;
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
public class RandomChestModule extends AbstractSurvivalModule
{
    private final Map<ItemStack, Integer> items;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public RandomChestModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        Validate.notNull(moduleConfiguration, "Configuration cannot be null!");

        this.items = (Map<ItemStack, Integer>) moduleConfiguration.get("items");
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
            int slot;

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

                        if (slot >= inventory.getSize())
                            slot = 0;
                    }

                    inventory.setItem(slot, this.addMetaIfNeeded(stack));
                    addedItems++;
                }
            }

            chest.setMetadata("playerInteracted", new FixedMetadataValue(this.plugin, true));
        }
    }

    private ItemStack addMetaIfNeeded(ItemStack stack)
    {
        Material material = stack.getType();

        if (material == Material.COAL || material == Material.IRON_INGOT || material == Material.GOLD_INGOT || material == Material.DIAMOND || material == Material.EMERALD || material == Material.QUARTZ)
            return Meta.addMeta(stack);
        else
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

    public static class ConfigurationBuilder implements IConfigurationBuilder
    {
        private final Map<ItemStack, Integer> items;

        public ConfigurationBuilder()
        {
            this.items = new HashMap<>();
        }

        @Override
        public Map<String, Object> build()
        {
            Map<String, Object> moduleConfiguration = new HashMap<>();

            moduleConfiguration.put("items", this.items);

            return moduleConfiguration;
        }

        @Override
        public Map<String, Object> buildFromJson(Map<String, JsonElement> configuration) throws Exception
        {
            if (configuration.containsKey("items"))
            {
                JsonArray itemsJson = configuration.get("items").getAsJsonArray();

                for (int i = 0; i < itemsJson.size(); i++)
                {
                    JsonObject itemJson = itemsJson.get(i).getAsJsonObject();
                    this.addItemWithPercentage(ItemUtils.strToStack(itemJson.get("stack").getAsString()), itemJson.get("probability").getAsInt());
                }
            }

            return this.build();
        }

        public ConfigurationBuilder addItemWithPercentage(ItemStack itemStack, int probability)
        {
            this.items.put(itemStack, probability);
            return this;
        }
    }
}
