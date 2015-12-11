package net.samagames.survivalapi.game.types.run;

import net.samagames.survivalapi.game.SurvivalGameLoop;
import net.samagames.survivalapi.game.SurvivalTeam;
import net.samagames.survivalapi.game.types.SurvivalTeamGame;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Iterator;
import java.util.UUID;

public class RunBasedTeamGame<SURVIVALLOOP extends SurvivalGameLoop> extends SurvivalTeamGame<SURVIVALLOOP> implements RunBasedGame
{
    public RunBasedTeamGame(JavaPlugin plugin, String gameCodeName, String gameName, String gameDescription, String magicSymbol, Class<? extends SURVIVALLOOP> survivalGameLoopClass, int personsPerTeam)
    {
        super(plugin, gameCodeName, gameName, gameDescription, ChatColor.ITALIC + magicSymbol, survivalGameLoopClass, personsPerTeam);

        this.applyModules();
    }

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

                if (player == null)
                {
                    continue;
                }
                else
                {
                    this.removeEffects(player);
                    player.teleport(new Location(location.getWorld(), location.getX() * 4 / 10, 150.0, location.getZ() * 4 / 10));
                }
            }
        }
    }
}