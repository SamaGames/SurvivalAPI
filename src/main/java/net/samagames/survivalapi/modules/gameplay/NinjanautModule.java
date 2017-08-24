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

/*
 * This file is part of SurvivalAPI.
 *
 * SurvivalAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SurvivalAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SurvivalAPI.  If not, see <http://www.gnu.org/licenses/>.
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
                this.setNinjaNaut(game, player, true);
                return;
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
            return;

        SurvivalGame game = (SurvivalGame)SamaGamesAPI.get().getGameManager().getGame();
        SurvivalPlayer player = (SurvivalPlayer)game.getPlayer(event.getEntity().getKiller().getUniqueId());

        if (player != null)
            this.setNinjaNaut(game, player, false);
    }

    private void setNinjaNaut(SurvivalGame game, SurvivalPlayer player, boolean first)
    {
        if (!player.isOnline() || player.isSpectator())
            return;

        this.ninjanaut = player.getUUID();

        game.getCoherenceMachine().getMessageManager().writeCustomMessage(player.getPlayerData().getDisplayName() + (first ? " a été choisi comme Ninjanaut" : " a tué le Ninjanaut, et prends donc sa place") + ". Il sera plus résistant, tuez le pour prendre sa place.", true);

        Player p = player.getPlayerIfOnline();
        p.addPotionEffect(PotionEffectType.SPEED.createEffect(Integer.MAX_VALUE, 2));
        p.addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(Integer.MAX_VALUE, 2));
    }
}
