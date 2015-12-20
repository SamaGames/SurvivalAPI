package net.samagames.survivalapi.game.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;

import java.util.List;

public class OptimizationListener implements Listener
{
    private final int radius;

    public OptimizationListener()
    {
        this.radius = 2;
    }

    /**
     * Concentrate stacks
     *
     * @param event Event
     */
    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event)
    {
        if (event.getEntityType() != EntityType.DROPPED_ITEM)
            return;

        Item newEntity = event.getEntity();
        List<Entity> entityList = newEntity.getNearbyEntities(this.radius, 1, this.radius);
        int maxSize = newEntity.getItemStack().getMaxStackSize();

        for (Entity anEntityList : entityList)
        {
            if (anEntityList instanceof Item)
            {
                Item curEntity = (Item) anEntityList;

                if (!curEntity.isDead())
                {
                    if (curEntity.getItemStack().getType().equals(newEntity.getItemStack().getType()))
                    {
                        if (curEntity.getItemStack().getData().getData() == newEntity.getItemStack().getData().getData())
                        {
                            if (curEntity.getItemStack().getDurability() == newEntity.getItemStack().getDurability())
                            {
                                if (Math.abs(curEntity.getLocation().getX() - newEntity.getLocation().getX()) <= this.radius
                                        && Math.abs(curEntity.getLocation().getY() - newEntity.getLocation().getY()) <= this.radius
                                        && Math.abs(curEntity.getLocation().getZ() - newEntity.getLocation().getZ()) <= this.radius
                                )
                                {

                                    int newAmount = newEntity.getItemStack().getAmount();
                                    int curAmount = curEntity.getItemStack().getAmount();
                                    int more = Math.min(newAmount, maxSize - curAmount);

                                    curAmount += more;
                                    newAmount -= more;
                                    curEntity.getItemStack().setAmount(curAmount);
                                    newEntity.getItemStack().setAmount(newAmount);

                                    if (newAmount <= 0)
                                        event.setCancelled(true);

                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
