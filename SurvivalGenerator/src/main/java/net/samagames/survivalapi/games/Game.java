package net.samagames.survivalapi.games;

import net.samagames.survivalapi.games.doublerunner.DoubleRunnerGame;
import net.samagames.survivalapi.games.uhc.UHCGame;
import net.samagames.survivalapi.games.uhcrandom.UHCRandomGame;
import net.samagames.survivalapi.games.uhcrun.UHCRunGame;
import net.samagames.survivalapi.games.randomrun.RandomRunGame;
import net.samagames.survivalapi.games.ultraflagkeeper.UltraFlagKeeperGame;

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
public enum Game
{
    UHC(UHCGame.class),
    UHCRUN(UHCRunGame.class),
    UHCRANDOM(UHCRandomGame.class),
    DOUBLERUNNER(DoubleRunnerGame.class),
    RANDOMRUN(RandomRunGame.class),
    ULTRAFLAGKEEPER(UltraFlagKeeperGame.class);

    private Class<? extends AbstractGame> gameClass;

    Game(Class<? extends AbstractGame> gameClass)
    {
        this.gameClass = gameClass;
    }

    public Class<? extends AbstractGame> getGameClass()
    {
        return this.gameClass;
    }
}
