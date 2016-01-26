package net.samagames.survivalapi.modules;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.game.SurvivalGame;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * AbstractSurvivalModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public abstract class AbstractSurvivalModule implements Listener
{
    protected final SurvivalPlugin plugin;
    protected final SurvivalAPI api;
    protected final Map<String, Object> moduleConfiguration;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public AbstractSurvivalModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        this.plugin = plugin;
        this.api = api;
        this.moduleConfiguration = moduleConfiguration;
    }

    /**
     * Fired when the game starts
     *
     * @param game Game instance
     */
    public void onGameStart(SurvivalGame game) {}

    /**
     * Get module's dependencies
     *
     * @return A list of modules
     */
    public List<Class<? extends AbstractSurvivalModule>> getRequiredModules()
    {
        return new ArrayList<>();
    }
}
