package net.samagames.survivalapi.game.types.run;

import net.samagames.survivalapi.game.SurvivalGameLoop;
import net.samagames.survivalapi.game.types.SurvivalSoloGame;
import net.samagames.survivalapi.utils.ChunkUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

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
public class RunBasedSoloGame<SURVIVALLOOP extends SurvivalGameLoop> extends SurvivalSoloGame<SURVIVALLOOP> implements RunBasedGame
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
     */
    public RunBasedSoloGame(JavaPlugin plugin, String gameCodeName, String gameName, String gameDescription, String magicSymbol, Class<? extends SURVIVALLOOP> survivalGameLoopClass)
    {
        super(plugin, gameCodeName, gameName, gameDescription, ChatColor.ITALIC + magicSymbol, survivalGameLoopClass);

        this.applyModules();
    }

    /**
     * See {@link RunBasedGame}
     */
    @Override
    public void teleportDeathMatch()
    {
        Collections.shuffle(this.spawns);
        Iterator<Location> locationIterator = this.spawns.iterator();

        List<UUID> players = new ArrayList<>();
        players.addAll(this.getInGamePlayers().keySet());

        for (UUID uuid : players)
        {
            Player player = Bukkit.getPlayer(uuid);

            if (player == null)
            {
                this.gamePlayers.remove(uuid);
                continue;
            }

            if (!locationIterator.hasNext())
            {
                player.kickPlayer(ChatColor.RED + "Plus de place dans la partie.");
                this.gamePlayers.remove(uuid);

                continue;
            }

            this.removeEffects(player);

            Location location = locationIterator.next();

            Location destination = new Location(location.getWorld(), location.getX() * 4 / 10, 150.0, location.getZ() * 4 / 10);
            ChunkUtils.loadDestination(player, destination, 3);
            Bukkit.getScheduler().runTaskLater(plugin, () -> player.teleport(destination), 2);

        }
    }
}
