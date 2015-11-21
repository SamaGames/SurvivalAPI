package net.samagames.survivalapi.games;

import net.samagames.survivalapi.SurvivalGenerator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;

public abstract class AbstractGame implements Listener
{
    protected final SurvivalGenerator plugin;

    public AbstractGame(SurvivalGenerator plugin)
    {
        this.plugin = plugin;
    }

    public abstract void preInit();
    public abstract void init(WorldInitEvent event);

    @EventHandler
    public void onWorldInit(final WorldInitEvent event)
    {
        this.init(event);
    }
}
