package net.samagames.survivalapi.modules.entity;

import com.google.gson.JsonElement;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.survivalapi.modules.IConfigurationBuilder;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
public class MobOresModule extends AbstractSurvivalModule
{
    private Random random;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public MobOresModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        Validate.notNull(moduleConfiguration, "Configuration cannot be null!");

        this.random = new Random();
    }

    /**
     * When a player breaks ore, spawn mob
     *
     * @param event Event
     */
    @EventHandler(ignoreCancelled = true)
    public void onEntityDeath(BlockBreakEvent event)
    {
        EntityType type;

        switch (event.getBlock().getType())
        {
            case COAL_ORE:
                type = EntityType.ZOMBIE;
                break;

            case IRON_ORE:
                type = EntityType.SKELETON;
                break;

            case GOLD_ORE:
                type = EntityType.SPIDER;
                break;

            case DIAMOND_ORE:
                type = EntityType.WITCH;
                break;

            default:
                type = null;
                break;
        }

        Entity entity;

        if (type != null && random.nextDouble() < (double) this.moduleConfiguration.get("chance"))
        {
            entity = event.getBlock().getWorld().spawnEntity(event.getBlock().getLocation(), type);

            if (type == EntityType.SKELETON)
                ((Skeleton) entity).getEquipment().clear(); //Remove skeleton bow
        }
    }

    public static class ConfigurationBuilder implements IConfigurationBuilder
    {
        private double chance;

        public ConfigurationBuilder()
        {
            this.chance = 0.4D;
        }

        @Override
        public Map<String, Object> build()
        {
            Map<String, Object> moduleConfiguration = new HashMap<>();

            moduleConfiguration.put("chance", this.chance);

            return moduleConfiguration;
        }

        @Override
        public Map<String, Object> buildFromJson(Map<String, JsonElement> configuration) throws Exception
        {
            if (configuration.containsKey("chance"))
                this.setChance(configuration.get("chance").getAsInt());

            return this.build();
        }

        public MobOresModule.ConfigurationBuilder setChance(double chance)
        {
            this.chance = chance;
            return this;
        }
    }
}
