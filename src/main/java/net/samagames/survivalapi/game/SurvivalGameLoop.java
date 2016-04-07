package net.samagames.survivalapi.game;

import net.samagames.survivalapi.game.types.SurvivalTeamGame;
import net.samagames.survivalapi.utils.TimedEvent;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.tools.Titles;
import net.samagames.tools.chat.ActionBarAPI;
import net.samagames.tools.scoreboards.ObjectiveSign;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SurvivalGameLoop class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class SurvivalGameLoop implements Runnable
{
    protected final JavaPlugin plugin;
    protected final Server server;
    protected final SurvivalGame game;
    protected final World world;
    protected final ConcurrentHashMap<UUID, ObjectiveSign> objectives;

    protected TimedEvent nextEvent;
    protected int minutes;
    protected int seconds;
    protected int episode;
    protected boolean episodeEnabled;
    protected boolean blocksProtected;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param server Server instance
     * @param game Game instance
     */
    public SurvivalGameLoop(JavaPlugin plugin, Server server, SurvivalGame game)
    {
        this.game = game;
        this.plugin = plugin;
        this.server = server;
        this.world = server.getWorlds().get(0);
        this.objectives = new ConcurrentHashMap<>();

        this.seconds = 0;
        this.minutes = 0;
        this.episode = 1;

        this.episodeEnabled = false;
        this.blocksProtected = true;

        this.createWaitingBlockRemovingEvent();
    }

    public void createWaitingBlockRemovingEvent()
    {
        this.nextEvent = new TimedEvent(0, 10, "Suppression des cages", ChatColor.GREEN, true, () ->
        {
            this.game.removeWaitingBlocks();
            this.blocksProtected = false;
            this.createDamageEvent();
	    SurvivalAPI.get().fireGameStart(this.game);
        });
    }

    public void createDamageEvent()
    {
        this.nextEvent = new TimedEvent(1, 0, "Dégats actifs", ChatColor.GREEN, false, () ->
        {
            this.game.getCoherenceMachine().getMessageManager().writeCustomMessage("Les dégats sont désormais actifs.", true);
            this.game.enableDamages();

            this.createPvPEvent();
        });
    }

    public void createPvPEvent()
    {
        this.nextEvent = new TimedEvent(19, 0, "Combats actifs", ChatColor.GOLD, false, () ->
        {
            this.game.getCoherenceMachine().getMessageManager().writeCustomMessage("Les combats sont désormais actifs.", true);
            this.game.enablePVP();

            this.createReducingEvent();
        });
    }

    public void createReducingEvent()
    {
        this.nextEvent = new TimedEvent(70, 0, "Réduction des bordures", ChatColor.RED, false, () ->
        {
            this.game.getWorldBorder().setSize(64, 60L * 20L);
            this.displayReducingMessage();
            this.createEndOfReducingEvent();
        });
    }

    public void createEndOfReducingEvent()
    {
        this.nextEvent = new TimedEvent(30, 0, "Fin de la réduction", ChatColor.YELLOW, false, () ->
        {
            this.game.getWorldBorder().setSize(100);
            this.createEndingEvent();
        });
    }

    public void createEndingEvent()
    {
        this.nextEvent = new TimedEvent(10, 0, "Fermeture du serveur", ChatColor.RED, false, Bukkit::shutdown);
    }

    /**
     * Function to display with a title that the border are reducing
     */
    public void displayReducingMessage()
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            Titles.sendTitle(player, 0, 100, 5, ChatColor.RED + "Attention !", ChatColor.YELLOW + "Les bordures se réduisent !");
            player.playSound(player.getLocation(), Sound.BLAZE_DEATH, 1.0F, 1.0F);
        }

        this.game.getCoherenceMachine().getMessageManager().writeCustomMessage(ChatColor.RED + "Les bordures se réduisent !", true);
    }

    /**
     * Force the next event to execute right now
     */
    public void forceNextEvent()
    {
        if (this.nextEvent != null)
            this.nextEvent.run();
    }

    /**
     * Add a game player to receive his scoreboard
     *
     * @param uuid Player's UUID
     * @param sign {@link ObjectiveSign} instance
     */
    public void addPlayer(UUID uuid, ObjectiveSign sign)
    {
        this.objectives.put(uuid, sign);
    }

    /**
     * Remvoe a game player to receive his scoreboard
     *
     * @param uuid Player's UUID
     */
    public void removePlayer(UUID uuid)
    {
        this.objectives.remove(uuid);
    }

    /**
     * Calculate the scoreboard and events decreasing
     */
    @Override
    public void run()
    {
        this.seconds++;

        if (this.seconds >= 60)
        {
            this.minutes++;
            this.seconds = 0;

            if (this.episodeEnabled && this.minutes >= 20)
            {
                this.game.getCoherenceMachine().getMessageManager().writeCustomMessage("Fin de l'épisode " + this.episode, true);
                this.episode++;

                this.minutes = 0;
            }
        }

        for (UUID playerUUID : this.objectives.keySet())
        {
            ObjectiveSign objective = this.objectives.get(playerUUID);
            Player player = this.server.getPlayer(playerUUID);

            objective.clearScores();

            if (player == null)
            {
                this.objectives.remove(playerUUID);
            }
            else
            {
                objective.setLine(0, ChatColor.DARK_RED + "");
                objective.setLine(1, ChatColor.GRAY + "Joueurs : " + ChatColor.WHITE + this.game.getInGamePlayers().size());

                int lastLine = 1;

                if (this.game instanceof SurvivalTeamGame)
                {
                    objective.setLine(lastLine + 1, ChatColor.GRAY + "Équipes : " + ChatColor.WHITE + ((SurvivalTeamGame) this.game).countAliveTeam());
                    lastLine++;
                }

                objective.setLine(lastLine + 1, ChatColor.RED + "");
                lastLine++;

                if (this.nextEvent != null)
                    ActionBarAPI.sendMessage(player, this.nextEvent.getColor().toString() + this.nextEvent.getName() + " dans " + this.toString(this.nextEvent.getSeconds() == 0 ? this.nextEvent.getMinutes() - 1 : this.nextEvent.getMinutes(), this.nextEvent.getSeconds() == 0 ? 59 : this.nextEvent.getSeconds() - 1));

                SurvivalPlayer gamePlayer = (SurvivalPlayer) this.game.getPlayer(playerUUID);
                int kills = gamePlayer == null ? 0 : gamePlayer.getKills().size();

                objective.setLine(lastLine + 1, ChatColor.GRAY + "Joueurs tués : " + ChatColor.WHITE + kills);
                objective.setLine(lastLine + 2, ChatColor.AQUA + "");

                lastLine += 2;

                if (this.game instanceof SurvivalTeamGame && gamePlayer != null && gamePlayer.getTeam() != null)
                {
                    int teammates = 0;

                    for (UUID teammateUUID : gamePlayer.getTeam().getPlayersUUID().keySet())
                    {
                        if (playerUUID.equals(teammateUUID))
                            continue;

                        teammates++;

                        Player teammate = Bukkit.getPlayer(teammateUUID);

                        if (teammate == null)
                            objective.setLine(lastLine + teammates, ChatColor.RED + "× " + Bukkit.getOfflinePlayer(teammateUUID).getName() + " : Déconnecté");
                        else if (this.game.getPlayer(teammateUUID).isSpectator())
                            objective.setLine(lastLine + teammates, ChatColor.RED + "× " + teammate.getName() + " : ✞");
                        else
                            objective.setLine(lastLine + teammates, getPrefixColorByHealth(teammate.getHealth(), teammate.getMaxHealth()) + getDirection(player, teammate) + " " + teammate.getName() + ChatColor.WHITE + " : " + (int) (teammate.getHealth() + ((CraftPlayer) teammate).getHandle().getAbsorptionHearts()) + ChatColor.RED + " ❤");
                    }

                    objective.setLine(lastLine + teammates + 1, ChatColor.DARK_PURPLE + "");

                    lastLine += teammates + 1;
                }

                objective.setLine(lastLine + 1, ChatColor.GRAY + "Bordure :");
                objective.setLine(lastLine + 2, ChatColor.WHITE + "-" + (int) this.world.getWorldBorder().getSize() / 2 + " +" + (int) this.world.getWorldBorder().getSize() / 2);
                objective.setLine(lastLine + 3, ChatColor.LIGHT_PURPLE + "");
                objective.setLine(lastLine + 4, ChatColor.GRAY + "Temps de jeu : " + ChatColor.WHITE + this.toString(this.minutes, this.seconds));

                objective.updateLines();

                this.server.getScheduler().runTaskAsynchronously(this.plugin, objective::updateLines);
            }
        }

        if (this.nextEvent != null)
        {
            if (this.nextEvent.getSeconds() == 0 && this.nextEvent.getMinutes() <= 3 && this.nextEvent.getMinutes() > 0 || this.nextEvent.getMinutes() == 0 && (this.nextEvent.getSeconds() <= 5 || this.nextEvent.getSeconds() == 10 || this.nextEvent.getSeconds() == 30))
            {
                this.game.getCoherenceMachine().getMessageManager().writeCustomMessage(ChatColor.YELLOW + this.nextEvent.getName() + ChatColor.YELLOW + " dans " + (this.nextEvent.getMinutes() != 0 ? this.nextEvent.getMinutes() + " minute" + (this.nextEvent.getMinutes() > 1 ? "s" : "") : this.nextEvent.getSeconds() + " seconde" + (this.nextEvent.getSeconds() > 1 ? "s" : "")) + ".", true);

                if (this.nextEvent.isTitle() && this.nextEvent.getSeconds() <= 5 && this.nextEvent.getSeconds() > 0)
                    for (Player player : Bukkit.getOnlinePlayers())
                        Titles.sendTitle(player, 0, 21, 10, ChatColor.RED + "" + (this.nextEvent.getSeconds() - 1), this.nextEvent.getName());
            }

            if (this.nextEvent.getSeconds() == 0 && this.nextEvent.getMinutes() == 0)
                this.game.getCoherenceMachine().getMessageManager().writeCustomMessage(ChatColor.YELLOW + this.nextEvent.getName() + ChatColor.YELLOW + " maintenant !", true);

            this.nextEvent.decrement();
        }
    }

    /**
     * Get a color according the health
     *
     * @param health Health
     * @param max Health max
     *
     * @return A color
     */
    private static ChatColor getPrefixColorByHealth(double health, double max)
    {
        double q = max / 4;

        if (health < q)
            return ChatColor.RED;
        else if (health < (q * 2))
            return ChatColor.YELLOW;
        else if (health < (q * 3))
            return ChatColor.GREEN;
        else
            return ChatColor.DARK_GREEN;
    }

    /**
     * Get the arrow corresponding to the location between two given players
     *
     * @param p First player
     * @param mate Second player
     *
     * @return A arrow
     */
    private static String getDirection(Player p, Player mate)
    {
        Location ploc = p.getLocation().clone();
        Location point = mate.getLocation().clone();

        if (ploc.getWorld().getEnvironment() != point.getWorld().getEnvironment())
            return "•";

        ploc.setY(0);
        point.setY(0);

        Vector d = ploc.getDirection();
        Vector v = point.subtract(ploc).toVector().normalize();

        double a = Math.toDegrees(Math.atan2(d.getX(), d.getZ()));
        a -= Math.toDegrees(Math.atan2(v.getX(), v.getZ()));
        a = (int) (a + 22.5) % 360;

        if (a < 0)
            a += 360;

        return Character.toString("⬆⬈➡⬊⬇⬋⬅⬉".charAt((int) a / 45));
    }

    /**
     * Format a time into a formatted string
     *
     * @param minutes Minutes
     * @param seconds Seconds
     *
     * @return Formatted string
     */
    private String toString(int minutes, int seconds)
    {
        return (minutes < 10 ? "0" : "") + minutes + ":" + (seconds < 10 ? "0" : "") + seconds;
    }

    /**
     * Return if blocks are protected from breaking
     * Usefull for cages
     */
    public boolean areBlocksProtected()
    {
        return this.blocksProtected;
    }
}
