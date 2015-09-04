package net.samagames.survivalapi;

import net.samagames.survivalapi.modules.ISurvivalModule;
import net.samagames.survivalapi.modules.SurvivalModules;

import java.util.HashMap;

public class SurvivalAPI
{
    private static SurvivalAPI instance;

    private final SurvivalPlugin plugin;
    private final HashMap<String, ISurvivalModule> modules;

    public SurvivalAPI(SurvivalPlugin plugin)
    {
        instance = this;

        this.plugin = plugin;
        this.modules = new HashMap<>();
    }

    public void loadModule(SurvivalModules module)
    {
        try
        {
            this.loadModule(module.getModuleClass().newInstance());
        }
        catch (InstantiationException | IllegalAccessException e)
        {
            this.plugin.getLogger().severe("Failed to load module: " + module.name() + "!");
            e.printStackTrace();
        }
    }

    public void loadModule(ISurvivalModule module)
    {
        if(!this.modules.containsKey(module.getIdentifier()))
        {
            module.enable(this.plugin);
            this.modules.put(module.getIdentifier(), module);

            this.plugin.getLogger().info("Module loaded: " + module.getIdentifier());
        }
        else
        {
            throw new IllegalStateException("Module already registered!");
        }
    }

    public void unloadModule(SurvivalModules module)
    {
        this.unloadModule(module.name().toLowerCase());
    }

    public void unloadModule(String identifier)
    {
        if(this.modules.containsKey(identifier))
        {
            this.modules.get(identifier).disable(this.plugin);
            this.modules.remove(identifier);

            this.plugin.getLogger().info("Module unloaded: " + identifier);
        }
    }

    public HashMap<String, ISurvivalModule> getModules()
    {
        return this.modules;
    }

    public boolean isLoaded(String identifier)
    {
        return this.modules.containsKey(identifier);
    }

    public static SurvivalAPI get()
    {
        return instance;
    }
}
