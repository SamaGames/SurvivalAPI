package net.samagames.survivalapi.game.events;

import net.samagames.survivalapi.SurvivalAPI;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldLoadEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
public class ChunkListener implements Runnable, Listener
{
    private final ConcurrentHashMap<Chunk, Long> lastChunkCleanUp;

    /**
     * Constructor
     */
    public ChunkListener()
    {
        this.lastChunkCleanUp = new ConcurrentHashMap<>();
    }

    /**
     * Clean the cache
     */
    @Override
    public void run()
    {
        long currentTime = System.currentTimeMillis();

        List<Map.Entry<Chunk, Long>> temp = new ArrayList<>();
        temp.addAll(this.lastChunkCleanUp.entrySet());

        for (Map.Entry<Chunk, Long> entry : temp)
        {
            Chunk chunk = entry.getKey();

            if (!chunk.isLoaded() || (currentTime - entry.getValue() <= 60000))
                continue;

            for (Entity entity : chunk.getEntities())
                if (!(entity instanceof Item || entity instanceof HumanEntity || entity instanceof Minecart))
                    entity.remove();

            this.lastChunkCleanUp.remove(chunk);
        }
    }

    /**
     * Save unloaded chunk
     *
     * @param event Event
     */
    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event)
    {
        for (Entity entity : event.getChunk().getEntities())
            if (!(entity instanceof Item || entity instanceof HumanEntity || entity instanceof Minecart))
                entity.remove();

        //event.setCancelled(true);
    }

    /**
     * Listen when world loaded
     */

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event)
    {
        if (event.getWorld().equals(Bukkit.getWorlds().get(0)))
            SurvivalAPI.get().fireEvents(SurvivalAPI.EventType.WORLDLOADED);
    }
}
