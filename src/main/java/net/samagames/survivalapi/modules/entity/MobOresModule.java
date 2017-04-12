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

/**
 * MobOresModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
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
