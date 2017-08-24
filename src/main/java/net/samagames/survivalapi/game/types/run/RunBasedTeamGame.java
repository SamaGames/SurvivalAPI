package net.samagames.survivalapi.game.types.run;

import net.samagames.survivalapi.game.SurvivalGameLoop;
import net.samagames.survivalapi.game.SurvivalTeam;
import net.samagames.survivalapi.game.types.SurvivalTeamGame;
import net.samagames.survivalapi.utils.ChunkUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Iterator;
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
public class RunBasedTeamGame<SURVIVALLOOP extends SurvivalGameLoop> extends SurvivalTeamGame<SURVIVALLOOP> implements RunBasedGame
{
    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param gameCodeName Game code name
     * @param gameName Game name
     * @param gameDescription Game description
     * @param magicSymbol Symbol into the scoreboard
     * @param survivalGameLoopClass Class of the game loop
     * @param personsPerTeam Number of players per team
     */
    public RunBasedTeamGame(JavaPlugin plugin, String gameCodeName, String gameName, String gameDescription, String magicSymbol, Class<? extends SURVIVALLOOP> survivalGameLoopClass, int personsPerTeam)
    {
        super(plugin, gameCodeName, gameName, gameDescription, ChatColor.ITALIC + magicSymbol, survivalGameLoopClass, personsPerTeam);

        this.applyModules();
    }

    /**
     * See {@link RunBasedGame}
     */
    @Override
    public void teleportDeathMatch()
    {
        Iterator<Location> locationIterator = this.spawns.iterator();

        for (SurvivalTeam team : this.teams)
        {
            if (!locationIterator.hasNext())
            {
                for (UUID playerUUID : team.getPlayersUUID().keySet())
                {
                    Player player = this.server.getPlayer(playerUUID);

                    if (player != null)
                        player.kickPlayer(ChatColor.RED + "Plus de place dans la partie.");

                    this.gamePlayers.remove(playerUUID);
                }

                continue;
            }

            Location location = locationIterator.next();

            for (UUID playerUUID : team.getPlayersUUID().keySet())
            {
                Player player = this.server.getPlayer(playerUUID);

                if (player != null)
                {
                    this.removeEffects(player);
                    Location destination = new Location(location.getWorld(), location.getX() * 4 / 10, 150.0, location.getZ() * 4 / 10);
                    ChunkUtils.loadDestination(player, destination, 3);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> player.teleport(destination), 2);
                }
            }
        }
    }
}