package net.samagames.survivalapi.modules.combat;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.Material;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.HashMap;

public class AutomaticTNTModule extends AbstractSurvivalModule
{
    private SurvivalGame game;

    public AutomaticTNTModule(SurvivalPlugin plugin, SurvivalAPI api, HashMap<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    @Override
    public void onGameStart(SurvivalGame game)
    {
        this.game = game;
    }

    /**
     * Fire the TNT's automatically
     *
     * @param event
     */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        if (this.game.isPvPActivated())
        {
            if (event.getBlock().getType() == Material.TNT)
            {
                event.getBlock().setType(Material.AIR);
                TNTPrimed tnt = event.getBlock().getWorld().spawn(event.getBlock().getLocation(), TNTPrimed.class);
                tnt.setFuseTicks(tnt.getFuseTicks() / 2);
            }
        }
    }
}
