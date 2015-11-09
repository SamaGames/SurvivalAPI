package net.samagames.survivalapi;

import com.google.gson.JsonObject;
import net.samagames.api.SamaGamesAPI;
import net.samagames.api.games.Game;
import net.samagames.api.games.GameHook;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class SurvivalAPI
{
    private static SurvivalAPI instance;

    private final SurvivalPlugin plugin;
    private final HashMap<String, AbstractSurvivalModule> modulesLoaded;

    public SurvivalAPI(SurvivalPlugin plugin)
    {
        instance = this;

        this.plugin = plugin;
        this.modulesLoaded = new HashMap<>();

        SamaGamesAPI.get().getGameManager().registerGameHook(new GameHook(GameHook.Type.START)
        {
            @Override
            public void run(Game game, Object... objects)
            {
                for (AbstractSurvivalModule module : modulesLoaded.values())
                    module.onGameStart(game);
            }
        });
    }

    public void loadModule(Class<? extends AbstractSurvivalModule> moduleClass, JsonObject moduleConfiguration)
    {
        if(!this.modulesLoaded.containsKey(moduleClass.getSimpleName()))
        {
            try
            {
                AbstractSurvivalModule module = moduleClass.getConstructor(SurvivalPlugin.class, SurvivalAPI.class, JsonObject.class).newInstance(this.plugin, this, moduleConfiguration);

                for (Class<? extends AbstractSurvivalModule> requiredModule : module.getRequiredModules())
                    this.loadModule(requiredModule, null);

                this.plugin.getServer().getPluginManager().registerEvents(module, this.plugin);

                this.modulesLoaded.put(moduleClass.getSimpleName(), module);
                this.plugin.getLogger().info("Module loaded: " + moduleClass.getSimpleName());
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
