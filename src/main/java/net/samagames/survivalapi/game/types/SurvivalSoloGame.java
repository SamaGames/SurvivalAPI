package net.samagames.survivalapi.game.types;

import net.samagames.api.SamaGamesAPI;
import net.samagames.survivalapi.game.GameException;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.game.SurvivalGameLoop;
import net.samagames.survivalapi.game.SurvivalPlayer;
import net.samagames.survivalapi.utils.ChunkUtils;
import net.samagames.tools.Titles;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

/**
 * SurvivalSoloGame class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class SurvivalSoloGame<SURVIVALLOOP extends SurvivalGameLoop> extends SurvivalGame
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
    public SurvivalSoloGame(JavaPlugin plugin, String gameCodeName, String gameName, String gameDescription, String magicSymbol, Class<? extends SURVIVALLOOP> survivalGameLoopClass)
    {
        super(plugin, gameCodeName, gameName, gameDescription, magicSymbol, survivalGameLoopClass);
    }

    /**
     * See {@link SurvivalGame}
     *
     * @param playerUUID Player
     * @param silent Display messages
     *
     * @throws GameException
     */
    @Override
    public void checkStump(UUID playerUUID, boolean silent) throws GameException
    {
        try
        {
            if (this.getPlayer(playerUUID) == null)
            {
                this.plugin.getLogger().severe("Tried to stump an unknown player: " + playerUUID.toString() + "!");
                return;
            }

            SurvivalPlayer playerData = (SurvivalPlayer) this.getPlayer(playerUUID);

            if (this.getInGamePlayers().size() == 3)
            {
                playerData.addCoins(20, "Troisième au classement !");
            }
            else if (this.getInGamePlayers().size() == 2)
            {
                playerData.addCoins(50, "Second au classement !");

                this.gamePlayers.remove(playerData.getUUID());
                UUID winnerId = (UUID) this.getInGamePlayers().keySet().iterator().next();
                this.gamePlayers.put(playerData.getUUID(), playerData);

                Player winner = this.server.getPlayer(winnerId);

                if (winner == null)
                    this.handleGameEnd();
                else
                    this.win(winner);
            }
            else if (this.getInGamePlayers().size() == 1)
            {
                this.plugin.getLogger().info("Game finished.");
                this.handleGameEnd();
            }
            else if (!silent)
            {
                this.coherenceMachine.getMessageManager().writeCustomMessage(ChatColor.YELLOW + "Il reste encore " + ChatColor.AQUA + (this.getInGamePlayers().size() - 1) + ChatColor.YELLOW + " joueur" + ((this.getInGamePlayers().size() - 1) > 1 ? "s" : "") + " en vie.", true);
            }
        }
        catch (NullPointerException | IllegalStateException ignored)
        {
            throw new GameException(ignored.getMessage());
        }
    }

    /**
     * Execute win procedure
     *
     * @param player Player
     */
    public void win(final Player player)
    {
        SurvivalPlayer playerData = (SurvivalPlayer) this.getPlayer(player.getUniqueId());

        if (playerData != null)
        {
            playerData.addCoins(100, "Victoire ! ");

            for (Player user : this.server.getOnlinePlayers())
                Titles.sendTitle(user, 0, 60, 5, ChatColor.RED + "Fin du jeu", ChatColor.YELLOW + "Victoire de " + SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).getDisplayName());

            this.coherenceMachine.getTemplateManager().getPlayerWinTemplate().execute(player);

            this.handleWinner(player.getUniqueId());
            this.effectsOnWinner(player);
        }

        this.handleGameEnd();
    }

    /**
     * See {@link SurvivalGame}
     */
    @Override
    public void teleport()
    {
        Iterator<Location> locationIterator = this.spawns.iterator();

        for (UUID uuid : (Set<UUID>)this.getInGamePlayers().keySet())
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

            Location destination = locationIterator.next().add(0,8,0);
            ChunkUtils.loadDestination(player, destination, 3);
            Bukkit.getScheduler().runTaskLater(plugin, () -> player.teleport(destination), 2);
            SurvivalPlayer playerdata = (SurvivalPlayer)this.getPlayer(uuid);
            if (playerdata != null)
                playerdata.setWaitingSpawn(destination);
        }
    }
}
