package net.samagames.survivalapi.game;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.SpawnerCreature;
import net.samagames.api.SamaGamesAPI;
import net.samagames.api.games.Game;
import net.samagames.api.games.GamePlayer;
import net.samagames.api.games.Status;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.game.commands.CommandNextEvent;
import net.samagames.survivalapi.game.commands.CommandUHC;
import net.samagames.survivalapi.game.events.*;
import net.samagames.survivalapi.game.types.SurvivalTeamGame;
import net.samagames.tools.Titles;
import net.samagames.tools.scoreboards.ObjectiveSign;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * SurvivalGame class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public abstract class SurvivalGame<SURVIVALLOOP extends SurvivalGameLoop> extends Game<SurvivalPlayer>
{
    protected final JavaPlugin plugin;
    protected final Server server;
    protected final String magicSymbol;
    protected final Class<? extends SURVIVALLOOP> survivalGameLoopClass;
    protected final List<Location> spawns;
    protected final List<WaitingBlock> waitingBlocks;
    protected final World world;

    protected LobbyPopulator lobbyPopulator;
    protected Location lobbySpawnLocation;
    protected SURVIVALLOOP gameLoop;
    protected Scoreboard scoreboard;
    protected BukkitTask mainTask;
    protected WorldBorder worldBorder;
    protected boolean damagesActivated;
    protected boolean pvpActivated;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param gameCodeName Game code name
     * @param gameName Game name
     * @param gameDescription Game description
     * @param magicSymbol Symbol into the scoreboard
     * @param survivalGameLoopClass Class of the game loop
     */
    public SurvivalGame(JavaPlugin plugin, String gameCodeName, String gameName, String gameDescription, String magicSymbol, Class<? extends SURVIVALLOOP> survivalGameLoopClass)
    {
        super(gameCodeName, gameName, gameDescription, SurvivalPlayer.class);

        plugin.saveResource("lobby.schematic", false);

        this.plugin = plugin;
        this.server = plugin.getServer();
        this.magicSymbol = magicSymbol;
        this.survivalGameLoopClass = survivalGameLoopClass;

        this.spawns = new ArrayList<>();
        this.waitingBlocks = new ArrayList<>();
        this.world = this.server.getWorlds().get(0);

        this.gameLoop = null;
        this.scoreboard = null;
        this.mainTask = null;
        this.damagesActivated = false;
        this.pvpActivated = false;

        this.worldBorder = this.world.getWorldBorder();
        this.worldBorder.setCenter(0D, 0D);
        this.worldBorder.setSize(1000);
        this.worldBorder.setWarningDistance(20);
        this.worldBorder.setWarningTime(0);
        this.worldBorder.setDamageBuffer(3D);
        this.worldBorder.setDamageAmount(2D);

        this.server.getPluginManager().registerEvents(new ChunkListener(), plugin);
        this.server.getPluginManager().registerEvents(new NaturalListener(), plugin);
        this.server.getPluginManager().registerEvents(new OptimizationListener(), plugin);
        this.server.getPluginManager().registerEvents(new SpectatorListener(this), plugin);
        this.server.getPluginManager().registerEvents(new SecurityListener(this), plugin);
        this.server.getPluginManager().registerEvents(new GameListener(this), plugin);

        for (World serverWorld : plugin.getServer().getWorlds())
        {
            serverWorld.setDifficulty(Difficulty.NORMAL);
            serverWorld.setGameRuleValue("doDaylightCycle", "false");
            serverWorld.setTime(2000L);
        }

        SamaGamesAPI.get().getGameManager().setMaxReconnectTime(this.gameManager.getGameProperties().getOption("reconnectTime", new JsonPrimitive(5)).getAsInt());

        CommandUHC.setGame(this);
        CommandNextEvent.setGame(this);
        SurvivalPlayer.setGame(this);

        this.scoreboard = this.server.getScoreboardManager().getMainScoreboard();

        SurvivalAPI.get().registerEvent(SurvivalAPI.EventType.AFTERGENERATION, () ->
        {
            try
            {
                this.lobbyPopulator = new LobbyPopulator(this.plugin);
                this.lobbyPopulator.place();

                JsonArray defaults = new JsonArray();
                defaults.add(new JsonPrimitive(0.5D));
                defaults.add(new JsonPrimitive(200.0D));
                defaults.add(new JsonPrimitive(0.5D));
                defaults.add(new JsonPrimitive(45.0F));
                defaults.add(new JsonPrimitive(0.0F));

                JsonArray spawnPos = this.gameManager.getGameProperties().getOption("spawnPos", defaults).getAsJsonArray();

                this.lobbySpawnLocation = new Location(this.world, spawnPos.get(0).getAsDouble(), spawnPos.get(1).getAsDouble(), spawnPos.get(2).getAsDouble(), spawnPos.get(3).getAsFloat(), spawnPos.get(4).getAsFloat());
                this.world.setSpawnLocation(this.lobbySpawnLocation.getBlockX(), this.lobbySpawnLocation.getBlockY(), this.lobbySpawnLocation.getBlockZ());

                this.gameLoop = this.survivalGameLoopClass.getConstructor(JavaPlugin.class, Server.class, SurvivalGame.class).newInstance(this.plugin, this.server, this);

                this.computeLocations();

                SpawnerCreature spawner = new SpawnerCreature();

                for (int i = 0; i < 2; i++)
                    spawner.a(MinecraftServer.getServer().getWorldServer(0), false, true, true);

                this.setStatus(Status.WAITING_FOR_PLAYERS);
            }
            catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
            {
                e.printStackTrace();
            }
        });
    }

    /**
     * Teleport the players to their spawn location
     */
    public abstract void teleport();

    /**
     * Check the status of the game after a kill or a disconnection
     *
     * @param playerUUID Player
     * @param silent Display messages
     *
     * @throws GameException
     */
    public abstract void checkStump(UUID playerUUID, boolean silent) throws GameException;

    /**
     * Set SamaGamesAPI
     */
    @Override
    public void handlePostRegistration()
    {
        super.handlePostRegistration();
        this.setStatus(Status.STARTING);
    }

    /**
     * Set SamaGamesAPI
     */
    @Override
    public void handleGameEnd()
    {
        this.dump();

        this.server.getScheduler().runTaskLater(this.plugin, () -> this.mainTask.cancel(), 20L);

        super.handleGameEnd();
    }

    /**
     * Set SamaGamesAPI
     */
    @Override
    public void handleReconnectTimeOut(OfflinePlayer player, boolean silent)
    {
        super.handleReconnectTimeOut(player, silent);

        try
        {
            this.stumpPlayer(player.getUniqueId(), true, true);
        }
        catch (GameException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Set SamaGamesAPI
     */
    @Override
    public void handleModeratorLogin(Player player)
    {
        super.handleModeratorLogin(player);
        this.rejoinPlayer(player);
    }

    /**
     * Set SamaGamesAPI
     */
    @Override
    public void startGame()
    {
        super.startGame();

        SurvivalAPI.get().fireGameStart(this);

        this.lobbyPopulator.remove();

        Objective displayNameLife = this.scoreboard.registerNewObjective("vie", "health");
        Objective playerListLife = this.scoreboard.registerNewObjective("vieb", "health");

        displayNameLife.setDisplayName(ChatColor.RED + "❤");
        displayNameLife.setDisplaySlot(DisplaySlot.BELOW_NAME);
        playerListLife.setDisplayName(ChatColor.RED + "❤");
        playerListLife.setDisplaySlot(DisplaySlot.PLAYER_LIST);

        this.mainTask = this.server.getScheduler().runTaskTimer(this.plugin, this.gameLoop, 20, 20);
        this.teleport();

        for (UUID uuid : this.getInGamePlayers().keySet())
        {
            Player player = this.server.getPlayer(uuid);

            if (player == null)
            {
                this.gamePlayers.remove(uuid);
                continue;
            }

            player.setGameMode(GameMode.SURVIVAL);
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(50);
            player.setExhaustion(0.0F);
            player.setScoreboard(this.scoreboard);
            player.setLevel(0);
            player.getInventory().clear();
            player.setAllowFlight(true);

            this.server.getScheduler().runTaskLater(this.plugin, () -> player.setAllowFlight(false), 20L * 5);

            displayNameLife.getScore(player.getName()).setScore((int) player.getHealth());
            playerListLife.getScore(player.getName()).setScore((int) player.getHealth());

            ObjectiveSign sign = new ObjectiveSign("sggameloop", ChatColor.DARK_AQUA + "" + (this.magicSymbol != null ? this.magicSymbol + " " : "") + ChatColor.BOLD + this.getGameName() + ChatColor.RESET + ChatColor.DARK_AQUA + (this.magicSymbol != null ? " " + this.magicSymbol : ""));
            sign.addReceiver(player);

            this.gameLoop.addPlayer(player.getUniqueId(), sign);
        }
    }

    /**
     * Dump the status of the game into the console
     */
    public void dump()
    {
        Logger logger = this.plugin.getLogger();

        logger.severe("==================[ GAME DUMP ]==================");
        logger.severe("|> Server name: " + SamaGamesAPI.get().getServerName());
        logger.severe("|> Connected players (" + this.getConnectedPlayers() + "):");

        for (GamePlayer gamePlayer : this.getInGamePlayers().values())
            logger.severe("   > " + gamePlayer.getOfflinePlayer().getName());

        if (this instanceof SurvivalTeamGame)
        {
            logger.severe("|> Teams (" + ((SurvivalTeamGame) this).countAliveTeam() + "):");

            for (SurvivalTeam team : ((SurvivalTeamGame) this).getTeams())
            {
                logger.severe("   > Team " + team.getChatColor().name() + " (" + team.getAlivePlayers() + "):");

                for (UUID uuid : team.getPlayersUUID().keySet())
                {
                    logger.severe("      > " + Bukkit.getOfflinePlayer(uuid).getName() + " - " + (team.getPlayersUUID().get(uuid) ? "Dead" : "Alive"));
                }
            }
        }

        logger.severe("|> Spectators (" + this.getSpectatorPlayers().size() + "):");

        for (GamePlayer gamePlayer : this.getSpectatorPlayers().values())
            logger.severe("   > " + gamePlayer.getOfflinePlayer().getName());

        logger.severe("|> Damages: " + this.damagesActivated);
        logger.severe("|> PVP: " + this.pvpActivated);
        logger.severe("|> World borders: " + this.getWorldBorder().getSize());

        logger.severe("==================[ GAME DUMP ]==================");
    }

    /**
     * Enable the damages in the game
     */
    public void enableDamages()
    {
        this.damagesActivated = true;
    }

    /**
     * Enable the PvP in the game
     */
    public void enablePVP()
    {
        this.pvpActivated = true;
    }

    /**
     * Disable the damages in the game
     */
    public void disableDamages()
    {
        this.damagesActivated = false;
    }

    /**
     * Disable the PvP in the game
     */
    public void disablePVP()
    {
        this.pvpActivated = false;
    }

    /**
     * Add back the scoreboard to a given reconnected player
     *
     * @param player Player
     */
    public void rejoinPlayer(Player player)
    {
        if (player != null)
        {
            this.server.getScheduler().runTaskLater(this.plugin, () ->
            {
                player.setScoreboard(this.scoreboard);

                ObjectiveSign sign = new ObjectiveSign("sggameloop", ChatColor.DARK_AQUA + "" + ChatColor.BOLD + (this.magicSymbol != null ? this.magicSymbol + " " : "") + this.getGameName() + (this.magicSymbol != null ? " " + this.magicSymbol : ""));
                sign.addReceiver(player);

                this.gameLoop.addPlayer(player.getUniqueId(), sign);
            }, 10L);
        }
    }

    /**
     * Check the death procedure of a given player
     *
     * @param playerUUID Player
     * @param logout Is disconnection death
     * @param silent Display messages
     *
     * @throws GameException
     */
    public void stumpPlayer(UUID playerUUID, boolean logout, boolean silent) throws GameException
    {
        try
        {
            if (this.status == Status.IN_GAME)
            {
                if (!logout)
                {
                    Player player = Bukkit.getPlayer(playerUUID);
                    MetadataValue lastDamager = player.hasMetadata("lastDamager") ? player.getMetadata("lastDamager").get(0) : null;
                    Player killer = null;

                    if (lastDamager != null)
                    {
                        if (lastDamager.value() instanceof Player)
                        {
                            killer = (Player) lastDamager.value();

                            if(killer == null)
                                killer = player.getKiller();

                            if (!killer.isOnline() || !this.gamePlayers.containsKey(player.getUniqueId()) || this.gamePlayers.get(player.getUniqueId()).isSpectator())
                                killer = null;
                        }
                        else if (player.hasMetadata("lastDamagerKeepingValue"))
                        {
                            killer = (Player) player.getMetadata("lastDamagerKeepingValue").get(0).value();

                            ((BukkitTask) player.getMetadata("lastDamagerKeeping").get(0).value()).cancel();
                            player.removeMetadata("lastDamagerKeeping", this.plugin);
                        }
                    }

                    if (killer != null)
                    {
                        if (killer.isOnline() && this.gamePlayers.containsKey(player.getUniqueId()) && !this.gamePlayers.get(player.getUniqueId()).isSpectator())
                        {
                            final Player finalKiller = killer;

                            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () ->
                            {
                                SurvivalPlayer gamePlayer = this.getPlayer(finalKiller.getUniqueId());
                                gamePlayer.addKill(player.getUniqueId());
                                gamePlayer.addCoins(20, "Meurtre de " + player.getName());

                                this.increaseStat(finalKiller.getUniqueId(), "kills", 1);
                            });

                            killer.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 400, 1));
                        }

                        String message;

                        if (this instanceof SurvivalTeamGame)
                            message = this.getPlayer(player.getUniqueId()).getTeam().getChatColor() + player.getName() + ChatColor.YELLOW + " a été tué par " + this.getPlayer(killer.getUniqueId()).getTeam().getChatColor() + killer.getName();
                        else
                            message = player.getDisplayName() + ChatColor.YELLOW + " a été tué par " + killer.getDisplayName();

                        this.coherenceMachine.getMessageManager().writeCustomMessage(message, true);
                    }
                    else
                    {
                        String message;

                        if (this instanceof SurvivalTeamGame)
                            message = this.getPlayer(player.getUniqueId()).getTeam().getChatColor() + player.getName();
                        else
                            message = player.getDisplayName();

                        message += " " + ChatColor.YELLOW;

                        switch (player.getLastDamageCause().getCause())
                        {
                            case FALL:
                            case FALLING_BLOCK:
                                message += "est mort de chute.";
                                break;

                            case FIRE:
                            case FIRE_TICK:
                                message += "a fini carbonisé.";
                                break;

                            case DROWNING:
                                message += "s'est noyé.";
                                break;

                            case LAVA:
                                message += "a essayé de nager dans la lave. Résultat peu concluant.";
                                break;

                            case SUFFOCATION:
                                message += "a essayé de se cacher dans un mur.";
                                break;

                            case BLOCK_EXPLOSION:
                            case ENTITY_EXPLOSION:
                                message += "a mangé un pétard. Allez savoir pourquoi.";
                                break;

                            case POISON:
                            case MAGIC:
                                message += "a s'est confronté à meilleur sorcier que lui.";
                                break;

                            case LIGHTNING:
                                message += "s'est transformé en Pikachu !";
                                break;

                            default:
                                message += "est mort.";
                                break;
                        }


                        this.coherenceMachine.getMessageManager().writeCustomMessage(message, true);

                        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> increaseStat(player.getUniqueId(), "deaths", 1));

                        Titles.sendTitle(player, 0, 100, 5, ChatColor.RED + "✞", ChatColor.RED + "Vous êtes mort !");
                        player.setGameMode(GameMode.SPECTATOR);
                        player.setHealth(20.0D);
                    }
                }
                else
                {
                    this.coherenceMachine.getMessageManager().writePlayerReconnectTimeOut(Bukkit.getOfflinePlayer(playerUUID));
                }

                this.plugin.getLogger().info("Stumping player " + playerUUID.toString() + "...");
                this.checkStump(playerUUID, silent);

                this.plugin.getLogger().info("Removing player " + playerUUID.toString() + "...");
                this.removeFromGame(playerUUID);

                this.dump();
            }
        }
        catch (NullPointerException | IllegalStateException e)
        {
            throw new GameException(e.getMessage());
        }
    }

    /**
     * Remove a given player from the game
     *
     * @param uuid Player's UUID
     */
    public void removeFromGame(UUID uuid)
    {
        SurvivalPlayer player = this.gamePlayers.get(uuid);

        if (player != null)
        {
            player.setSpectator();
        }
        else
        {
            this.plugin.getLogger().severe("Can't set spectator mode to " + uuid.toString() + "!");
        }
    }

    /**
     * Remove the waiting blocks
     */
    public void removeWaitingBlocks()
    {
        this.waitingBlocks.forEach(WaitingBlock::remove);
    }

    /**
     * Register spawn locations
     */
    public void computeLocations()
    {
        this.spawns.add(new Location(this.world, 0, 150, 200));
        this.spawns.add(new Location(this.world, 0, 150, 400));
        this.spawns.add(new Location(this.world, 200, 150, 0));
        this.spawns.add(new Location(this.world, 400, 150, 0));
        this.spawns.add(new Location(this.world, 400, 150, 200));
        this.spawns.add(new Location(this.world, 200, 150, 400));
        this.spawns.add(new Location(this.world, 400, 150, 400));
        this.spawns.add(new Location(this.world, 200, 150, 200));
        this.spawns.add(new Location(this.world, 0, 150, -200));
        this.spawns.add(new Location(this.world, 0, 150, -400));
        this.spawns.add(new Location(this.world, -200, 150, 0));
        this.spawns.add(new Location(this.world, -400, 150, 0));
        this.spawns.add(new Location(this.world, -400, 150, -200));
        this.spawns.add(new Location(this.world, -200, 150, -400));
        this.spawns.add(new Location(this.world, -400, 150, -400));
        this.spawns.add(new Location(this.world, -200, 150, -200));
        this.spawns.add(new Location(this.world, 400, 150, -200));
        this.spawns.add(new Location(this.world, -400, 150, 200));
        this.spawns.add(new Location(this.world, 200, 150, -400));
        this.spawns.add(new Location(this.world, -200, 150, 400));
        this.spawns.add(new Location(this.world, 400, 150, -400));
        this.spawns.add(new Location(this.world, 200, 150, -200));
        this.spawns.add(new Location(this.world, -200, 150, 200));
        this.spawns.add(new Location(this.world, -400, 150, 400));

        Collections.shuffle(this.spawns);

        //this.waitingBlocks.addAll(this.spawns.stream().map(WaitingBlock::new).collect(Collectors.toList()));
    }

    /**
     * Get parent plugin
     *
     * @return Parent plugin
     */
    public JavaPlugin getPlugin()
    {
        return this.plugin;
    }

    /**
     * Get waiting lobby spawn location
     *
     * @return Location
     */
    public Location getLobbySpawn()
    {
        return this.lobbySpawnLocation;
    }

    /**
     * Get the scoreboard instance (hearts in the tab list)
     *
     * @return Instance
     */
    public Scoreboard getScoreboard()
    {
        return this.scoreboard;
    }

    /**
     * Get the game loop instance
     *
     * @return Instance
     */
    public SURVIVALLOOP getSurvivalGameLoop()
    {
        return this.gameLoop;
    }

    /**
     * Get the world border instance
     *
     * @return World border instance
     */
    public WorldBorder getWorldBorder()
    {
        return this.worldBorder;
    }

    /**
     * Get spawn locations
     *
     * @return A list of locations
     */
    public List<Location> getSpawns()
    {
        return this.spawns;
    }

    /**
     * Is the damages activated
     *
     * @return {@code true} if yes or {@code false}
     */
    public boolean isDamagesActivated()
    {
        return this.damagesActivated;
    }

    /**
     * Is the PvP activated
     *
     * @return {@code true} if yes or {@code false}
     */
    public boolean isPvPActivated()
    {
        return this.pvpActivated;
    }
}
