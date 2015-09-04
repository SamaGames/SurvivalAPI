package net.samagames.survivalapi.modules;

import net.samagames.survivalapi.SurvivalPlugin;
import org.bukkit.event.Listener;

public interface ISurvivalModule
{
    void enable(SurvivalPlugin plugin);
    void disable(SurvivalPlugin plugin);

    Listener getModuleListener();

    String getIdentifier();
}
