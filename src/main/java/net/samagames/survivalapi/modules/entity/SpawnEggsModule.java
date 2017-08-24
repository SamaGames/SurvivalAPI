package net.samagames.survivalapi.modules.entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.survivalapi.modules.IConfigurationBuilder;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.*;

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
public class SpawnEggsModule extends AbstractSurvivalModule
{
    private final List<EntityType> entities;
    private final Random random;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public SpawnEggsModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        Validate.notNull(moduleConfiguration, "Configuration cannot be null!");

        this.entities = (List<EntityType>) moduleConfiguration.get("entities");
        this.random = new Random();
    }

    /**
     * Spawn mob on block hit
     *
     * @param event Event
     */
    @EventHandler
    public void onEntityLand(ProjectileHitEvent event)
    {
        this.onHit(event.getEntity());
    }

    /**
     * Spawn mob on mob hit
     *
     * @param event Event
     */
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event)
    {
        this.onHit(event.getDamager());
    }

    /**
     * Spawn random mob
     *
     * @param entity Source entity
     */
    private void onHit(Entity entity)
    {
        if (entity == null || !(entity instanceof Egg))
            return;

        entity.getWorld().spawnEntity(entity.getLocation(), this.entities.get(this.random.nextInt(this.entities.size())));
    }

    public static class ConfigurationBuilder implements IConfigurationBuilder
    {
        private List<EntityType> entities;

        public ConfigurationBuilder()
        {
            this.entities = new ArrayList<>();
        }

        @Override
        public Map<String, Object> build()
        {
            Map<String, Object> moduleConfiguration = new HashMap<>();

            moduleConfiguration.put("entities", this.entities);

            return moduleConfiguration;
        }

        @Override
        public Map<String, Object> buildFromJson(Map<String, JsonElement> configuration) throws Exception
        {
            if (configuration.containsKey("entities"))
            {
                JsonArray entityTypesJson = configuration.get("entities").getAsJsonArray();
                entityTypesJson.forEach(element -> this.addEntityType(EntityType.valueOf(element.getAsString().toUpperCase())));
            }

            return this.build();
        }

        public SpawnEggsModule.ConfigurationBuilder addDefaults()
        {
            Arrays.asList(new EntityType[] {
                    EntityType.CAVE_SPIDER, EntityType.COW, EntityType.CREEPER, EntityType.ENDERMAN, EntityType.ENDERMITE,
                    EntityType.IRON_GOLEM, EntityType.MAGMA_CUBE, EntityType.OCELOT, EntityType.PIG,
                    EntityType.PIG_ZOMBIE, EntityType.RABBIT, EntityType.SHEEP, EntityType.SILVERFISH, EntityType.SKELETON,
                    EntityType.SLIME, EntityType.SNOWMAN, EntityType.SPIDER, EntityType.SQUID, EntityType.VILLAGER,
                    EntityType.WOLF, EntityType.ZOMBIE, EntityType.HORSE, EntityType.MUSHROOM_COW,
                    EntityType.WITHER_SKULL, EntityType.WITHER
            }).forEach(this::addEntityType);

            return this;
        }

        public SpawnEggsModule.ConfigurationBuilder addEntityType(EntityType entityType)
        {
            this.entities.add(entityType);
            return this;
        }
    }
}
