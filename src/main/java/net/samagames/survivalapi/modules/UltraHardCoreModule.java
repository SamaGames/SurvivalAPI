package net.samagames.survivalapi.modules;

import net.samagames.survivalapi.SurvivalPlugin;
import org.bukkit.event.Listener;

public class UltraHardCoreModule implements ISurvivalModule
{
    @Override
    public void enable(SurvivalPlugin plugin)
    {

    }

    @Override
    public void disable(SurvivalPlugin plugin)
    {

    }

    @Override
    public Listener getModuleListener()
    {
        return null;
    }

    @Override
    public String getIdentifier()
    {
        return "ultrahardcore";
    }
}
