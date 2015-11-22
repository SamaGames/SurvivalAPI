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
import java.util.UUID;

public class SurvivalTeam
{
    private final SurvivalTeamGame game;
    private final ArrayList<UUID> invited;
    private final HashMap<UUID, Boolean> players;
    private final ItemStack icon;
    private final ChatColor chatColor;
    //private final Team team;

    private String teamName;
    private int maxSize;
    private boolean locked;

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

        /**this.team = board.registerNewTeam("meow" + chatColor.getChar());
        this.team.setDisplayName(name);
        this.team.setCanSeeFriendlyInvisibles(true);
        this.team.setPrefix(chatColor + "");
        this.team.setSuffix(ChatColor.RESET + "");**/
    }

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
        }

        ((SurvivalPlayer) this.game.getPlayer(player)).setTeam(this);
        //this.team.addPlayer(newJoiner);

        this.players.put(player, false);
    }

    public void invite(String inviter, UUID invited)
    {
        this.invited.add(invited);

        new FancyMessage("Vous avez été invité dans l'équipe " + this.teamName + " par " + inviter + " ")
                .color(ChatColor.GOLD)
                .style(ChatColor.BOLD)
                .then("[Rejoindre]")
                .color(ChatColor.GREEN)
                .style(ChatColor.BOLD)
                .command("/uhc join " + this.chatColor.getChar())
                .send(Bukkit.getPlayer(invited));
    }

    public void remove(UUID player, boolean death)
    {
        if (death)
        {
            this.players.put(player, true);
        }
        else
        {
            this.players.remove(player);
            //this.team.removePlayer(Bukkit.getOfflinePlayer(player));
        }

        this.lockCheck();
    }

    public int removePlayer(UUID player, boolean death)
    {
        if (death)
        {
            this.players.put(player, true);
        }
        else
        {
            this.players.remove(player);
            //this.team.removePlayer(Bukkit.getOfflinePlayer(player));
        }

        this.lockCheck();

        return this.getAlivePlayers();
    }

    public void lockCheck()
    {
        if (this.players.size() == 0 && this.isLocked())
            this.setLocked(false);
    }

    public void setTeamName(String teamName)
    {
        this.teamName = teamName;
    }

    public void setLocked(boolean locked)
    {
        this.locked = locked;
    }

    public ItemStack getIcon()
    {
        return this.icon;
    }

    public HashMap<UUID, Boolean> getPlayersUUID()
    {
        return this.players;
    }

    public int getAlivePlayers()
    {
        int i = 0;

        for (boolean dead : this.getPlayersUUID().values())
            if (!dead)
                i++;

        return i;
    }

    public String getTeamName()
    {
        return this.teamName;
    }

    public ChatColor getChatColor()
    {
        return this.chatColor;
    }

    public boolean hasPlayer(UUID player)
    {
        return this.players.containsKey(player);
    }

    public boolean canJoin()
    {
        return !this.isLocked() && this.players.size() < this.maxSize;
    }

    public boolean isInvited(UUID player)
    {
        return this.invited.contains(player);
    }

    public boolean isLocked()
    {
        return this.locked;
    }

    public boolean isEmpty()
    {
        return this.players.isEmpty();
    }

    public boolean isFull()
    {
        return this.players.size() == this.game.getPersonsPerTeam();
    }
}
