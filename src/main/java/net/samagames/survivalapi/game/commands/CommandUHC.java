package net.samagames.survivalapi.game.commands;

import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.game.SurvivalPlayer;
import net.samagames.survivalapi.game.SurvivalTeam;
import net.samagames.survivalapi.game.types.SurvivalTeamGame;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * CommandUHC class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class CommandUHC implements CommandExecutor
{
    private static SurvivalGame game;

    /**
     * Set the game instance to use later
     *
     * @param instance Game instance
     */
    public static void setGame(SurvivalGame instance)
    {
        game = instance;
    }

    /**
     * Command executor
     *
     * @param commandSender Sender
     * @param command Command instance
     * @param s Command's name
     * @param strings Command's arguments
     *
     * @return {@code true} is successfully executed or {@code false}
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if(!(commandSender instanceof Player))
            return true;

        if (strings.length > 0)
        {
            if ("invite".equalsIgnoreCase(strings[0]) && strings.length == 3)
            {
                if(game instanceof SurvivalTeamGame && !game.isGameStarted())
                {
                    String teamRaw = strings[1];
                    String playerRaw = strings[2];
                    SurvivalPlayer player = (SurvivalPlayer) game.getPlayer(UUID.fromString(playerRaw));
                    SurvivalTeam aTeam = ((SurvivalTeamGame) game).getTeams().getTeam(ChatColor.getByChar(teamRaw));

                    if (aTeam == null)
                        return true;

                    if(!player.hasTeam() && !aTeam.isInvited(player.getUUID()))
                        aTeam.invite(commandSender.getName(), player.getUUID());
                }
            }
            else if ("join".equalsIgnoreCase(strings[0]) && strings.length == 3 && game instanceof SurvivalTeamGame && !game.isGameStarted())
            {
                String teamRaw = strings[1];
                String playerRaw = strings[2];
                SurvivalPlayer player = (SurvivalPlayer) game.getPlayer(UUID.fromString(playerRaw));
                SurvivalTeam aTeam = ((SurvivalTeamGame) game).getTeams().getTeam(ChatColor.getByChar(teamRaw));

                if (aTeam == null)
                    return true;

                if(!player.hasTeam() && aTeam.isInvited(player.getUUID()))
                    aTeam.join(player.getUUID());
            }
        }

        return true;
    }
}
