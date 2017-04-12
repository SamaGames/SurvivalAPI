package net.samagames.survivalapi.modules.entity;

import com.google.gson.JsonElement;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.survivalapi.modules.IConfigurationBuilder;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * InfestationModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class InfestationModule extends AbstractSurvivalModule
{
    private final Random random;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public InfestationModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        Validate.notNull(moduleConfiguration, "Configuration cannot be null!");

        this.random = new Random();
    }

    /**
     * When a player kill a mob there's a 40% chance to spawn the same at the same place
     *
     * @param event Event
     */
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event)
    {
        if (event.getEntity().getType() == EntityType.PLAYER)
            return;

        if (this.random.nextDouble() < (double) this.moduleConfiguration.get("chance"))
            event.getEntity().getWorld().spawnEntity(event.getEntity().getLocation(), event.getEntity().getType());
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

        public InfestationModule.ConfigurationBuilder setChance(double chance)
        {
            this.chance = chance;
            return this;
        }
    }
}
