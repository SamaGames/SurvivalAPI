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
import java.util.UUID;

public class SurvivalPlayer extends GamePlayer
{
    private static SurvivalGame game;

    private final ArrayList<UUID> kills;
    private SurvivalTeam team;

    public SurvivalPlayer(Player player)
    {
        super(player);

        this.kills = new ArrayList<>();
        this.team = null;
    }

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
            player.teleport(this.game.getLobbySpawn());
            player.getInventory().setItem(8, SamaGamesAPI.get().getGameManager().getCoherenceMachine().getLeaveItem());
            player.getInventory().setHeldItemSlot(0);
            player.updateInventory();

            if (this.game instanceof SurvivalTeamGame)
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
            this.game.rejoinPlayer(player);
        }
    }

    @Override
    public void handleLogout()
    {
        super.handleLogout();

        if (this.spectator)
            return;

        Player player = getPlayerIfOnline();

        if (game.getStatus() == Status.IN_GAME)
        {
            game.getSurvivalGameLoop().removePlayer(player.getUniqueId());

            if (game.isPvPActivated())
            {
                game.stumpPlayer(player, true);

                Location location = player.getLocation();
                World world = location.getWorld();
                ItemStack[] inventory = player.getInventory().getContents();

                for (ItemStack stack : inventory)
                    if (stack != null)
                        world.dropItemNaturally(location, stack);
            }
        }
    }

    public void addKill(UUID killed)
    {
        this.kills.add(killed);
    }

    public void setTeam(SurvivalTeam team)
    {
        this.team = team;
    }

    public ArrayList<UUID> getKills()
    {
        return this.kills;
    }

    public SurvivalTeam getTeam()
    {
        return this.team;
    }

    public boolean hasTeam()
    {
        return this.team != null;
    }

    public static void setGame(SurvivalGame instance)
    {
        game = instance;
    }
}
