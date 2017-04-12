package net.samagames.survivalapi.modules.utility;

import com.google.gson.JsonElement;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.survivalapi.modules.IConfigurationBuilder;
import org.apache.commons.lang.Validate;

import java.util.HashMap;
import java.util.Map;

/**
 * GenerationModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class GenerationModule extends AbstractSurvivalModule
{
    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public GenerationModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        Validate.notNull(moduleConfiguration, "Configuration cannot be null!");

        SurvivalAPI.get().setCustomMapName((String) moduleConfiguration.get("map-name"));
    }

    public static class ConfigurationBuilder implements IConfigurationBuilder
    {
        private String mapName;

        public ConfigurationBuilder()
        {
            this.mapName = "<MAP NAME>";
        }

        @Override
        public Map<String, Object> build()
        {
            Map<String, Object> moduleConfiguration = new HashMap<>();

            moduleConfiguration.put("map-name", this.mapName);

            return moduleConfiguration;
        }

        @Override
        public Map<String, Object> buildFromJson(Map<String, JsonElement> configuration) throws Exception
        {
            if (configuration.containsKey("map-name"))
                this.setMapName(configuration.get("map-name").getAsString());

            return this.build();
        }

        public GenerationModule.ConfigurationBuilder setMapName(String mapName)
        {
            this.mapName = mapName;
            return this;
        }
    }
}