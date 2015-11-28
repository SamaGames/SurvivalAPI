package net.samagames.survivalapi.game.events;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChunkListener implements Runnable, Listener
{
    private ConcurrentHashMap<Chunk, Long> lastChunkCleanUp;

    public ChunkListener(JavaPlugin plugin)
    {
        this.lastChunkCleanUp = new ConcurrentHashMap<>();

        //plugin.getServer().getScheduler().runTaskTimer(plugin, this, 20, 200);
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

            lastChunkCleanUp.remove(chunk);
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
        /*if (!this.lastChunkCleanUp.containsKey(event.getChunk()))
            this.lastChunkCleanUp.put(event.getChunk(), System.currentTimeMillis());*/

        for (Entity entity : event.getChunk().getEntities())
            if (!(entity instanceof Item || entity instanceof HumanEntity || entity instanceof Minecart))
                entity.remove();

        event.setCancelled(true);
    }

    private boolean containPlayer(Chunk chunk)
    {
        for (Entity entity : chunk.getEntities())
            if (entity instanceof HumanEntity)
                return true;

        return false;
    }
}
