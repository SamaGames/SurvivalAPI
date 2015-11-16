package net.samagames.survivalapi.modules;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.game.SurvivalGame;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbstractSurvivalModule implements Listener
{
    protected final SurvivalPlugin plugin;
    protected final SurvivalAPI api;
    protected final HashMap<String, Object> moduleConfiguration;

    public AbstractSurvivalModule(SurvivalPlugin plugin, SurvivalAPI api, HashMap<String, Object> moduleConfiguration)
    {
        this.plugin = plugin;
        this.api = api;
        this.moduleConfiguration = moduleConfiguration;
    }

    public void onGameStart(SurvivalGame game) {}

    public ArrayList<Class<? extends AbstractSurvivalModule>> getRequiredModules()
    {
        return new ArrayList<>();
    }
}
