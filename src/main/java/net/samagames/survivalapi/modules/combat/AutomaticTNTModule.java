package net.samagames.survivalapi.modules.combat;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.Material;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Map;

/**
 * AutomaticTNTModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class AutomaticTNTModule extends AbstractSurvivalModule
{
    private SurvivalGame game;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public AutomaticTNTModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        this.game = null;
    }

    @Override
    public void onGameStart(SurvivalGame game)
    {
        this.game = game;
    }

    /**
     * Fire the TNT's automatically
     *
     * @param event Event
     */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        if (this.game != null && this.game.isPvPActivated() && event.getBlock().getType() == Material.TNT)
        {
            event.getBlock().setType(Material.AIR);

            TNTPrimed tnt = event.getBlock().getWorld().spawn(event.getBlock().getLocation(), TNTPrimed.class);
            tnt.setFuseTicks(tnt.getFuseTicks() / 2);
        }
    }
}
