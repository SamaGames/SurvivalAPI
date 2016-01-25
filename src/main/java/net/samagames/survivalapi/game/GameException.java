package net.samagames.survivalapi.game;

import net.samagames.api.SamaGamesAPI;

/**
 * GameException class
 *
 * Copyright (c) for SamaGames
 * All right reserved
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
