package net.samagames.survivalapi.game.types.team;

import net.samagames.api.games.Status;
import net.samagames.api.gui.AbstractGui;
import net.samagames.survivalapi.game.SurvivalTeam;
import net.samagames.survivalapi.game.types.SurvivalTeamGame;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.UUID;

/**
 * SurvivalTeamSelector class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class SurvivalTeamSelector implements Listener
{
    private static SurvivalTeamSelector instance;
    private final SurvivalTeamGame game;
    private HashMap<UUID, AbstractGui> playersGui;

    /**
     * Constructor
     *
     * @param game Team based game instance
     *
     * @throws IllegalAccessException
     */
    public SurvivalTeamSelector(SurvivalTeamGame game) throws IllegalAccessException
    {
        if (instance != null)
            throw new IllegalAccessException("Instance already defined!");

        instance = this;

        this.game = game;
        this.playersGui = new HashMap<>();
    }

    /**
     * Get the instance
     *
     * @return Instance
     */
    public static SurvivalTeamSelector getInstance()
    {
        return instance;
    }

    /**
     * Event fired when a player uses his nether star
     *
     * @param event Event
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (this.game.getStatus().equals(Status.IN_GAME))
            event.getHandlers().unregister(this);
        else if (event.getItem() != null && event.getItem().getType() == Material.NETHER_STAR)
            this.openGui(event.getPlayer(), new GuiSelectTeam());
    }

    /**
     * Event fired when a player clicks in the team selection GUI
     *
     * @param event Event
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        if (this.game.getStatus().equals(Status.IN_GAME))
        {
            event.getHandlers().unregister(this);
        }
        else if (event.getCurrentItem() != null && event.getCurrentItem().getItemMeta() != null && event.getView().getType() != InventoryType.PLAYER)
        {
            AbstractGui gui = this.playersGui.get(event.getWhoClicked().getUniqueId());

            if (gui != null)
            {
                String action = gui.getAction(event.getSlot());

                if (action != null)
                    gui.onClick((Player) event.getWhoClicked(), event.getCurrentItem(), action, event.getClick());

                event.setCancelled(true);
            }
        }
    }

    /**
     * Event fired when a player rename his team
     *
     * @param event Event
     */
    @EventHandler
    public void onSignChange(SignChangeEvent event)
    {
        if (this.game.getStatus().equals(Status.IN_GAME))
        {
            event.getHandlers().unregister(this);
            return;
        }

        if (!this.game.getStatus().equals(Status.IN_GAME))
        {
            SurvivalTeam team = this.game.getPlayerTeam(event.getPlayer().getUniqueId());
            String name = event.getLine(0);
            name = name.trim();

            if (!name.isEmpty())
            {
                team.setTeamName(name);
                event.getPlayer().sendMessage(this.game.getCoherenceMachine().getGameTag() + " " + ChatColor.GREEN + "Le nom de votre équipe est désormais : " + team.getChatColor() + team.getTeamName());
                this.openGui(event.getPlayer(), new GuiSelectTeam());
            }
            else
            {
                event.getPlayer().sendMessage(this.game.getCoherenceMachine().getGameTag() + " " + ChatColor.RED + "Le nom de l'équipe ne peut être vide.");
                this.openGui(event.getPlayer(), new GuiSelectTeam());
            }
            this.game.getPlugin().getServer().getScheduler().runTaskLater(this.game.getPlugin(), () ->
            {
                event.getBlock().setType(Material.AIR);
                event.getBlock().getRelative(BlockFace.DOWN).setType(Material.AIR);
            }, 1L);
        }
    }

    /**
     * Open a given GUI to a given player
     *
     * @param player Player
     * @param gui GUI
     */
    public void openGui(Player player, AbstractGui gui)
    {
        if (this.playersGui.containsKey(player.getUniqueId()))
        {
            player.closeInventory();
            this.playersGui.remove(player.getUniqueId());
        }

        this.playersGui.put(player.getUniqueId(), gui);
        gui.display(player);
    }

    /**
     * Handle private chat
     *
     * @param event Event
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        if (!this.game.getStatus().equals(Status.IN_GAME))
            return;

        if (this.game.isSpectator(event.getPlayer()))
        {
            event.setCancelled(true);
            return;
        }

        if (event.getMessage().startsWith("!"))
        {
            event.setCancelled(true);
            String message = event.getMessage().substring(1);
            SurvivalTeam team = this.game.getPlayerTeam(event.getPlayer().getUniqueId());

            if (team != null)
            {
                event.setCancelled(true);

                for (Player player : this.game.getPlugin().getServer().getOnlinePlayers())
                    player.sendMessage(team.getChatColor() + "[" + team.getTeamName() + "] " + event.getPlayer().getName() + " : " + ChatColor.WHITE + message);
            }
        }
        else
        {
            SurvivalTeam team = this.game.getPlayerTeam(event.getPlayer().getUniqueId());

            if (team != null)
            {
                event.setCancelled(true);
                String message = team.getChatColor() + "(Equipe) " + event.getPlayer().getName() + " : " + ChatColor.GOLD + ChatColor.ITALIC + event.getMessage();

                for (UUID id : team.getPlayersUUID().keySet())
                {
                    Player player = this.game.getPlugin().getServer().getPlayer(id);

                    if (player != null)
                        player.sendMessage(message);
                }

                this.game.getPlugin().getServer().getOnlinePlayers().stream().filter(player -> !this.game.hasPlayer(player)).forEach(player -> player.sendMessage(message));
            }
        }
    }
}
