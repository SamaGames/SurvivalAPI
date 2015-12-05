package net.samagames.survivalapi.game.types.run;

import net.samagames.survivalapi.game.SurvivalGameLoop;
import net.samagames.survivalapi.game.types.SurvivalSoloGame;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class RunBasedSoloGame<SURVIVALLOOP extends SurvivalGameLoop> extends SurvivalSoloGame<SURVIVALLOOP> implements RunBasedGame
{
    public RunBasedSoloGame(JavaPlugin plugin, String gameCodeName, String gameName, String gameDescription, String magicSymbol, Class<? extends SURVIVALLOOP> survivalGameLoopClass)
    {
        super(plugin, gameCodeName, gameName, gameDescription, ChatColor.ITALIC + magicSymbol, survivalGameLoopClass);

        this.applyModules();
    }

    @Override
    public void teleportDeathMatch()
    {
        Collections.shuffle(this.spawns);
        Iterator<Location> locationIterator = this.spawns.iterator();

        for (UUID uuid : (Set<UUID>) this.getInGamePlayers().keySet())
        {
            Player player = this.server.getPlayer(uuid);

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
            player.teleport(new Location(location.getWorld(), location.getX() * 4 / 10, 150.0, location.getZ() * 4 / 10));
        }
    }
}
