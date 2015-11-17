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

public class CommandUHC implements CommandExecutor
{
    private static SurvivalGame game;

    public static void setGame(SurvivalGame instance)
    {
        game = instance;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if(!(commandSender instanceof Player))
            return true;

        if (strings.length > 0)
        {
            if (strings[0].equals("invite"))
            {
                if(game instanceof SurvivalTeamGame)
                {
                    if(!game.isGameStarted())
                    {
                        String teamRaw = strings[1];
                        String playerRaw = strings[2];
                        SurvivalPlayer player = (SurvivalPlayer) game.getPlayer(UUID.fromString(playerRaw));
                        SurvivalTeam aTeam = ((SurvivalTeamGame) game).getTeams().getTeam(ChatColor.getByChar(teamRaw));

                        if (aTeam == null)
                            return true;

                        if(!player.hasTeam())
                            if(!aTeam.isInvited(player.getUUID()))
                                aTeam.invite(commandSender.getName(), player.getUUID());
                    }
                }
            }
            else if (strings[0].equals("join"))
            {
                if(game instanceof SurvivalTeamGame)
                {
                    if(!game.isGameStarted())
                    {
                        String teamRaw = strings[1];
                        String playerRaw = strings[2];
                        SurvivalPlayer player = (SurvivalPlayer) game.getPlayer(UUID.fromString(playerRaw));
                        SurvivalTeam aTeam = ((SurvivalTeamGame) game).getTeams().getTeam(ChatColor.getByChar(teamRaw));

                        if (aTeam == null)
                            return true;

                        if(!player.hasTeam())
                            if(aTeam.isInvited(player.getUUID()))
                                aTeam.join(player.getUUID());
                    }
                }
            }
        }

        return true;
    }
}
