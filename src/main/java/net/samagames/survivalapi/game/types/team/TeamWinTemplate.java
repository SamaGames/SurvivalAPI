package net.samagames.survivalapi.game.types.team;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.games.themachine.messages.templates.WinMessageTemplate;
import net.samagames.survivalapi.game.SurvivalTeam;
import net.samagames.tools.chat.ChatUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.UUID;

public class TeamWinTemplate extends WinMessageTemplate
{
    public ArrayList<String> prepare(SurvivalTeam team)
    {
        ArrayList<String> custom = new ArrayList<>();
        ArrayList<String> players = new ArrayList<>();

        custom.add(ChatUtils.getCenteredText(ChatColor.GREEN + "Gagnant" + ChatColor.GRAY + " - " + ChatColor.RESET + team.getChatColor() + team.getTeamName()));

        for (UUID teammate : team.getPlayersUUID())
        {
            if (Bukkit.getPlayer(teammate) != null)
                players.add(Bukkit.getPlayer(teammate).getName());
            else
                players.add(SamaGamesAPI.get().getUUIDTranslator().getName(teammate));
        }

        custom.add(ChatUtils.getCenteredText(StringUtils.join(players, ", ")));

        return super.prepare(custom);
    }

    public void execute(SurvivalTeam team)
    {
        new WinMessageTemplate().execute(this.prepare(team));
    }
}
