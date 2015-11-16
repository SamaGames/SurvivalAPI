package net.samagames.survivalapi.game.types;

import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.game.SurvivalGameLoop;
import net.samagames.survivalapi.game.SurvivalPlayer;
import net.samagames.tools.Titles;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class SurvivalSoloGame<SURVIVALLOOP extends SurvivalGameLoop> extends SurvivalGame
{
    public SurvivalSoloGame(JavaPlugin plugin, String gameCodeName, String gameName, String gameDescription, String magicSymbol, Class<? extends SURVIVALLOOP> survivalGameLoopClass)
    {
        super(plugin, gameCodeName, gameName, gameDescription, magicSymbol, survivalGameLoopClass);
    }

    @Override
    public void checkStump(Player player)
    {
        SurvivalPlayer playerData = (SurvivalPlayer) this.getPlayer(player.getUniqueId());

        if (getInGamePlayers().size() == 3)
        {
            playerData.addCoins(20, "Troisième au classement !");
        }
        else if (getInGamePlayers().size() == 2)
        {
            playerData.addCoins(50, "Second au classement !");
            playerData.addStars(1, "Second au classement !");

            this.gamePlayers.remove(playerData.getUUID());
            UUID winnerId = (UUID) this.getInGamePlayers().keySet().iterator().next();
            this.gamePlayers.put(playerData.getUUID(), playerData);

            Player winner = this.server.getPlayer(winnerId);

            if (winner == null)
                this.handleGameEnd();
            else
                this.win(winner);
        }
        else if (getInGamePlayers().size() == 1)
        {
            this.handleGameEnd();
        }
        else
        {
            this.coherenceMachine.getMessageManager().writeCustomMessage(ChatColor.YELLOW + "Il reste encore " + ChatColor.AQUA + (this.getInGamePlayers().size() - 1) + ChatColor.YELLOW + " joueur" + ((this.getInGamePlayers().size() - 1) > 1 ? "s" : "") + "en vie.", true);
        }
    }

    public void win(final Player player)
    {
        final SurvivalPlayer playerData = (SurvivalPlayer) this.getPlayer(player.getUniqueId());

        if (playerData != null)
        {
            playerData.addStars(2, "Victoire !");
            playerData.addCoins(100, "Victoire ! ");

            this.server.getScheduler().runTaskAsynchronously(this.plugin, () ->
            {
                try
                {
                    this.increaseStat(player.getUniqueId(), "wins", 1);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            });

            for (Player user : this.server.getOnlinePlayers())
                Titles.sendTitle(user, 0, 60, 5, ChatColor.RED + "Fin du jeu", ChatColor.YELLOW + "Victoire de " + player.getName());

            this.coherenceMachine.getTemplateManager().getPlayerWinTemplate().execute(player);

            this.effectsOnWinner(player);
        }
        this.handleGameEnd();
    }

    @Override
    public void teleport()
    {
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

            Location location = locationIterator.next();
            player.teleport(location);
        }
    }
}
