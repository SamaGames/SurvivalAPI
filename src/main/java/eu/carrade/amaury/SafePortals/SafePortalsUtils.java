package eu.carrade.amaury.SafePortals;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;


/**
 * An utility class to get informations about the vanilla WorldBorder, and to safely teleport players.
 *
 * @author AmauryCarrade
 */
public class SafePortalsUtils
{
    private SafePortalsUtils()
    {
    }

    /**
     * Checks if the given location is inside the world border of his world.
     *
     * @param location The location.
     *
     * @return The result of the check.
     */
    public static boolean isInsideBorder(Location location)
    {
        World world = location.getWorld();
        WorldBorder border = world.getWorldBorder();

        Double borderRadius = border.getSize() / 2;

        Double xMin = border.getCenter().getX() - borderRadius;
        Double xMax = border.getCenter().getX() + borderRadius;
        Double zMin = border.getCenter().getZ() - borderRadius;
        Double zMax = border.getCenter().getZ() + borderRadius;

        Double x = location.getX();
        Double z = location.getZ();

        return (x > xMin && x < xMax) && (z > zMin && z < zMax);
    }


    /**
     * Finds a safe spot where teleport the player, and teleport the player to that spot. If a spot
     * is not found, the player is not teleported, except if {@code force} is set to true.
     *
     * @param player   The teleported player.
     * @param location The location where this player should be teleported.
     * @param force    If true the player will be teleported to the exact given location if there is
     *                 no safe spot.
     *
     * @return true if the player was effectively teleported.
     */
    public static boolean safeTP(Player player, Location location, boolean force)
    {
        // If the target is safe, let's go
        if (isSafeSpot(location))
        {
            player.teleport(location);
            return true;
        }

        Location safeSpot = searchSafeSpot(location);

        // A spot was found, let's teleport.
        if (safeSpot != null)
        {
            player.teleport(safeSpot);
            return true;
        }

        // No spot found; the teleportation is cancelled if not forced.
        else
        {
            // If the teleportation is forced, let's go
            if (force)
            {
                player.teleport(location);
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    /**
     * Searches a safe spot where teleport the player, and teleport the player to that spot. If a
     * spot is not found, the player is not teleported.
     *
     * @param player   The teleported player.
     * @param location The location where this player should be teleported.
     *
     * @return true if the player was effectively teleported.
     */
    public static boolean safeTP(Player player, Location location)
    {
        return safeTP(player, location, false);
    }

    /**
     * Searches a safe spot in the given location.
     *
     * The spot is in the same X;Z coordinates.
     *
     * @param location The location where to find a safe spot.
     *
     * @return A Location object representing the safe spot, or null if no safe spot is available.
     */
    public static Location searchSafeSpot(Location location)
    {
        // We try to find a spot above or below the target

        Location safeSpot = null;
        final int maxHeight = (location.getWorld().getEnvironment() == World.Environment.NETHER) ? 125 : location.getWorld().getMaxHeight() - 2; // (thx to WorldBorder)

        for (int yGrow = location.getBlockY(), yDecr = location.getBlockY(); yDecr >= 1 || yGrow <= maxHeight; yDecr--, yGrow++)
        {
            // Above?
            if (yGrow < maxHeight)
            {
                Location spot = new Location(location.getWorld(), location.getBlockX(), yGrow, location.getBlockZ());
                if (isSafeSpot(spot))
                {
                    safeSpot = spot;
                    break;
                }
            }

            // Below?
            if (yDecr > 1 && yDecr != yGrow)
            {
                Location spot = new Location(location.getWorld(), location.getX(), yDecr, location.getZ());
                if (isSafeSpot(spot))
                {
                    safeSpot = spot;
                    break;
                }
            }
        }

        // A spot was found, we changes the pitch & yaw according to the original location.
        if (safeSpot != null)
        {
            safeSpot.setPitch(location.getPitch());
            safeSpot.setYaw(location.getYaw());
        }

        return safeSpot;
    }

    /**
     * Checks if a given location is safe. A safe location is a location with two breathable blocks
     * (aka transparent block or water) over something solid (or water).
     *
     * @param location The checked location.
     *
     * @return {@code true} if the location is safe.
     */
    public static boolean isSafeSpot(Location location)
    {
        Block blockCenter = location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        Block blockAbove = location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() + 1, location.getBlockZ());
        Block blockBelow = location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() - 1, location.getBlockZ());

        // two breathable blocks: ok
        if ((blockCenter.getType().isTransparent() || (blockCenter.isLiquid() && !blockCenter.getType().equals(Material.LAVA) && !blockCenter.getType().equals(Material.STATIONARY_LAVA)))
                && (blockAbove.getType().isTransparent() || (blockAbove.isLiquid() && !blockAbove.getType().equals(Material.LAVA) && !blockCenter.getType().equals(Material.STATIONARY_LAVA))))
        {
            // The block below is solid, or liquid (but not lava)
            return blockBelow.getType().isSolid() || blockBelow.getType().equals(Material.WATER) || blockBelow.getType().equals(Material.STATIONARY_WATER);
        }
        else
        {
            return false;
        }
    }
}