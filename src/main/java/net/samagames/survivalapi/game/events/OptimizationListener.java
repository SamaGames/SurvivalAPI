package net.samagames.survivalapi.game.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;

import java.util.List;

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
public class OptimizationListener implements Listener
{
    private final int radius;

    /**
     * Constructor
     */
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

                if (!curEntity.isDead()
                        && curEntity.getItemStack().getType().equals(newEntity.getItemStack().getType())
                        && curEntity.getItemStack().getData().getData() == newEntity.getItemStack().getData().getData()
                        && curEntity.getItemStack().getDurability() == newEntity.getItemStack().getDurability()
                        && Math.abs(curEntity.getLocation().getX() - newEntity.getLocation().getX()) <= this.radius
                        && Math.abs(curEntity.getLocation().getY() - newEntity.getLocation().getY()) <= this.radius
                        && Math.abs(curEntity.getLocation().getZ() - newEntity.getLocation().getZ()) <= this.radius)
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
