package net.samagames.survivalapi.modules.entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.survivalapi.modules.IConfigurationBuilder;
import net.samagames.tools.ItemUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
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
public class EntityDropModule extends AbstractSurvivalModule
{
    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public EntityDropModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * Add custom drops to entity's ones
     *
     * @param event Event
     */
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event)
    {
        HashMap<EntityType, ItemStack[]> drops = (HashMap<EntityType, ItemStack[]>) this.moduleConfiguration.get("drops");

        if (drops.containsKey(event.getEntity().getType()))
            Collections.addAll(event.getDrops(), drops.get(event.getEntity().getType()));
    }

    public static class ConfigurationBuilder implements IConfigurationBuilder
    {
        private final Map<EntityType, ItemStack[]> drops;

        public ConfigurationBuilder()
        {
            this.drops = new HashMap<>();
        }

        @Override
        public Map<String, Object> build()
        {
            Map<String, Object> moduleConfiguration = new HashMap<>();

            moduleConfiguration.put("drops", this.drops);

            return moduleConfiguration;
        }

        @Override
        public Map<String, Object> buildFromJson(Map<String, JsonElement> configuration) throws Exception
        {
            if (configuration.containsKey("drops"))
            {
                JsonArray dropsJson = configuration.get("drops").getAsJsonArray();

                for (int i = 0; i < dropsJson.size(); i++)
                {
                    JsonObject dropJson = dropsJson.get(i).getAsJsonObject();

                    EntityType entityType = EntityType.valueOf(dropJson.get("entity").getAsString().toUpperCase());
                    JsonArray stacksJson = dropJson.get("stacks").getAsJsonArray();
                    ItemStack[] stacks = new ItemStack[stacksJson.size()];

                    for (int j = 0; j < stacksJson.size(); j++)
                        stacks[j] = ItemUtils.strToStack(stacksJson.get(i).getAsString());

                    this.addCustomDrops(entityType, stacks);
                }
            }

            return this.build();
        }

        public ConfigurationBuilder addCustomDrops(EntityType entity, ItemStack... stacks)
        {
            this.drops.put(entity, stacks);
            return this;
        }
    }
}
