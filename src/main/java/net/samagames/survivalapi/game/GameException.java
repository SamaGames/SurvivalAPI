package net.samagames.survivalapi.game;

import net.samagames.api.SamaGamesAPI;

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
public class GameException extends Exception
{
    /**
     * Constructor
     *
     * @param message Exception message
     */
    public GameException(String message)
    {
        super(message);
    }

    /**
     * When the exception is printed, also dump the status of the game
     */
    @Override
    public void printStackTrace()
    {
        super.printStackTrace();
        ((SurvivalGame) SamaGamesAPI.get().getGameManager().getGame()).dump();
    }
}
