package net.samagames.survivalapi.modules.gameplay;

import net.samagames.api.games.GamePlayer;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;

public class PotentialHeartsModule extends AbstractSurvivalModule
{
    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public PotentialHeartsModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * Multiply max health by 2
     *
     * @param game Game
     */
    @Override
    public void onGameStart(SurvivalGame game)
    {
        for (GamePlayer player : (Collection<GamePlayer>) game.getInGamePlayers().values())
        {
            Player p = player.getPlayerIfOnline();
            p.setMaxHealth(20D);
            p.setHealth(10D);
        }
    }
}
