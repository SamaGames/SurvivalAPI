package net.samagames.survivalapi;

import org.bukkit.plugin.java.JavaPlugin;

public class SurvivalPlugin extends JavaPlugin
{
    private SurvivalAPI api;

    @Override
    public void onEnable()
    {
        this.api = new SurvivalAPI(this);
    }

    @Override
    public void onDisable()
    {
        this.api.getModules().keySet().forEach(this.api::unloadModule);
    }
}
