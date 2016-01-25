package net.samagames.survivalapi.modules.entity;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * EntityDropModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
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

    public static class ConfigurationBuilder
    {
        private final Map<EntityType, ItemStack[]> drops;

        public ConfigurationBuilder()
        {
            this.drops = new HashMap<>();
        }

        public Map<String, Object> build()
        {
            Map<String, Object> moduleConfiguration = new HashMap<>();

            moduleConfiguration.put("drops", this.drops);

            return moduleConfiguration;
        }

        public ConfigurationBuilder addCustomDrops(EntityType entity, ItemStack... stacks)
        {
            this.drops.put(entity, stacks);
            return this;
        }
    }
}
