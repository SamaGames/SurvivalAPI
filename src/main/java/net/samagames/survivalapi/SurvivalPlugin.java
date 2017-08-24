package net.samagames.survivalapi;

import com.google.gson.JsonPrimitive;
import net.samagames.api.SamaGamesAPI;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.game.WorldLoader;
import net.samagames.survivalapi.game.commands.CommandNextEvent;
import net.samagames.survivalapi.game.commands.CommandUHC;
import net.samagames.survivalapi.nms.NMSPatcher;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.logging.Level;

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
public class SurvivalPlugin extends JavaPlugin
{
    private SurvivalAPI api;
    private BukkitTask startTimer;
    private WorldLoader worldLoader;

    /**
     * Called on when plugin enables
     */
    @Override
    public void onEnable()
    {
        this.worldLoader = new WorldLoader(this, SamaGamesAPI.get().getGameManager().getGameProperties().getGameOption("size", new JsonPrimitive(1000)).getAsInt());
        this.api = new SurvivalAPI(this);

        try
        {
            NMSPatcher nmsPatcher = new NMSPatcher(this);
            nmsPatcher.patchBiomes();
            nmsPatcher.patchPotions();

            if (SamaGamesAPI.get().getGameManager().getGameProperties().getGameOption("patch-stackable", new JsonPrimitive(false)).getAsBoolean())
                nmsPatcher.patchStackable();
        }
        catch (Exception e)
        {
            this.getLogger().log(Level.SEVERE, "Error while patching NMS" , e);
        }

        this.getCommand("uhc").setExecutor(new CommandUHC());
        this.getCommand("nextevent").setExecutor(new CommandNextEvent());

        this.startTimer = this.getServer().getScheduler().runTaskTimer(this, this::postInit, 20L, 20L);
    }

    /**
     * Called the plugin disables
     */
    @Override
    public void onDisable()
    {
        if (SamaGamesAPI.get().getGameManager().getGame() != null)
            ((SurvivalGame) SamaGamesAPI.get().getGameManager().getGame()).dump();
    }

    /**
     * Called when the world's loading process is finished
     *
     * @param world World
     * @param time Loading time
     */
    public void finishGeneration(World world, long time)
    {
        this.getLogger().info("Ready in " + time + "ms");
        this.getServer().setSpawnRadius(0);
        this.api.fireEvents(SurvivalAPI.EventType.AFTERGENERATION);
    }

    /**
     * Called before the world generation, with that, we can use WorldEdit
     */
    private void postInit()
    {
        this.startTimer.cancel();
        this.api.fireEvents(SurvivalAPI.EventType.POSTINIT);
    }

    public SurvivalAPI getApi()
    {
        return api;
    }

    public WorldLoader getWorldLoader() {
        return worldLoader;
    }
}
