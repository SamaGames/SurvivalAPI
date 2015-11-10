package net.samagames.survivalapi.modules.gameplay;

import net.samagames.api.games.Game;
import net.samagames.api.games.GamePlayer;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;

import java.util.Collection;
import java.util.HashMap;

public class DoubleHealthModule extends AbstractSurvivalModule
{
    public DoubleHealthModule(SurvivalPlugin plugin, SurvivalAPI api, HashMap<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * Double the health on game's start
     *
     * @param game Game
     */
    @Override
    public void onGameStart(Game game)
    {
        for (GamePlayer player : (Collection<GamePlayer>) game.getInGamePlayers())
        {
            player.getPlayerIfOnline().setMaxHealth(40.0D);
            player.getPlayerIfOnline().setHealth(40.0D);
        }
    }
}
