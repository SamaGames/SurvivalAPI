package net.samagames.survivalapi.games;

import net.samagames.survivalapi.SurvivalGenerator;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldSaveEvent;

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
public abstract class AbstractGame implements Listener
{
    protected final SurvivalGenerator plugin;

    public AbstractGame(SurvivalGenerator plugin)
    {
        this.plugin = plugin;
    }

    public abstract void preInit();
    public abstract void init(World world);

    public void onFinish(World world)
    {
        this.plugin.getWorldLoader().computeTop(world, () -> plugin.finishGeneration(world, System.currentTimeMillis() - this.plugin.getWorldLoader().getStartTime()));
    }

    public void onLoaded(World world) {}

    @EventHandler
    public void onWorldInit(final WorldInitEvent event)
    {
        if (event.getWorld().getEnvironment() == World.Environment.NORMAL)
            this.init(event.getWorld());
    }

    @EventHandler
    public void onWorldLoaded(final WorldSaveEvent event)
    {
        if (event.getWorld().getEnvironment() == World.Environment.NORMAL)
            this.onLoaded(event.getWorld());
    }
}
