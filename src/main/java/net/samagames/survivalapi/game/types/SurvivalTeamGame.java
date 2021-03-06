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
import java.util.logging.Level;

/*
 * This file is part of SurvivalAPI.
 *
 * SurvivalAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SurvivalAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SurvivalAPI.  If not, see <http://www.gnu.org/licenses/>.
 */
public class SurvivalTeamGame<SURVIVALLOOP extends SurvivalGameLoop> extends SurvivalGame<SURVIVALLOOP>
{
    private final int personsPerTeam;
    private SurvivalTeamSelector teamSelector;
    protected SurvivalTeamList teams;
    protected boolean teamBalancing;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param gameCodeName Game code name
     * @param gameName Game name
     * @param gameDescription Game description
     * @param magicSymbol Symbol into the scoreboard
     * @param survivalGameLoopClass Class of the game loop
     * @param personsPerTeam Number of players per team
     */
    public SurvivalTeamGame(JavaPlugin plugin, String gameCodeName, String gameName, String gameDescription, String magicSymbol, Class<? extends SURVIVALLOOP> survivalGameLoopClass, int personsPerTeam)
    {
        super(plugin, gameCodeName, gameName, gameDescription, magicSymbol, survivalGameLoopClass);

        //Disable rank tab
        SamaGamesAPI.get().getServerOptions().setRankTabColorEnable(false);

        this.personsPerTeam = personsPerTeam;
        this.teamBalancing = false;
        this.teams = new SurvivalTeamList();

        try
        {
            this.teamSelector = new SurvivalTeamSelector(this);
        }
        catch (IllegalAccessException e)
        {
            this.plugin.getLogger().log(Level.SEVERE, "Error initiating team selector", e);
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

        for (int i = 0; i < SamaGamesAPI.get().getGameManager().getGameProperties().getGameOption("teams", new JsonPrimitive(12)).getAsInt(); i++)
        {
            if (i > temporaryTeams.size())
                break;
            else
                this.registerTeam(temporaryTeams.get(i));
        }

        GuiSelectTeam.setGame(this);
    }

    /**
     * See SamaGamesAPI
     */
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
                SurvivalTeam team;
                if (this.teamBalancing)
                    team = this.teams.stream().filter(t -> !t.isFull() && !t.isLocked()).sorted((t1, t2) -> t1.getPlayersUUID().size() - t2.getPlayersUUID().size()).findFirst().orElse(null);
                else
                    team = this.teams.stream().filter(t -> !t.isFull() && !t.isLocked()).findFirst().orElse(null);

                if (team == null)
                    player.kickPlayer(ChatColor.RED + "Aucune team était apte à vous reçevoir, vous avez été réenvoyé dans le hub.");
                else
                    team.join(uuid);
            }
        }

        super.startGame();
    }

    /**
     * See {@link SurvivalGame}
     */
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

            Location destination = locationIterator.next().add(0,8,0);

            for (UUID player : team.getPlayersUUID().keySet())
            {
                Player p = this.server.getPlayer(player);

                if (p != null)
                {
                    ChunkUtils.loadDestination(p, destination, 3);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> p.teleport(destination), 2);
                    SurvivalPlayer playerdata = this.getPlayer(player);
                    if (playerdata != null)
                        playerdata.setWaitingSpawn(destination);
                }
            }
        }
    }

    /**
     * See {@link SurvivalGame}
     *
     * @param playerUUID Player
     * @param silent Display messages
     *
     * @throws GameException
     */
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
                        this.win(this.getLastAliveTeam());
                    else if (teamLeft < 1)
                        this.handleGameEnd();
                    else if (!silent)
                        this.coherenceMachine.getMessageManager().writeCustomMessage(ChatColor.YELLOW + "Il reste encore " + ChatColor.AQUA + teamLeft + ChatColor.YELLOW + " équipes en jeu.", true);
                }
            }
            catch (NullPointerException | IllegalStateException ignored)
            {
                try
                {
                    throw new GameException(ignored.getMessage());
                }
                catch (GameException ex)
                {
                    this.plugin.getLogger().log(Level.SEVERE, "Error checking stump players", ex);
                }
            }
        }, 2L);
    }

    /**
     * See {@link SurvivalGame}
     *
     * @param playerUUID Player
     * @param logout Is disconnection death
     * @param silent Display messages
     *
     * @throws GameException
     */
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

    /**
     * Execute win procedure
     *
     * @param team Team
     */
    public void win(final SurvivalTeam team)
    {
        for (final UUID playerID : team.getPlayersUUID().keySet())
        {
            SurvivalPlayer playerData = this.getPlayer(playerID);

            if(playerData != null)
                playerData.addCoins(100, "Victoire !");

            this.handleWinner(playerID);

            Player player = this.plugin.getServer().getPlayer(playerID);

            if (player == null)
                continue;

            this.effectsOnWinner(player);
        }

        for (Player user : this.server.getOnlinePlayers())
            Titles.sendTitle(user, 0, 60, 5, ChatColor.RED + "Fin du jeu", ChatColor.YELLOW + "Victoire de l'équipe " + team.getChatColor() + team.getTeamName());

        new TeamWinTemplate().execute(team);

        this.handleGameEnd();
    }

    /**
     * Register a team in the game
     *
     * @param team Team instance
     */
    public void registerTeam(SurvivalTeam team)
    {
        this.teams.add(team);
    }

    /**
     * Get player's team
     *
     * @param uniqueId Player's UUID
     *
     * @return Team instance
     */
    public SurvivalTeam getPlayerTeam(UUID uniqueId)
    {
        return this.teams.getTeam(uniqueId);
    }

    /**
     * Get the last alive team
     *
     * @return Team instance
     */
    public SurvivalTeam getLastAliveTeam()
    {
        for(SurvivalTeam team : this.teams)
            if (!team.isDead())
                return team;

        return null;
    }

    /**
     * Get teams list
     *
     * @return A list of teams
     */
    public SurvivalTeamList getTeams()
    {
        return this.teams;
    }

    /**
     * Get the number of alive teams
     *
     * @return Number of teams
     */
    public int countAliveTeam()
    {
        int nb = 0;

        for(SurvivalTeam team : this.teams)
            if (!team.isDead())
                nb++;

        return nb;
    }

    /**
     * Get the number of players per team
     *
     * @return Number of players
     */
    public int getPersonsPerTeam()
    {
        return this.personsPerTeam;
    }
}
