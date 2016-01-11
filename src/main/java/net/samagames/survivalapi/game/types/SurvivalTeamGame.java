package net.samagames.survivalapi.game.types;

import com.google.gson.JsonPrimitive;
import net.samagames.api.SamaGamesAPI;
import net.samagames.api.games.Status;
import net.samagames.survivalapi.game.*;
import net.samagames.survivalapi.game.types.team.GuiSelectTeam;
import net.samagames.survivalapi.game.types.team.SurvivalTeamList;
import net.samagames.survivalapi.game.types.team.SurvivalTeamSelector;
import net.samagames.survivalapi.game.types.team.TeamWinTemplate;
import net.samagames.survivalapi.utils.ChunkUtils;
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

        List<SurvivalTeam> temporaryTeams = new ArrayList<>();

        temporaryTeams.add(new SurvivalTeam(this, "Blanc", DyeColor.WHITE, ChatColor.WHITE));
        temporaryTeams.add(new SurvivalTeam(this, "Orange", DyeColor.ORANGE, ChatColor.GOLD));
        temporaryTeams.add(new SurvivalTeam(this, "Bleu Clair", DyeColor.LIGHT_BLUE, ChatColor.BLUE));
        temporaryTeams.add(new SurvivalTeam(this, "Bleu Foncé", DyeColor.BLUE, ChatColor.DARK_BLUE));
        temporaryTeams.add(new SurvivalTeam(this, "Cyan", DyeColor.CYAN, ChatColor.AQUA));
        temporaryTeams.add(new SurvivalTeam(this, "Jaune", DyeColor.YELLOW, ChatColor.YELLOW));
        temporaryTeams.add(new SurvivalTeam(this, "Rose", DyeColor.PINK, ChatColor.LIGHT_PURPLE));
        temporaryTeams.add(new SurvivalTeam(this, "Vert Foncé", DyeColor.GREEN, ChatColor.DARK_GREEN));
        temporaryTeams.add(new SurvivalTeam(this, "Rouge", DyeColor.RED, ChatColor.RED));
        temporaryTeams.add(new SurvivalTeam(this, "Violet", DyeColor.PURPLE, ChatColor.DARK_PURPLE));
        temporaryTeams.add(new SurvivalTeam(this, "Gris", DyeColor.GRAY, ChatColor.GRAY));
        temporaryTeams.add(new SurvivalTeam(this, "Noir", DyeColor.BLACK, ChatColor.BLACK));

        for (int i = 0; i < SamaGamesAPI.get().getGameManager().getGameProperties().getOption("teams", new JsonPrimitive(12)).getAsInt(); i++)
        {
            if (i > temporaryTeams.size())
                break;
            else
                this.registerTeam(temporaryTeams.get(i));
        }

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

            Location destination = locationIterator.next();

            for (UUID player : team.getPlayersUUID().keySet())
            {
                Player p = this.server.getPlayer(player);

                if (p != null)
                {
                    ChunkUtils.loadDestination(p, destination, 3);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> p.teleport(destination), 2);
                }
            }
        }
    }

    @Override
    public void checkStump(UUID playerUUID, boolean silent) throws GameException
    {
        this.server.getScheduler().runTaskLater(this.plugin, () ->
        {
            try
            {
                SurvivalTeam team = this.teams.getTeam(playerUUID);

                if (team == null)
                    return;

                int left = team.playerDied(playerUUID);

                if (left == 0)
                {
                    this.coherenceMachine.getMessageManager().writeCustomMessage(ChatColor.YELLOW + "L'équipe " + team.getChatColor() + team.getTeamName() + ChatColor.YELLOW + " a été éliminée !", true);

                    int teamLeft = this.countAliveTeam();

                    if (teamLeft == 1)
                    {
                        this.win(this.getLastAliveTeam());
                        return;
                    }
                    else if (teamLeft < 1)
                    {
                        this.handleGameEnd();
                        return;
                    }
                    else if (!silent)
                    {
                        this.coherenceMachine.getMessageManager().writeCustomMessage(ChatColor.YELLOW + "Il reste encore " + ChatColor.AQUA + teamLeft + ChatColor.YELLOW + " équipes en jeu.", true);
                    }
                }
            }
            catch (NullPointerException | IllegalStateException e)
            {
                try
                {
                    throw new GameException(e.getMessage());
                }
                catch (GameException ignored) {}
            }
        }, 2L);
    }

    @Override
    public void stumpPlayer(UUID playerUUID, boolean logout, boolean silent) throws GameException
    {
        super.stumpPlayer(playerUUID, logout, silent);

        if (logout && !this.getStatus().equals(Status.IN_GAME))
        {
            SurvivalTeam team = this.teams.getTeam(playerUUID);

            if (team != null)
                team.playerDied(playerUUID);
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

    public void registerTeam(SurvivalTeam team)
    {
        this.teams.add(team);
    }

    public SurvivalTeam getPlayerTeam(UUID uniqueId)
    {
        return this.teams.getTeam(uniqueId);
    }

    public SurvivalTeam getLastAliveTeam()
    {
        for(SurvivalTeam team : this.teams)
            if (!team.isDead())
                return team;

        return null;
    }

    public SurvivalTeamList getTeams()
    {
        return this.teams;
    }

    public int countAliveTeam()
    {
        int nb = 0;

        for(SurvivalTeam team : this.teams)
            if (!team.isDead())
                nb++;

        return nb;
    }

    public int getPersonsPerTeam()
    {
        return this.personsPerTeam;
    }
}
