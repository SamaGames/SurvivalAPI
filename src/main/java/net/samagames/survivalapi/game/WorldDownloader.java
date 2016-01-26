package net.samagames.survivalapi.game;

import com.google.gson.JsonElement;
import net.samagames.api.SamaGamesAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * WorldDownloader class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class WorldDownloader
{
    private SurvivalPlugin plugin;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     */
    public WorldDownloader(SurvivalPlugin plugin)
    {
        this.plugin = plugin;
    }

    /**
     * Download the world in a given destination folder
     *
     * @param worldDir Destination
     *
     * @return {@code true} if success or {@code false}
     */
    public boolean checkAndDownloadWorld(File worldDir)
    {
        SamaGamesAPI.get().getGameManager().getGameProperties().reload();

        File worldTar = new File(worldDir.getParentFile(), "world.tar.gz");
        JsonElement worldStorage = SamaGamesAPI.get().getGameManager().getGameProperties().getConfig("worldStorage", null);

        if (worldStorage == null)
        {
            plugin.getLogger().severe("worldStorage not defined");
            return false;
        }

        URL worldStorageURL;
        String mapID = "No file found";

        try
        {
            worldStorageURL = new URL(worldStorage.getAsString() + "get.php");
            BufferedReader in = new BufferedReader(new InputStreamReader(worldStorageURL.openStream(), "UTF-8"));
            mapID = in.readLine();
            in.close();

            this.plugin.getLogger().info(mapID);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }

        if (mapID.equals("No file found"))
        {
            if (worldTar.exists())
            {
                this.plugin.getLogger().warning("No map availaible but found world.zip in local, assuming to use it.");

                boolean result = this.extractWorld(worldTar, worldDir);

                try
                {
                    worldStorageURL = new URL(worldStorage.getAsString() + "clean.php?name=" + mapID);
                    URLConnection connection = worldStorageURL.openConnection();
                    connection.connect();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

                return result;
            }

            return false;
        }
        else if (worldTar.exists())
        {
            this.plugin.getLogger().warning("world.zip already exist! Is that a Hydro managed server?");

            if (!worldTar.delete())
            {
                this.plugin.getLogger().severe("Cannot remove world.tar.gz, this is a CRITICAL error!");
                return false;
            }
        }


        try
        {
            worldStorageURL = new URL(worldStorage.getAsString() + "download.php?name=" + mapID);

            ReadableByteChannel rbc = Channels.newChannel(worldStorageURL.openStream());
            FileOutputStream fos = new FileOutputStream(worldTar);

            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }

        try
        {
            worldStorageURL = new URL(worldStorage.getAsString() + "clean.php?name=" + mapID);

            URLConnection connection = worldStorageURL.openConnection();
            connection.connect();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return extractWorld(worldTar, worldDir);
    }

    /**
     * Extract the downloaded world in a given destination folder
     *
     * @param worldTar World archive
     * @param worldDir Destination
     *
     * @return {@code true} if success or {@code false}
     */
    private static boolean extractWorld(File worldTar, File worldDir)
    {
        Archiver archiver = ArchiverFactory.createArchiver("tar", "gz");

        try
        {
            archiver.extract(worldTar, worldDir.getParentFile());
            return true;
        }
        catch (IOException e)
        {
            return false;
        }
    }
}
