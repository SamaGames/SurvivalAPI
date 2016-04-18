package net.samagames.survivalapi.modules.gameplay;

import net.samagames.api.games.GamePlayer;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;

/**
 * NightmareModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class NightmareModule extends AbstractSurvivalModule
{
    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public NightmareModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * Set always night
     * @param game Game instance
     */
    @Override
    public void onGameStart(SurvivalGame game)
    {
        World world = this.plugin.getServer().getWorlds().get(0);
        world.setGameRuleValue("doDaylightCycle", "false");
        world.setTime(15000);
        for (GamePlayer player : (Collection<GamePlayer>) game.getInGamePlayers().values())
        {
            Player p = player.getPlayerIfOnline();
            p.playSound(p.getLocation(), Sound.WITHER_SPAWN, 0.9F, 1F);
        }
    }
}

