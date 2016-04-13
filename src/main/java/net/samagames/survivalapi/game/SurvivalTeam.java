package net.samagames.survivalapi.game;

import net.samagames.survivalapi.game.types.SurvivalTeamGame;
import net.samagames.tools.chat.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * SurvivalTeam object
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class SurvivalTeam
{
    private final SurvivalTeamGame game;
    private final ArrayList<UUID> invited;
    private final HashMap<UUID, Boolean> players;
    private final ItemStack icon;
    private final ChatColor chatColor;
    private final Team team;

    private String teamName;
    private int maxSize;
    private boolean locked;

    /**
     * Constructor
     *
     * @param game Team based game instance
     * @param name Team's name
     * @param color Team's color
     * @param chatColor Team's color in chat
     */
    public SurvivalTeam(SurvivalTeamGame game, String name, DyeColor color, ChatColor chatColor)
    {
        this.game = game;
        this.teamName = name;
        this.chatColor = chatColor;
        this.icon = new ItemStack(Material.WOOL, 1, color.getData());
        this.maxSize = game.getPersonsPerTeam();

        this.invited = new ArrayList<>();
        this.players = new HashMap<>();

        Scoreboard board = game.getScoreboard();

        this.team = board.registerNewTeam("meow" + chatColor.getChar());
        this.team.setDisplayName(name);
        this.team.setCanSeeFriendlyInvisibles(true);
        this.team.setPrefix(chatColor + "");
        this.team.setSuffix(ChatColor.RESET + "");
    }

    /**
     * Add the given player into the team
     *
     * @param player Player's UUID
     */
    public void join(UUID player)
    {
        Player newJoiner = Bukkit.getPlayer(player);

        if (newJoiner != null)
        {
            for (UUID member : this.players.keySet())
            {
                Player user = Bukkit.getPlayer(member);
                if (user != null)
                    user.sendMessage(this.game.getCoherenceMachine().getGameTag() + " " + ChatColor.AQUA + newJoiner.getName() + ChatColor.YELLOW + " a rejoint l'équipe.");
            }

            ((SurvivalPlayer) this.game.getPlayer(player)).setTeam(this);
            this.team.addEntry(newJoiner.getName());

            this.players.put(player, false);
        }
    }

    /**
     * Fired when a player reconnects
     *
     * @param player Player
     */
    public void rejoin(Player player)
    {
        this.team.addEntry(player.getName());
    }

    /**
     * Invite a given player to the team
     *
     * @param member Person who invite
     * @param invited Invited person
     */
    public void invite(String member, UUID invited)
    {
        this.invited.add(invited);

        new FancyMessage("Vous avez été invité dans l'équipe " + this.teamName + " par " + member + " ")
                .color(ChatColor.GOLD)
                .style(ChatColor.BOLD)
                .then("[Rejoindre]")
                .color(ChatColor.GREEN)
                .style(ChatColor.BOLD)
                .command("/uhc join " + this.chatColor.getChar())
                .send(Bukkit.getPlayer(invited));
    }

    /**
     * Fired when a player die
     *
     * @param player Player's UUID
     *
     * @return Alive team's players
     */
    public int playerDied(UUID player)
    {
        this.players.put(player, true);

        return this.getAlivePlayers();
    }

    /**
     * Remove a given player from the team
     *
     * @param player Player's UUID
     *
     * @return Alive team's players
     */
    public int removePlayer(UUID player)
    {
        this.players.remove(player);
        this.team.removeEntry(Bukkit.getOfflinePlayer(player).getName());

        this.lockCheck();

        return this.getAlivePlayers();
    }

    /**
     * Unlock the team is it's empty
     */
    public void lockCheck()
    {
        if (this.players.isEmpty() && this.isLocked())
            this.setLocked(false);
    }

    /**
     * Set team's name
     *
     * @param teamName Team's name
     */
    public void setTeamName(String teamName)
    {
        this.teamName = teamName;
    }

    /**
     * Define the team as locked or unlocked
     *
     * @param locked State
     */
    public void setLocked(boolean locked)
    {
        this.locked = locked;
    }

    /**
     * Get the team icon (with the color given in the constructor)
     *
     * @return A ItemStack
     */
    public ItemStack getIcon()
    {
        return this.icon;
    }

    /**
     * Get team's members
     *
     * @return A map corresponding to a player UUID and if he is dead
     */
    public Map<UUID, Boolean> getPlayersUUID()
    {
        return (Map<UUID, Boolean>) this.players.clone();
    }

    /**
     * Get the number of alive players in the team
     *
     * @return Number of players
     */
    public int getAlivePlayers()
    {
        int i = 0;

        for (boolean dead : this.getPlayersUUID().values())
            if (!dead)
                i++;

        return i;
    }

    /**
     * Get team's name
     *
     * @return Team's name
     */
    public String getTeamName()
    {
        return this.teamName;
    }

    /**
     * Get team's chat color
     *
     * @return Team's chat color
     */
    public ChatColor getChatColor()
    {
        return this.chatColor;
    }

    /**
     * Know if a given player is in this team
     *
     * @param player Player's UUID
     *
     * @return {@code true} if yes or {@code false}
     */
    public boolean hasPlayer(UUID player)
    {
        return this.players.containsKey(player);
    }

    /**
     * Is this team joinable
     *
     * @return {@code true} if yes or {@code false}
     */
    public boolean canJoin()
    {
        return !this.isLocked() && this.players.size() < this.maxSize;
    }

    /**
     * Know if a given player is invited to join this team
     *
     * @param player Player's UUID
     *
     * @return {@code true} if yes or {@code false}
     */
    public boolean isInvited(UUID player)
    {
        return this.invited.contains(player);
    }

    /**
     * Is the team locked
     *
     * @return {@code true} if yes or {@code false}
     */
    public boolean isLocked()
    {
        return this.locked;
    }

    /**
     * Is the team empty
     *
     * @return {@code true} if yes or {@code false}
     */
    public boolean isEmpty()
    {
        return this.players.isEmpty();
    }

    /**
     * Are all members of the team dead
     *
     * @return {@code true} if yes or {@code false}
     */
    public boolean isDead()
    {
        return getAlivePlayers() == 0;
    }

    /**
     * Is the team full
     *
     * @return {@code true} if yes or {@code false}
     */
    public boolean isFull()
    {
        return this.players.size() == this.game.getPersonsPerTeam();
    }
}
