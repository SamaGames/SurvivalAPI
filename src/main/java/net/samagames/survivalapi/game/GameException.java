package net.samagames.survivalapi.game;

import net.samagames.api.SamaGamesAPI;

public class GameException extends Exception
{
    public GameException(String message)
    {
        super(message);
    }

    @Override
    public void printStackTrace()
    {
        super.printStackTrace();
        ((SurvivalGame) SamaGamesAPI.get().getGameManager().getGame()).dump();
    }
}
