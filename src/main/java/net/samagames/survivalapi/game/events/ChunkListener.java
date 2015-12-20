package net.samagames.survivalapi.game.events;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChunkListener implements Runnable, Listener
{
    private final ConcurrentHashMap<Chunk, Long> lastChunkCleanUp;

    public ChunkListener()
    {
        this.lastChunkCleanUp = new ConcurrentHashMap<>();
    }

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

        event.setCancelled(true);
    }
}
