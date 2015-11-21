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
import java.util.logging.Logger;

public class LobbyPopulator
{
    private final Logger logger;
    private final File file;
    private EditSession editSession;

    public LobbyPopulator(JavaPlugin plugin)
    {
        this.logger = plugin.getLogger();
        this.file = new File(plugin.getDataFolder(), "lobby.schematic");
    }

    public void place()
    {
        this.logger.info("Generating lobby...");

        if (this.file.exists())
        {
            try
            {
                Vector vector = new Vector(0, 200, 0);

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
                ex.printStackTrace();
            }

        }
        else
        {
            this.logger.severe("File does not exist. Abort...");
        }

        this.logger.info("Done.");
    }

    public void remove()
    {
        this.editSession.undo(this.editSession);
    }
}
