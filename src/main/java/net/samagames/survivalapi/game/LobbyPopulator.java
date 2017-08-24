package net.samagames.survivalapi.game;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.schematic.SchematicFormat;
import com.sk89q.worldedit.world.DataException;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
public class LobbyPopulator
{
    private final Logger logger;
    private final File file;
    private EditSession editSession;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     */
    public LobbyPopulator(JavaPlugin plugin)
    {
        this.logger = plugin.getLogger();
        this.file = new File(plugin.getDataFolder(), "lobby.schematic");
    }

    /**
     * Place the waiting lobby in the world
     */
    public void place()
    {
        this.logger.info("Generating lobby...");

        if (this.file.exists())
        {
            try
            {
                Vector vector = new Vector(0, 190, 0);

                World world = Bukkit.getWorld("world");
                world.loadChunk(0, 0);

                BukkitWorld bwf = new BukkitWorld(world);

                this.editSession = new EditSession(bwf, -1);
                this.editSession.setFastMode(true);

                CuboidClipboard c1 = SchematicFormat.MCEDIT.load(this.file);
                c1.paste(this.editSession, vector, true);
            }
            catch (MaxChangedBlocksException | IOException | DataException ex)
            {
                this.logger.log(Level.SEVERE, "Error in world", ex);
            }

        }
        else
        {
            this.logger.severe("File does not exist. Abort...");
        }

        this.logger.info("Done.");
    }

    /**
     * Remove the waiting lobby from the world
     */
    public void remove()
    {
        this.editSession.undo(this.editSession);
    }
}
