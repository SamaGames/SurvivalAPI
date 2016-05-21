package net.samagames.survivalapi.games;

import net.samagames.survivalapi.games.doublerunner.DoubleRunnerGame;
import net.samagames.survivalapi.games.uhc.UHCGame;
import net.samagames.survivalapi.games.uhcrandom.UHCRandomGame;
import net.samagames.survivalapi.games.uhcrun.UHCRunGame;

public enum Game
{
    UHC(UHCGame.class),
    UHCRUN(UHCRunGame.class),
    UHCRANDOM(UHCRandomGame.class),
    DOUBLERUNNER(DoubleRunnerGame.class);

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
