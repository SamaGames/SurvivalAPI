package net.samagames.survivalapi;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.samagames.api.SamaGamesAPI;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.survivalapi.modules.IConfigurationBuilder;
import org.bukkit.event.HandlerList;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * SurvivalAPI class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class SurvivalAPI
{
    public enum EventType { POSTINIT, WORLDLOADED, AFTERGENERATION }

    private static SurvivalAPI instance;

    private final SurvivalPlugin plugin;
    private final Map<String, AbstractSurvivalModule> modulesLoaded;
    private final Map<EventType, List<Runnable>> events;
    private String customMap;

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
        this.customMap = null;
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
     * @param defaultModuleConfiguration Module's configuration. Can be a default configuration {@link net.samagames.survivalapi.modules.block.RapidOresModule}
     */
    public void loadModule(Class<? extends AbstractSurvivalModule> moduleClass, Map<String, Object> defaultModuleConfiguration)
    {
        if(!this.modulesLoaded.containsKey(moduleClass.getSimpleName()))
        {
            try
            {
                JsonObject gameOptions = SamaGamesAPI.get().getGameManager().getGameProperties().getGameOptions();
                Map<String, Object> moduleConfiguration = defaultModuleConfiguration;

                if (gameOptions.has("modules"))
                {
                    if (gameOptions.getAsJsonObject("modules").has(moduleClass.getSimpleName()))
                    {
                        JsonObject moduleConfigurationJson = gameOptions.getAsJsonObject("modules").get(moduleClass.getSimpleName()).getAsJsonObject();

                        if (moduleConfigurationJson.has("enabled") && moduleConfigurationJson.get("enabled").getAsBoolean())
                        {
                            Map<String, JsonElement> moduleConfigurationRaw = new HashMap<>();

                            for (Map.Entry<String,JsonElement> entry : moduleConfigurationJson.entrySet())
                                moduleConfigurationRaw.put(entry.getKey(), entry.getValue());

                            try
                            {
                                Class<?> moduleConfigurationBuilderClass = Class.forName(moduleClass.getName() + "$ConfigurationBuilder");

                                if (IConfigurationBuilder.class.isAssignableFrom(moduleConfigurationBuilderClass))
                                {
                                    IConfigurationBuilder moduleConfigurationBuilder = (IConfigurationBuilder) moduleConfigurationBuilderClass.newInstance();

                                    try
                                    {
                                        moduleConfiguration = moduleConfigurationBuilder.buildFromJson(moduleConfigurationRaw);
                                    }
                                    catch (Exception e)
                                    {
                                        this.plugin.getLogger().severe("An error occured while creating the custom module configuration of the module " + moduleClass.getSimpleName() + "!");
                                    }
                                }
                                else
                                {
                                    this.plugin.getLogger().severe("The ConfigurationBuilder of the module " + moduleClass.getSimpleName() + " is not implementing the IConfigurationBuilder interface. Using default configuration.");
                                }
                            }
                            catch (ClassNotFoundException ignored)
                            {
                                this.plugin.getLogger().warning("A custom configuration was found but no ConfigurationBuilder was found for the module " + moduleClass.getSimpleName() + "!");
                            }
                            catch (IllegalAccessException | InstantiationException e)
                            {
                                this.plugin.getLogger().severe("Failed to create the custom configuration for the module " + moduleClass.getSimpleName() + "!");
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            this.plugin.getLogger().info("The custom configuration specify that the module " + moduleClass.getSimpleName() + " has to remain disabled. Passing...");
                            return;
                        }
                    }
                }

                AbstractSurvivalModule module = moduleClass.getConstructor(SurvivalPlugin.class, SurvivalAPI.class, Map.class).newInstance(this.plugin, this, moduleConfiguration);

                for (Class<? extends AbstractSurvivalModule> requiredModule : module.getRequiredModules())
                    this.loadModule(requiredModule, null);

                this.plugin.getServer().getPluginManager().registerEvents(module, this.plugin);

                this.modulesLoaded.put(moduleClass.getSimpleName(), module);
                this.plugin.getLogger().info("Module loaded: " + moduleClass.getSimpleName() + " (" + this.modulesLoaded.size() + " modules loaded)");
            }
            catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
            {
                this.plugin.getLogger().log(Level.SEVERE, "Error loading module", e);
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
     * Set a custom map for download
     */
    public void setCustomMapName(String map)
    {
        this.customMap = map;
    }

    /**
     * Get the custom map name if changed
     * @return Custom Map Name
     */
    public String getCustomMapName()
    {
        return this.customMap;
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
