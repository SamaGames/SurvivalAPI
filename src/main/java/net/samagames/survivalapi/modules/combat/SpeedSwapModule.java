package net.samagames.survivalapi.modules.combat;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.games.GamePlayer;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.Map;

/**
 * SpeedSwapModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class SpeedSwapModule extends AbstractSurvivalModule
{
    private boolean isSpeed;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public SpeedSwapModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        this.isSpeed = true;
    }

    /**
     * Toggle speed when player dies
     *
     * @param event Event
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        for (GamePlayer player : (Collection<GamePlayer>) SamaGamesAPI.get().getGameManager().getGame().getInGamePlayers().values())
        {
            Player p = player.getPlayerIfOnline();

            if (p == null)
                return;

            p.addPotionEffect((this.isSpeed ? PotionEffectType.SPEED : PotionEffectType.SLOW).createEffect(Integer.MAX_VALUE, 1));
        }

        this.isSpeed = !this.isSpeed;
    }
}
