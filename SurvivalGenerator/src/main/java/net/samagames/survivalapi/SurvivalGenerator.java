package net.samagames.survivalapi;

import net.minecraft.server.v1_8_R3.BiomeBase;
import net.minecraft.server.v1_8_R3.BiomeDecorator;
import net.samagames.survivalapi.games.AbstractGame;
import net.samagames.survivalapi.games.Game;
import net.samagames.tools.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;

public class SurvivalGenerator extends JavaPlugin
{
    private AbstractGame game;
    private BukkitTask startTimer;

    @Override
    public void onEnable()
    {
        this.saveDefaultConfig();

        String gameRaw = this.getConfig().getString("game", "UHC");

        try
        {
            this.patchBiomes();

            this.game = Game.valueOf(gameRaw).getGameClass().getConstructor(SurvivalGenerator.class).newInstance(this);
            this.game.preInit();

            this.getServer().getPluginManager().registerEvents(this.game, this);

            this.startTimer = getServer().getScheduler().runTaskTimer(this, this::postInit, 20L, 20L);
        }
        catch (ReflectiveOperationException e)
        {
            e.printStackTrace();
        }
    }

    public void postInit()
    {
        World world = getServer().getWorld("world");
        this.startTimer.cancel();

        WorldLoader worldLoader = new WorldLoader(this, this.getConfig().getInt("size", 1000));
        worldLoader.begin(world);
    }

    public void finishGeneration(World world, long time)
    {
        this.getLogger().info("Ready in " + time + "ms");
        Bukkit.shutdown();
    }

    private void patchBiomes() throws ReflectiveOperationException
    {
        BiomeBase[] biomes = BiomeBase.getBiomes();
        Map<String, BiomeBase> biomesMap = BiomeBase.o;
        BiomeBase defaultBiome = BiomeBase.FOREST;

        Reflection.setFinalStatic(BiomeBase.class.getDeclaredField("ad"), defaultBiome);

        biomesMap.remove(BiomeBase.OCEAN.ah);
        biomesMap.remove(BiomeBase.DEEP_OCEAN.ah);
        biomesMap.remove(BiomeBase.FROZEN_OCEAN.ah);

        this.setReedsPerChunk(BiomeBase.BEACH, 8);
        this.setReedsPerChunk(BiomeBase.STONE_BEACH, 8);

        for (int i = 0; i < biomes.length; i++)
        {
            if (biomes[i] != null)
            {
                if (!biomesMap.containsKey(biomes[i].ah))
                    biomes[i] = defaultBiome;

                this.setReedsPerChunk(biomes[i], (int) Reflection.getValue(biomes[i].as, BiomeDecorator.class, true, "F") * 2);
            }
        }

        Reflection.setFinalStatic(BiomeBase.class.getDeclaredField("biomes"), biomes);
    }

    private void setReedsPerChunk(BiomeBase biome, int value) throws NoSuchFieldException, IllegalAccessException
    {
        Reflection.setValue(biome.as, BiomeDecorator.class, true, "F", value);
    }
}
