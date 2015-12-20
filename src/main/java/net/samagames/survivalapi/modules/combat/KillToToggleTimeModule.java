package net.samagames.survivalapi.modules.combat;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Map;

public class KillToToggleTimeModule extends AbstractSurvivalModule
{
    private static final int DAY = 6000;
    private static final int NIGHT = 18000;

    private boolean isDay;

    public KillToToggleTimeModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);

        for (World world : plugin.getServer().getWorlds())
        {
            world.setTime(DAY);
            world.setGameRuleValue("doDaylightCycle", "false");
        }

        this.isDay = true;
    }

    /**
     * Toggle worlds times when a player die
     *
     * @param event Event
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        for (World world : this.plugin.getServer().getWorlds())
            world.setTime(this.isDay ? NIGHT : DAY);

        this.isDay = !this.isDay;
    }
}
