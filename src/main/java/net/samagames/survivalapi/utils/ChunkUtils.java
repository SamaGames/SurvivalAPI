package net.samagames.survivalapi.utils;

import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;

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
public class ChunkUtils
{
    private ChunkUtils()
    {
    }

    /**
     * Send to a given players the chunks around a given radius
     *
     * @param p The player
     * @param dest The block of the chunk
     * @param radius The radius
     */
    public static void loadDestination(Player p, Location dest, int radius)
    {
        WorldServer handle = ((CraftWorld) dest.getWorld()).getHandle();

        //TODO fix chunk sending
        /*for (int x = -radius; x < radius; x++)
        {
            for (int z = -radius; z < radius; z++)
            {
                Chunk chunk = handle.getChunkAtWorldCoords(new BlockPosition(dest.getBlockX() + x, 0, dest.getBlockZ() + z));
                ((CraftPlayer)p).getHandle().playerConnection.sendPacket(new PacketPlayOutMapChunk(chunk, 0xFFFFFFFF));
            }
        }*/
    }
}
