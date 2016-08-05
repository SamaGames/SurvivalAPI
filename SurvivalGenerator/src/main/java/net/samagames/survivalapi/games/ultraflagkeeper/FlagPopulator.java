package net.samagames.survivalapi.games.ultraflagkeeper;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.schematic.SchematicFormat;
import net.samagames.survivalapi.SurvivalGenerator;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rigner for project SurvivalGenerator.
 */
public class FlagPopulator
{
    private SurvivalGenerator plugin;
    private CuboidClipboard flag;
    private List<Pair<Location, Byte>> locations;
    private EditSession es;
    private boolean done;

    FlagPopulator(SurvivalGenerator plugin)
    {
        this.plugin = plugin;
        this.locations = new ArrayList<>();
        this.done = false;

        try
        {
            this.flag = SchematicFormat.MCEDIT.load(new File(plugin.getDataFolder(), "/ufk_flag.schematic"));

            List<String> locs = this.plugin.getConfig().getStringList("flags");
            locs.forEach(string ->
            {
                String[] split = string.split(", ");
                this.locations.add(Pair.of(new Location(this.plugin.getServer().getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3])), Byte.parseByte(split[4])));
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void populate(World world)
    {
        if (this.done)
            return ;
        this.done = true;
        this.es = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(world), -1);
        this.es.setFastMode(false);
        this.locations.forEach(locationShortPair ->
        {
            try
            {
                int x = locationShortPair.getLeft().getBlockX();
                int y = locationShortPair.getLeft().getWorld().getHighestBlockYAt(locationShortPair.getLeft());
                int z = locationShortPair.getLeft().getBlockZ();

                this.plugin.getLogger().info("Removing trees at " + x + "; " + y + "; " + z);

                for (int i = x - 10; i < x + 10; i++)
                    for (int j = y - 20; j < y + 30; j++)
                        for (int k = z - 10; k < z + 10; k++)
                        {
                            Block block = world.getBlockAt(i, j, k);
                            if (block.getType() == Material.LOG || block.getType() == Material.LOG_2 || block.getType() == Material.LEAVES || block.getType() == Material.LEAVES_2)
                                block.setType(Material.AIR);
                        }

                y = locationShortPair.getLeft().getWorld().getHighestBlockYAt(locationShortPair.getLeft());

                this.plugin.getLogger().info("Generating flag at " + x + "; " + y + "; " + z + " with color " + locationShortPair.getRight());

                Chunk chunk_ = world.getChunkAt(new Location(world, x, y, z));
                chunk_.load(true);

                int chunkX = chunk_.getX();
                int chunkZ = chunk_.getZ();
                world.getChunkAt(chunkX - 1, chunkZ + 1).load(true);
                world.getChunkAt(chunkX - 1, chunkZ - 1).load(true);
                world.getChunkAt(chunkX - 1, chunkZ).load(true);
                world.getChunkAt(chunkX + 1, chunkZ + 1).load(true);
                world.getChunkAt(chunkX + 1, chunkZ - 1).load(true);
                world.getChunkAt(chunkX + 1, chunkZ).load(true);
                world.getChunkAt(chunkX, chunkZ + 1).load(true);
                world.getChunkAt(chunkX, chunkZ - 1).load(true);

                this.flag.paste(this.es, new Vector(x, y, z), true);
                this.flag.paste(this.es, new Vector(x, y, z), true);
                this.flag.paste(this.es, new Vector(x, y, z), true);

                int bx = x - (this.flag.getWidth() / 2);
                int maxX = x + (this.flag.getWidth() / 2) + 1;

                while (bx < maxX)
                {
                    int bz = z - (this.flag.getLength() / 2);
                    int maxZ = z + (this.flag.getLength() / 2) + 1;

                    while (bz < maxZ)
                    {
                        int by = y;
                        int maxY = y + this.flag.getHeight();

                        while (by < maxY)
                        {
                            Block block = new Location(world, bx, by, bz).getBlock();

                            if (block.getType() == Material.WOOL)
                            {
                                block.setData(locationShortPair.getRight());

                                this.plugin.getLogger().info("Flag set at " + bx + "; " + by + "; " + bz + " with color " + locationShortPair.getRight());
                            }

                            by++;
                        }

                        bz++;
                    }

                    bx++;
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        });
    }
}
