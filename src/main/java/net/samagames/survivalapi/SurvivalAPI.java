package net.samagames.survivalapi;

import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.event.HandlerList;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SurvivalAPI class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class SurvivalAPI
{
    public enum EventType { POSTINIT, AFTERGENERATION }

    private static SurvivalAPI instance;

    private final SurvivalPlugin plugin;
    private final Map<String, AbstractSurvivalModule> modulesLoaded;
    private final Map<EventType, List<Runnable>> events;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     */
    public SurvivalAPI(SurvivalPlugin plugin)
    {
        instance = this;

        this.plugin = plugin;
        this.modulesLoaded = new HashMap<>();
        this.events = new HashMap<>();
    }

    /**
     * Fire the function `onGameStart()` in each modules loaded
     *
     * @param game Instance of the game
     */
    public void fireGameStart(SurvivalGame game)
    {
        this.modulesLoaded.values().stream().forEach(module -> module.onGameStart(game));
    }

    /**
     * Register a hook into the SurvivalAPI to fire an event
     *
     * @param eventType When the event has to be fired
     * @param callback Event
     */
    public void registerEvent(EventType eventType, Runnable callback)
    {
        if (!this.events.containsKey(eventType))
            this.events.put(eventType, new ArrayList<>());

        this.events.get(eventType).add(callback);
    }

    /**
     * Fire the events registered of a given type
     *
     * @param eventType Event type
     */
    public void fireEvents(EventType eventType)
    {
        if (!this.events.containsKey(eventType))
            return;

        this.events.get(eventType).forEach(Runnable::run);
    }

    /**
     * Load a SurvivalAPI's module
     *
     * @param moduleClass Module class
     * @param moduleConfiguration Module's configuration. Can be a default configuration {@link net.samagames.survivalapi.modules.block.RapidOresModule}
     */
    public void loadModule(Class<? extends AbstractSurvivalModule> moduleClass, Map<String, Object> moduleConfiguration)
    {
        if(!this.modulesLoaded.containsKey(moduleClass.getSimpleName()))
        {
            try
            {
                AbstractSurvivalModule module = moduleClass.getConstructor(SurvivalPlugin.class, SurvivalAPI.class, Map.class).newInstance(this.plugin, this, moduleConfiguration);

                for (Class<? extends AbstractSurvivalModule> requiredModule : module.getRequiredModules())
                    this.loadModule(requiredModule, null);

                this.plugin.getServer().getPluginManager().registerEvents(module, this.plugin);

                this.modulesLoaded.put(moduleClass.getSimpleName(), module);
                this.plugin.getLogger().info("Module loaded: " + moduleClass.getSimpleName() + " (" + this.modulesLoaded.size() + " modules loaded)");
            }
            catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Unload a given module
     *
     * @param moduleClass Module class
     */
    public void unloadModule(Class<? extends AbstractSurvivalModule> moduleClass)
    {
        if(this.modulesLoaded.containsKey(moduleClass.getSimpleName()))
        {
            AbstractSurvivalModule module = this.modulesLoaded.get(moduleClass.getSimpleName());

            HandlerList.unregisterAll(module);

            this.modulesLoaded.remove(moduleClass.getSimpleName());
            this.plugin.getLogger().info("Module unloaded: " + moduleClass.getSimpleName() + " (" + this.modulesLoaded.size() + " modules loaded)");
        }
    }

    /**
     * Is a module enabled
     *
     * @param moduleClass Module class
     *
     * @return {@code true} if enabled or {@code false}
     */
    public boolean isModuleEnabled(Class<? extends AbstractSurvivalModule> moduleClass)
    {
        return this.modulesLoaded.containsKey(moduleClass.getSimpleName());
    }

    /**
     * Get the instance of a given module
     *
     * @param type Module class
     * @param <T> -
     *
     * @return Module's instance
     */
    public <T extends AbstractSurvivalModule> T getModule(Class<T> type)
    {
        if(isModuleEnabled(type))
        {
            return (T) this.modulesLoaded.get(type.getSimpleName());
        }
        return null;
    }

    /**
     * Get the parent plugin of the API
     *
     * @return Parent plugin instance
     */
    public SurvivalPlugin getPlugin()
    {
        return this.plugin;
    }

    /**
     * Get the instance of the API
     *
     * @return Instance
     */
    public static SurvivalAPI get()
    {
        return instance;
    }
}
