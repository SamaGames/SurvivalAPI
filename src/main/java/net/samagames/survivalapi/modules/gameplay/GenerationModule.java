package net.samagames.survivalapi.modules.gameplay;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;

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
     * @param plugin              Parent plugin
     * @param api                 API instance
     * @param moduleConfiguration Module configuration
     * @param name The map sub-name
     */
    public GenerationModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration, String name)
    {
        super(plugin, api, moduleConfiguration);
        SurvivalAPI.get().setCustomMapName(name);
    }
}