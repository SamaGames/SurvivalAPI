package SafePortals;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;


/**
 * An utility class to get informations about the vanilla WorldBorder.
 *
 * @author AmauryCarrade
 */
public class SafePortalsUtils
{
    public static boolean isInsideBorder(Location location)
    {
        if(location == null)
            return false;

        World world = location.getWorld();
        WorldBorder border = world.getWorldBorder();

        Double borderRadius = border.getSize() / 2;

        Double xMin = border.getCenter().getX() - borderRadius;
        Double xMax = border.getCenter().getX() + borderRadius;
        Double zMin = border.getCenter().getZ() - borderRadius;
        Double zMax = border.getCenter().getZ() + borderRadius;

        Double x = location.getX();
        Double z = location.getZ();

        return ((x > xMin && x < xMax) && (z > zMin && z < zMax));
    }

    public static boolean safeTP(Player player, Location location, boolean force)
    {
        if(isSafeSpot(location))
        {
            player.teleport(location);
            return true;
        }

        Location safeSpot = searchSafeSpot(location);

        if(safeSpot != null)
        {
            player.teleport(safeSpot);
            return true;
        }
        else
        {
            if(force)
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

    public static boolean safeTP(Player player, Location location)
    {
        return safeTP(player, location, false);
    }

    public static Location searchSafeSpot(Location location)
    {
        Location safeSpot = null;
        final int maxHeight = (location.getWorld().getEnvironment() == World.Environment.NETHER) ? 125 : location.getWorld().getMaxHeight() - 2; // (thx to WorldBorder)

        for(int yGrow = location.getBlockY(), yDecr = location.getBlockY(); yDecr >= 1 || yGrow <= maxHeight; yDecr--, yGrow++)
        {
            if(yGrow < maxHeight)
            {
                Location spot = new Location(location.getWorld(), location.getBlockX(), yGrow, location.getBlockZ());
                if(isSafeSpot(spot))
                {
                    safeSpot = spot;
                    break;
                }
            }

            if(yDecr > 1 && yDecr != yGrow)
            {
                Location spot = new Location(location.getWorld(), location.getX(), yDecr, location.getZ());

                if(isSafeSpot(spot))
                {
                    safeSpot = spot;
                    break;
                }
            }
        }

        if(safeSpot != null)
        {
            safeSpot.setPitch(location.getPitch());
            safeSpot.setYaw(location.getYaw());
        }

        return safeSpot;
    }

    public static boolean isSafeSpot(Location location)
    {
        Block blockCenter = location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        Block blockAbove = location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() + 1, location.getBlockZ());
        Block blockBelow = location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() - 1, location.getBlockZ());

        if((blockCenter.getType().isTransparent() || (blockCenter.isLiquid() && !blockCenter.getType().equals(Material.LAVA) && !blockCenter.getType().equals(Material.STATIONARY_LAVA))) && (blockAbove.getType().isTransparent() || (blockAbove.isLiquid() && !blockAbove.getType().equals(Material.LAVA) && !blockCenter.getType().equals(Material.STATIONARY_LAVA))))
        {
            if(blockBelow.getType().isSolid() || blockBelow.getType().equals(Material.WATER) || blockBelow.getType().equals(Material.STATIONARY_WATER))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }
}