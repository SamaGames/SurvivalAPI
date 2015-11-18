package net.samagames.survivalapi.game.types.team;

import net.samagames.api.games.themachine.messages.templates.WinMessageTemplate;
import net.samagames.survivalapi.game.SurvivalTeam;
import net.samagames.tools.PlayerUtils;
import net.samagames.tools.chat.ChatUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class TeamWinTemplate
{
    public ArrayList<String> prepare(SurvivalTeam team)
    {
        ArrayList<String> custom = new ArrayList<>();
        ArrayList<String> players = new ArrayList<>();

        custom.add(ChatUtils.getCenteredText(ChatColor.GREEN + "Gagnant" + ChatColor.GRAY + " - Equipe " + ChatColor.RESET + team.getChatColor() + team.getTeamName()));

        players.addAll(team.getPlayersUUID().stream().map(PlayerUtils::getColoredFormattedPlayerName).collect(Collectors.toList()));

        custom.add(ChatUtils.getCenteredText(StringUtils.join(players, ", ")));

        return custom;
    }

    public void execute(SurvivalTeam team)
    {
        new WinMessageTemplate().execute(this.prepare(team));
    }
}
