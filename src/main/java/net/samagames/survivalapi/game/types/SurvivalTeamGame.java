package net.samagames.survivalapi.game.types;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.games.Status;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.game.SurvivalGameLoop;
import net.samagames.survivalapi.game.SurvivalPlayer;
import net.samagames.survivalapi.game.SurvivalTeam;
import net.samagames.survivalapi.game.types.team.GuiSelectTeam;
import net.samagames.survivalapi.game.types.team.SurvivalTeamList;
import net.samagames.survivalapi.game.types.team.SurvivalTeamSelector;
import net.samagames.survivalapi.game.types.team.TeamWinTemplate;
import net.samagames.tools.Titles;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class SurvivalTeamGame<SURVIVALLOOP extends SurvivalGameLoop> extends SurvivalGame
{
    private final int personsPerTeam;
    private SurvivalTeamSelector teamSelector;
    protected SurvivalTeamList teams;

    public SurvivalTeamGame(JavaPlugin plugin, String gameCodeName, String gameName, String gameDescription, String magicSymbol, Class<? extends SURVIVALLOOP> survivalGameLoopClass, int personsPerTeam)
    {
        super(plugin, gameCodeName, gameName, gameDescription, magicSymbol, survivalGameLoopClass);

        //Disable rank tab
        SamaGamesAPI.get().getServerOptions().setRankTabColorEnable(false);

        this.personsPerTeam = personsPerTeam;
        this.teams = new SurvivalTeamList();

        try
        {
            this.teamSelector = new SurvivalTeamSelector(this);
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }

        plugin.getServer().getPluginManager().registerEvents(this.teamSelector, plugin);

        this.registerTeam("Blanc", ChatColor.WHITE, DyeColor.WHITE);
        this.registerTeam("Orange", ChatColor.GOLD, DyeColor.ORANGE);
        this.registerTeam("Bleu Clair", ChatColor.BLUE, DyeColor.LIGHT_BLUE);
        this.registerTeam("Bleu Foncé", ChatColor.DARK_BLUE, DyeColor.BLUE);
        this.registerTeam("Cyan", ChatColor.AQUA, DyeColor.CYAN);
        this.registerTeam("Jaune", ChatColor.YELLOW, DyeColor.YELLOW);
        this.registerTeam("Rose", ChatColor.LIGHT_PURPLE, DyeColor.PINK);
        this.registerTeam("Vert Foncé", ChatColor.DARK_GREEN, DyeColor.GREEN);
        this.registerTeam("Rouge", ChatColor.RED, DyeColor.RED);
        this.registerTeam("Violet", ChatColor.DARK_PURPLE, DyeColor.PURPLE);
        this.registerTeam("Gris", ChatColor.GRAY, DyeColor.GRAY);
        this.registerTeam("Noir", ChatColor.BLACK, DyeColor.BLACK);

        GuiSelectTeam.setGame(this);
    }

    @Override
    public void startGame()
    {
        List<UUID> uuids = new ArrayList<>();
        uuids.addAll(this.getInGamePlayers().keySet());
        for (UUID uuid : uuids)
        {
            Player player = Bukkit.getPlayer(uuid);

            if (player == null)
                continue;

            if (this.getPlayerTeam(uuid) == null)
            {
                for (SurvivalTeam team : teams)
                {
                    if (!team.isFull() && !team.isLocked())
                    {
                        team.join(uuid);
                        break;
                    }
                }

                if (this.getPlayerTeam(uuid) == null)
                    player.kickPlayer(ChatColor.RED + "Aucune team était apte à vous reçevoir, vous avez été réenvoyé dans le hub.");
            }
        }

        super.startGame();
    }

    @Override
    public void teleport()
    {
        Iterator<Location> locationIterator = this.spawns.iterator();

        for (SurvivalTeam team : this.teams)
        {
            if (!locationIterator.hasNext() || team.isEmpty())
            {

                for (UUID player : team.getPlayersUUID().keySet())
                {
                    Player p = this.server.getPlayer(player);

                    if (p != null)
                        p.kickPlayer(ChatColor.RED + "Plus de place dans la partie.");

                    team.removePlayer(player);
                    this.gamePlayers.remove(player);
                }
                continue;
            }

            Location location = locationIterator.next();

            for (UUID player : team.getPlayersUUID().keySet())
            {
                Player p = this.server.getPlayer(player);

                if (p != null)
                {
                    p.teleport(location);
                }
            }
        }
    }

    @Override
    public void checkStump(Player player)
    {
        this.server.getScheduler().runTaskLater(this.plugin, () ->
        {
            SurvivalTeam team = this.teams.getTeam(player.getUniqueId());

            if (team == null)
                return;

            int left = team.playerDied(player.getUniqueId());

            if (left == 0)
            {
                this.server.broadcastMessage(ChatColor.GOLD + "L'équipe " + team.getChatColor() + team.getTeamName() + ChatColor.GOLD + " a été éliminée !");

                int teamLeft = countAliveTeam();

                if (teamLeft == 1)
                {
                    this.win(this.teams.get(0));
                    return;
                }
                else if (teamLeft < 1)
                {
                    this.handleGameEnd();
                    return;
                }
                else
                {
                    this.server.broadcastMessage(ChatColor.YELLOW + "Il reste encore " + ChatColor.AQUA + teamLeft + ChatColor.YELLOW + " équipes en jeu.");
                }
            }
        }, 2L);
    }

    @Override
    public void stumpPlayer(Player player, boolean logout)
    {
        super.stumpPlayer(player, logout);

        if (logout && !this.getStatus().equals(Status.IN_GAME))
        {
            SurvivalTeam team = this.teams.getTeam(player.getUniqueId());

            if (team != null)
                team.playerDied(player.getUniqueId());

        }
    }

    public void win(final SurvivalTeam team)
    {
        for (final UUID playerID : team.getPlayersUUID().keySet())
        {

            SurvivalPlayer playerData = (SurvivalPlayer) this.getPlayer(playerID);
            if(playerData != null)
            {
                playerData.addCoins(100, "Victoire !");
                playerData.addStars(2, "Victoire !");
            }

            this.increaseStat(playerID, "wins", 1);

            Player player = Bukkit.getPlayer(playerID);
            if (player == null)
                continue;

            this.effectsOnWinner(player);
        }

        for (Player user : this.server.getOnlinePlayers())
            Titles.sendTitle(user, 0, 60, 5, ChatColor.RED + "Fin du jeu", ChatColor.YELLOW + "Victoire de l'équipe " + team.getChatColor() + team.getTeamName());

        new TeamWinTemplate().execute(team);

        this.handleGameEnd();
    }

    public void registerTeam(String name, ChatColor chatColor, DyeColor color)
    {
        this.teams.add(new SurvivalTeam(this, name, color, chatColor));
    }

    public SurvivalTeam getPlayerTeam(UUID uniqueId)
    {
        return this.teams.getTeam(uniqueId);
    }

    public SurvivalTeamList getTeams()
    {
        return this.teams;
    }

    public int countAliveTeam()
    {
        int nb = 0;

        for(SurvivalTeam team : teams)
        {
            if (!team.isDead())
                nb++;
        }

        return nb;
    }

    public int getPersonsPerTeam()
    {
        return this.personsPerTeam;
    }
}
