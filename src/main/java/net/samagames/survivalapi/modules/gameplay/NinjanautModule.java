package net.samagames.survivalapi.modules.gameplay;

import net.samagames.api.SamaGamesAPI;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.game.SurvivalPlayer;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * NinjanautModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class NinjanautModule extends AbstractSurvivalModule
{
    private UUID ninjanaut;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public NinjanautModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        this.ninjanaut = null;
    }

    /**
     * Select one ninjanaut at start
     *
     * @param game Game instance
     */
    @Override
    public void onGameStart(SurvivalGame game)
    {
        super.onGameStart(game);
        Map map = game.getInGamePlayers();
        int r = new Random().nextInt(map.size());
        for (SurvivalPlayer player : (Collection<SurvivalPlayer>)map.values())
        {
            if (r == 0)
            {
                setNinjaNaut(game, player, true);
                return ;
            }
            r--;
        }
    }

    /**
     * On kill, give Ninjanaut to killer
     *
     * @param event Death event instace
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        if (this.ninjanaut == null || !event.getEntity().getUniqueId().equals(this.ninjanaut) || event.getEntity().getKiller() == null
                || event.getEntity().getKiller() == event.getEntity())
            return ;
        SurvivalGame game = (SurvivalGame)SamaGamesAPI.get().getGameManager().getGame();
        SurvivalPlayer player = (SurvivalPlayer)game.getPlayer(event.getEntity().getKiller().getUniqueId());
        if (player != null)
            setNinjaNaut(game, player, false);
    }

    private void setNinjaNaut(SurvivalGame game, SurvivalPlayer player, boolean first)
    {
        if (!player.isOnline() || player.isSpectator())
            return ;
        this.ninjanaut = player.getUUID();
        game.getCoherenceMachine().getMessageManager().writeCustomMessage(player.getPlayerData().getDisplayName() + (first ? " a été choisi comme Ninjanaut" : " a tué le Ninjanaut, et prends donc sa place") + ". Il sera plus résistant, tuez le pour prendre sa place.", true);
        Player bukkitPlayer = player.getPlayerIfOnline();
        bukkitPlayer.addPotionEffect(PotionEffectType.SPEED.createEffect(Integer.MAX_VALUE, 2));
        bukkitPlayer.addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(Integer.MAX_VALUE, 2));
    }
}
