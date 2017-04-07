package net.samagames.survivalapi.games.uhcrun;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.schematic.SchematicFormat;
import com.sk89q.worldedit.world.DataException;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.samagames.survivalapi.utils.StructurePieceTreasure;
import net.samagames.survivalapi.SurvivalGenerator;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftChest;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

public class FortressPopulator extends BlockPopulator
{
    private SurvivalGenerator plugin;
    private Logger logger;
    private com.sk89q.worldedit.world.World bukkitWorld;
    private CuboidClipboard netherHouse, netherFortress;
    private Random random;
    private EditSession es;
    private boolean isFortess;

    public FortressPopulator(SurvivalGenerator plugin)
    {
        this.plugin = plugin;
        this.logger = plugin.getLogger();

        try
        {
            this.netherHouse = SchematicFormat.MCEDIT.load(new File(plugin.getDataFolder(), "/uhcrun_nether_1.schematic"));
            this.netherFortress = SchematicFormat.MCEDIT.load(new File(plugin.getDataFolder(), "/uhcrun_nether_2.schematic"));
        }
        catch (IOException | DataException e)
        {
            e.printStackTrace();
        }

        this.random = new Random();
    }

    @Override
    public void populate(World world, Random random, Chunk chunk)
    {
        if (this.bukkitWorld == null)
        {
            this.bukkitWorld = new BukkitWorld(world);
            this.es = WorldEdit.getInstance().getEditSessionFactory().getEditSession(this.bukkitWorld, -1);
            this.es.setFastMode(false);
        }

        if (MathHelper.nextInt(random, 0, 100) == 0)
        {
            int xFortress = chunk.getX() * 16 + random.nextInt(15);
            int zFortress = chunk.getZ() * 16 + random.nextInt(15);

            this.generateBlazeFortress(world, xFortress, zFortress);
        }
    }

    private void generateBlazeFortress(World world, int x, int z)
    {
        if (!this.plugin.isWorldLoaded())
            return;

        this.isFortess = !this.isFortess;
        this.generateCuboid(this.isFortess ? this.netherFortress : this.netherHouse, world, x, z);
    }

    private void generateCuboid(CuboidClipboard cuboidClipboard, World world, int x, int z)
    {
        if (cuboidClipboard != null)
        {
            this.logger.info("Generating fortress at " + x + "; " + z);

            try
            {
                Vector v = new Vector(x, 21, z);
                Chunk chunk = world.getChunkAt(new org.bukkit.Location(world, x, 21, z));

                int chunkX = chunk.getX();
                int chunkZ = chunk.getZ();

                chunk.load(true);

                int cx = chunkX - 3;
                int cz = chunkZ - 3;

                while (cx < chunkX + 3)
                {
                    while (cz < chunkZ + 3)
                    {
                        if (cx != chunkX || cz != chunkZ)
                            world.getChunkAt(cx, cz).load(true);

                        cz++;
                    }

                    cx++;
                }

                world.getChunkAt(chunkX - 1, chunkZ + 1).load(true);
                world.getChunkAt(chunkX - 1, chunkZ - 1).load(true);
                world.getChunkAt(chunkX - 1, chunkZ).load(true);
                world.getChunkAt(chunkX + 1, chunkZ + 1).load(true);
                world.getChunkAt(chunkX + 1, chunkZ - 1).load(true);
                world.getChunkAt(chunkX + 1, chunkZ).load(true);
                world.getChunkAt(chunkX, chunkZ + 1).load(true);
                world.getChunkAt(chunkX, chunkZ - 1).load(true);

                cuboidClipboard.paste(this.es, v, false);

                int bx = x - (cuboidClipboard.getWidth() / 2);
                int maxX = x + (cuboidClipboard.getWidth() / 2) + 1;

                while (bx < maxX)
                {
                    int bz = z - (cuboidClipboard.getLength() / 2);
                    int maxZ = z + (cuboidClipboard.getLength() / 2) + 1;

                    while (bz < maxZ)
                    {
                        int by = 42;

                        while (by > 0)
                        {
                            Block block = new org.bukkit.Location(world, bx, by, bz).getBlock();

                            if (block.getType() == Material.MOB_SPAWNER)
                            {
                                block.setType(Material.STONE);
                                block.setType(Material.MOB_SPAWNER);
                                CreatureSpawner spawner = (CreatureSpawner) block.getState();
                                spawner.setSpawnedType(EntityType.BLAZE);
                                spawner.setDelay(1);
                                spawner.update();

                                this.logger.info("Spawner configured at " + bx + "; " + by + "; " + bz);
                            }

                            by--;
                        }

                        bz++;
                    }

                    bx++;
                }
            }
            catch (MaxChangedBlocksException ex)
            {
                ex.printStackTrace();
            }
        }
    }
}
