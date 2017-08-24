package net.samagames.survivalapi.game;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.games.GamePlayer;
import net.samagames.api.games.Status;
import net.samagames.survivalapi.game.types.SurvivalTeamGame;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
public class SurvivalPlayer extends GamePlayer
{
    private static SurvivalGame game;

    private final List<UUID> kills;
    private final DamageReporter damageReporter;
    private SurvivalTeam team;
    private Location waitingspawn;

    /**
     * Constructor
     *
     * @param player Parent player
     */
    public SurvivalPlayer(Player player)
    {
        super(player);

        this.kills = new ArrayList<>();
        this.damageReporter = new DamageReporter(player.getUniqueId());
        this.team = null;
        this.waitingspawn = null;
    }

    /**
     * See SamaGamesAPI
     *
     * @param reconnect {@code true} if the player is reconnecting—if reconnection is allowed.
     */
    @Override
    public void handleLogin(boolean reconnect)
    {
        super.handleLogin(reconnect);

        Player player = getPlayerIfOnline();

        if (player == null)
            return;

        if (!reconnect)
        {
            player.setGameMode(GameMode.ADVENTURE);
            player.teleport(game.getLobbySpawn());
            player.getInventory().setItem(8, SamaGamesAPI.get().getGameManager().getCoherenceMachine().getLeaveItem());
            player.getInventory().setHeldItemSlot(0);
            player.updateInventory();

            if (game instanceof SurvivalTeamGame)
            {
                ItemStack star = new ItemStack(Material.NETHER_STAR);
                ItemMeta starMeta = star.getItemMeta();
                starMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Sélectionner une équipe");
                star.setItemMeta(starMeta);
                player.getInventory().setItem(0, star);
                player.getInventory().setHeldItemSlot(0);
            }
        }
        else
        {
            game.rejoinPlayer(player);

            if (this.team != null)
                this.team.rejoin(player);
        }
    }

    /**
     * See SamaGamesAPI
     */
    @Override
    public void handleLogout()
    {
        super.handleLogout();

        if (this.spectator)
            return;

        Player player = getPlayerIfOnline();

        if (game.getStatus() == Status.WAITING_FOR_PLAYERS || game.getStatus() == Status.READY_TO_START)
        {
            if (this.team != null)
                this.team.removePlayer(this.uuid);
        }
        else if (game.getStatus() == Status.IN_GAME)
        {
            game.getSurvivalGameLoop().removePlayer(this.uuid);

            if (!SamaGamesAPI.get().getGameManager().isReconnectAllowed(this.uuid))
            {
                try
                {
                    game.stumpPlayer(this.uuid, true, false);
                }
                catch (GameException e)
                {
                    game.plugin.getLogger().log(Level.SEVERE, "Error stumping player", e);
                }

                Location location = player.getLocation();
                World world = location.getWorld();
                ItemStack[] inventory = player.getInventory().getContents();

                for (ItemStack stack : inventory)
                    if (stack != null)
                        world.dropItemNaturally(location, stack);
            }
        }
    }

    /**
     * Add a kill to the personal list of the killer
     *
     * @param killed Player killed
     */
    public void addKill(UUID killed)
    {
        this.kills.add(killed);
    }

    /**
     * Set the team of the player
     *
     * @param team {@link SurvivalTeam} object
     */
    public void setTeam(SurvivalTeam team)
    {
        this.team = team;
    }

    /**
     * Get player's kills
     *
     * @return A list of UUID
     */
    public List<UUID> getKills()
    {
        return this.kills;
    }


    /**
     * Get player's damage reporter
     *
     * @return The damage reporter
     */
    public DamageReporter getDamageReporter()
    {
        return this.damageReporter;
    }

    /**
     * Get player's team
     *
     * @return {@link SurvivalTeam} object
     */
    public SurvivalTeam getTeam()
    {
        return this.team;
    }

    /**
     * Is the player in a team
     *
     * @return {@code true} if yes or {@code false}
     */
    public boolean hasTeam()
    {
        return this.team != null;
    }

    /**
     * Set the game instance
     *
     * @param instance Game instance
     */
    public static void setGame(SurvivalGame instance)
    {
        game = instance;
    }

    /**
     * Set the player waiting spawn
     *
     * @param spawn Spawn instance
     */
    public void setWaitingSpawn(Location spawn)
    {
        this.waitingspawn = spawn;
    }

    /**
     * Get the player waiting spawn
     *
     * @return Spawn instance
     */
    public Location getWaitingSpawn()
    {
        return waitingspawn;
    }
}
