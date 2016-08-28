package net.samagames.survivalapi.games;

import net.samagames.survivalapi.SurvivalGenerator;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldSaveEvent;

public abstract class AbstractGame implements Listener
{
    protected final SurvivalGenerator plugin;

    public AbstractGame(SurvivalGenerator plugin)
    {
        this.plugin = plugin;
    }

    public abstract void preInit();
    public abstract void init(World world);

    public void onFinish(World world)
    {
        this.plugin.getWorldLoader().computeTop(world, () -> plugin.finishGeneration(world, System.currentTimeMillis() - this.plugin.getWorldLoader().getStartTime()));
    }

    public void onLoaded(World world) {}

    @EventHandler
    public void onWorldInit(final WorldInitEvent event)
    {
        if (event.getWorld().getEnvironment() == World.Environment.NORMAL)
            this.init(event.getWorld());
    }

    @EventHandler
    public void onWorldLoaded(final WorldSaveEvent event)
    {
        if (event.getWorld().getEnvironment() == World.Environment.NORMAL)
            this.onLoaded(event.getWorld());
    }
}
