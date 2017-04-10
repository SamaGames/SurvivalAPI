package net.samagames.survivalapi;

import net.minecraft.server.v1_8_R3.*;
import net.samagames.survivalapi.games.AbstractGame;
import net.samagames.survivalapi.games.Game;
import net.samagames.survivalapi.gen.WorldLoader;
import net.samagames.survivalapi.utils.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SurvivalGenerator extends JavaPlugin
{
    private List<BiomeBase> biomesToRemove;
    private AbstractGame game;
    private BukkitTask startTimer;
    private boolean worldLoaded;
    private WorldLoader worldLoader;

    @Override
    public void onEnable()
    {
        this.saveDefaultConfig();
        this.biomesToRemove = new ArrayList<>();

        String gameRaw = this.getConfig().getString("game", "UHC");

        try
        {
            this.game = Game.valueOf(gameRaw).getGameClass().getConstructor(SurvivalGenerator.class).newInstance(this);

            this.patchBiomes();
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
        this.startTimer.cancel();
        this.worldLoaded = true;

        this.worldLoader = new WorldLoader(this, this.getConfig().getInt("size", 1000));
        this.worldLoader.begin(this.getServer().getWorld("world"));
    }

    public void finishGeneration(World world, long time)
    {
        this.getLogger().info("Ready in " + time + "ms");
        Bukkit.shutdown();
    }

    public void addBiomeToRemove(BiomeBase biomeBase)
    {
        this.biomesToRemove.add(biomeBase);
    }

    public boolean isWorldLoaded()
    {
        return this.worldLoaded;
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

        for (BiomeBase biomeBase : this.biomesToRemove)
            biomesMap.remove(biomeBase.ah);

        for (int i = 0; i < biomes.length; i++)
        {
            if (biomes[i] != null)
            {
                if (!biomesMap.containsKey(biomes[i].ah))
                    biomes[i] = defaultBiome;

                this.setReedsPerChunk(biomes[i], 64);
            }
        }

        Reflection.setFinalStatic(BiomeBase.class.getDeclaredField("biomes"), biomes);
    }

    public AbstractGame getGame()
    {
        return this.game;
    }

    public WorldLoader getWorldLoader()
    {
        return worldLoader;
    }

    private void setReedsPerChunk(BiomeBase biome, int value) throws NoSuchFieldException, IllegalAccessException
    {
        Reflection.setValue(biome.as, BiomeDecorator.class, true, "F", value);
    }
}
