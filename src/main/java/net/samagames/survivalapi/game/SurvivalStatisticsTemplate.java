package net.samagames.survivalapi.game;

import net.samagames.api.games.themachine.messages.templates.BasicMessageTemplate;
import net.samagames.tools.PlayerUtils;
import net.samagames.tools.chat.ChatUtils;
import org.bukkit.ChatColor;

import java.util.*;

/**
 * SurvivalStatisticsTemplate class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class SurvivalStatisticsTemplate
{
    /**
     * Prepare the message with the team's members
     *
     * @param game The game
     *
     * @return A list of lines
     */
    public List<String> prepare(SurvivalGame game)
    {
        List<SurvivalPlayer> killers = new ArrayList<>(new HashMap<>(game.getRegisteredGamePlayers()).values());
        List<SurvivalPlayer> damagers = new ArrayList<>(new HashMap<>(game.getRegisteredGamePlayers()).values());

        Collections.sort(killers, (o1, o2) -> Integer.compare(o1.getKills().size(), o2.getKills().size()));
        Collections.sort(damagers, (o1, o2) -> Double.compare(o1.getDamageReporter().getTotalPlayerDamages(), o2.getDamageReporter().getTotalPlayerDamages()));

        Collections.reverse(killers);
        Collections.reverse(damagers);

        List<String> finalLines = new ArrayList<>();
        finalLines.add(ChatUtils.getCenteredText(ChatColor.WHITE + "•" + ChatColor.BOLD + " Statistiques du jeu " + ChatColor.RESET + ChatColor.WHITE + "•"));
        finalLines.add("");
        finalLines.add(ChatUtils.getCenteredText(ChatColor.WHITE + "★ Classement des meurtres ★"));
        finalLines.add("");

        finalLines.add(ChatUtils.getCenteredText(ChatColor.GREEN + "1er" + ChatColor.GRAY + " - " + ChatColor.RESET + PlayerUtils.getFullyFormattedPlayerName(killers.get(0).getUUID()) + ChatColor.GRAY + " (" + killers.get(0).getKills().size() + ")"));
        finalLines.add(ChatUtils.getCenteredText(ChatColor.YELLOW + "2e" + ChatColor.GRAY + " - " + ChatColor.RESET + PlayerUtils.getFullyFormattedPlayerName(killers.get(1).getUUID()) + ChatColor.GRAY + " (" + killers.get(1).getKills().size() + ")"));

        if(killers.size() > 2)
            finalLines.add(ChatUtils.getCenteredText(ChatColor.RED + "3e" + ChatColor.GRAY + " - " + ChatColor.RESET + PlayerUtils.getFullyFormattedPlayerName(killers.get(2).getUUID()) + ChatColor.GRAY + " (" + killers.get(2).getKills().size() + ")"));

        finalLines.add("");
        finalLines.add("");
        finalLines.add(ChatUtils.getCenteredText(ChatColor.WHITE + "★ Classement des dégats ★"));
        finalLines.add("");

        finalLines.add(ChatUtils.getCenteredText(ChatColor.GREEN + "1er" + ChatColor.GRAY + " - " + ChatColor.RESET + PlayerUtils.getFullyFormattedPlayerName(damagers.get(0).getUUID()) + ChatColor.GRAY + " (" + damagers.get(0).getDamageReporter().getTotalPlayerDamages() + ")"));
        finalLines.add(ChatUtils.getCenteredText(ChatColor.YELLOW + "2e" + ChatColor.GRAY + " - " + ChatColor.RESET + PlayerUtils.getFullyFormattedPlayerName(damagers.get(1).getUUID()) + ChatColor.GRAY + " (" + damagers.get(1).getDamageReporter().getTotalPlayerDamages() + ")"));

        if(damagers.size() > 2)
            finalLines.add(ChatUtils.getCenteredText(ChatColor.RED + "3e" + ChatColor.GRAY + " - " + ChatColor.RESET + PlayerUtils.getFullyFormattedPlayerName(damagers.get(2).getUUID()) + ChatColor.GRAY + " (" + damagers.get(2).getDamageReporter().getTotalPlayerDamages() + ")"));

        finalLines.add("");

        return finalLines;
    }

    /**
     * Send the message with the team's members
     *
     * @param game The game
     */
    public void execute(SurvivalGame game)
    {
        new BasicMessageTemplate().execute(this.prepare(game));
    }
}
