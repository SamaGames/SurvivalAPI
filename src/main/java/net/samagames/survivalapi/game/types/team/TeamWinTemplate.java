package net.samagames.survivalapi.game.types.team;

import net.samagames.api.games.themachine.messages.templates.WinMessageTemplate;
import net.samagames.survivalapi.game.SurvivalTeam;
import net.samagames.tools.PlayerUtils;
import net.samagames.tools.chat.ChatUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
public class TeamWinTemplate
{
    /**
     * Prepare the message with the team's members
     *
     * @param team Winning team
     *
     * @return A list of lines
     */
    public List<String> prepare(SurvivalTeam team)
    {
        List<String> custom = new ArrayList<>();
        List<String> players = new ArrayList<>();

        custom.add(ChatUtils.getCenteredText(ChatColor.GREEN + "Gagnant" + ChatColor.GRAY + " - Equipe " + ChatColor.RESET + team.getChatColor() + team.getTeamName()));

        players.addAll(team.getPlayersUUID().keySet().stream().map(PlayerUtils::getColoredFormattedPlayerName).collect(Collectors.toList()));

        custom.add(ChatUtils.getCenteredText(StringUtils.join(players, ", ")));

        return custom;
    }

    /**
     * Send the message with the team's members
     *
     * @param team Winning team
     */
    public void execute(SurvivalTeam team)
    {
        new WinMessageTemplate().execute(this.prepare(team));
    }
}
