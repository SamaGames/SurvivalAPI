package net.samagames.survivalapi.game;

import net.samagames.api.games.IGameStatisticsHelper;

import java.util.UUID;

/**
 *               )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * This file is issued of the project SurvivalAPI
 * Created by Jérémy L. (BlueSlime) on 29/07/16
 */
public interface SurvivalGameStatisticsHelper extends IGameStatisticsHelper
{
    void increaseKills(UUID uuid);
    void increaseDeaths(UUID uuid);
    void increaseDamages(UUID uuid, double damages);
}
