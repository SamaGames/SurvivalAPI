package net.samagames.survivalapi.game;

import net.samagames.api.games.IGameStatisticsHelper;

import java.util.UUID;

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
public interface SurvivalGameStatisticsHelper extends IGameStatisticsHelper
{
    void increaseKills(UUID uuid);
    void increaseDeaths(UUID uuid);
    void increaseDamages(UUID uuid, double damages);
}
