package net.samagames.survivalapi.game.events;

import eu.carrade.amaury.SafePortals.SafePortalsUtils;
import net.samagames.survivalapi.game.SurvivalGame;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TravelAgent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerPortalEvent;

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
public class SecurityListener implements Listener
{
    private final SurvivalGame game;

    /**
     * Constructor
     *
     * @param game Game instance
     */
    public SecurityListener(SurvivalGame game)
    {
        this.game = game;
    }

    /**
     * Patch player teleporting through portals to be INSIDE the world border
     *
     * @param event Event
     */
    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event)
    {
        if (!this.game.getPlugin().getServer().getAllowNether() || this.game.getSurvivalGameLoop().isNetherClosed())
        {
            event.setCancelled(true);
            return;
        }
        TravelAgent travelAgent = event.getPortalTravelAgent();
        Location destination = travelAgent.findPortal(event.getTo());

        if (destination != null)
        {
            if (!SafePortalsUtils.isInsideBorder(destination))
            {
                event.useTravelAgent(false);
                boolean success = travelAgent.createPortal(event.getTo());

                if (success)
                {
                    event.setTo(travelAgent.findPortal(event.getTo()));

                    if (!SafePortalsUtils.isSafeSpot(event.getTo()))
                    {
                        Location safeTo = SafePortalsUtils.searchSafeSpot(event.getTo());
                        if (safeTo != null)
                        {
                            event.setTo(safeTo);
                        }
                    }
                }
            }
        }
        else
        {
            event.useTravelAgent(true);
        }
    }

    /**
     * Prevent portal trap
     *
     * @param event Event
     */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        if(!this.game.isGameStarted() || !this.game.hasPlayer(event.getPlayer()))
        {
            event.setCancelled(true);
            return;
        }

        boolean flag = false;

        if(event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation().add(1.0D, 0.0D, 0.0D)).getType() == Material.PORTAL)
            flag = true;

        if(event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation().subtract(1.0D, 0.0D, 0.0D)).getType() == Material.PORTAL)
            flag = true;

        if(event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation().add(0.0D, 0.0D, 1.0D)).getType() == Material.PORTAL)
            flag = true;

        if(event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation().subtract(0.0D, 0.0D, 1.0D)).getType() == Material.PORTAL)
            flag = true;

        if(flag)
        {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "Vous ne pouvez pas placer de bloc ici !");
        }
    }
}
