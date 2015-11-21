package net.samagames.survivalapi;

import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

public class SurvivalAPI
{
    public enum EventType { POSTINIT, AFTERGENERATION }

    private static SurvivalAPI instance;

    private final SurvivalPlugin plugin;
    private final HashMap<String, AbstractSurvivalModule> modulesLoaded;
    private final HashMap<EventType, ArrayList<Runnable>> events;

    public SurvivalAPI(SurvivalPlugin plugin)
    {
        instance = this;

        this.plugin = plugin;
        this.modulesLoaded = new HashMap<>();
        this.events = new HashMap<>();
    }

    public void fireGameStart(SurvivalGame game)
    {
        this.modulesLoaded.values().stream().forEach(module -> module.onGameStart(game));
    }

    public void registerEvent(EventType eventType, Runnable callback)
    {
        if (!this.events.containsKey(eventType))
            this.events.put(eventType, new ArrayList<>());

        this.events.get(eventType).add(callback);
    }

    public void fireEvents(EventType eventType)
    {
        if (!this.events.containsKey(eventType))
            return;

        this.events.get(eventType).forEach(Runnable::run);
    }

    public void loadModule(Class<? extends AbstractSurvivalModule> moduleClass, HashMap<String, Object> moduleConfiguration)
    {
        if(!this.modulesLoaded.containsKey(moduleClass.getSimpleName()))
        {
            try
            {
                AbstractSurvivalModule module = moduleClass.getConstructor(SurvivalPlugin.class, SurvivalAPI.class, HashMap.class).newInstance(this.plugin, this, moduleConfiguration);

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

    public boolean isModuleEnabled(Class<? extends AbstractSurvivalModule> moduleClass)
    {
        return this.modulesLoaded.containsKey(moduleClass.getSimpleName());
    }

    public static SurvivalAPI get()
    {
        return instance;
    }
}
