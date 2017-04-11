package net.samagames.survivalapi.games.uhcrandom;

import net.samagames.survivalapi.SurvivalGenerator;
import net.samagames.survivalapi.games.AbstractGame;
import net.samagames.survivalapi.gen.WorldGenMonumentPatched;
import org.bukkit.World;

public class UHCRandomGame extends AbstractGame
{
    public UHCRandomGame(SurvivalGenerator plugin)
    {
        super(plugin);
    }

    @Override
    public void preInit(){}

    @Override
    public void init(World world)
    {
        try
        {
            WorldGenMonumentPatched.load(world);
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
        }

        String gen = this.plugin.getConfig().getString("generator", null);

        if (gen == null)
            return;

        if (gen.equalsIgnoreCase("bigcrack"))
            world.getPopulators().add(new BigCrackPopulator());
        else if (gen.equalsIgnoreCase("chunkapocalypse"))
            world.getPopulators().add(new ChunkApocalypsePopulator());
        else
            return;

        this.plugin.getLogger().info("Added " + gen + " generator");
    }
}
