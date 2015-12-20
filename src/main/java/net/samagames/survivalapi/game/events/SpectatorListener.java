package net.samagames.survivalapi.game.events;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.games.Status;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.game.SurvivalPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.*;

import java.util.Collection;

public class SpectatorListener implements Listener
{
    private final SurvivalGame game;

    public SpectatorListener(SurvivalGame game)
    {
        this.game = game;
    }

    /**
     * Teleport back the player in the waiting island if he fall
     *
     * @param event Event
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event)
    {
        if ((this.game.getStatus() == Status.READY_TO_START || this.game.getStatus() == Status.WAITING_FOR_PLAYERS) && event.getTo().getY() < 125)
        {
            event.setCancelled(true);
            event.getPlayer().teleport(this.game.getLobbySpawn());
            event.getPlayer().sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "Mais où allez-vous comme ça ?!");
        }
    }

    /**
     * Disable food level changing
     *
     * @param event Event
     */
    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event)
    {
        event.setCancelled(this.hasToCancel((Player) event.getEntity()));
    }

    /**
     * Disable block breaking
     *
     * @param event Event
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        event.setCancelled(this.hasToCancel(event.getPlayer()));
    }

    /**
     * Disable entity interaction
     *
     * @param event Event
     */
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event)
    {
        if (event.getEntity() instanceof Player)
            event.setCancelled(this.hasToCancel((Player) event.getEntity()));
    }

    /**
     * Disable ability to pickup items
     *
     * @param event Event
     */
    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event)
    {
        event.setCancelled(this.hasToCancel(event.getPlayer()));
    }

    /**
     * Disable ability to drop items
     *
     * @param event Event
     */
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event)
    {
        event.setCancelled(this.hasToCancel(event.getPlayer()));
    }

    /**
     * Disable entity interaction
     *
     * @param event Event
     */
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
    {
        event.setCancelled(this.hasToCancel(event.getPlayer()));
    }

    /**
     * Disable bucket filling
     *
     * @param event Event
     */
    @EventHandler
    public void onPlayerBucketFill(PlayerBucketFillEvent event)
    {
        event.setCancelled(this.hasToCancel(event.getPlayer()));
    }

    /**
     * Disable bucket emptying
     *
     * @param event Event
     */
    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event)
    {
        event.setCancelled(this.hasToCancel(event.getPlayer()));
    }

    /**
     * Disable pre-breaking blocks
     *
     * @param event Event
     */
    @EventHandler
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event)
    {
        if (event.getEntity() instanceof Player)
            event.setCancelled(this.hasToCancel((Player) event.getEntity()));
    }

    /**
     * Handle leaving door
     *
     * @param event Event
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (event.getItem() != null && this.game.getStatus() != Status.IN_GAME && event.getItem().isSimilar(SamaGamesAPI.get().getGameManager().getCoherenceMachine().getLeaveItem()))
            SamaGamesAPI.get().getGameManager().kickPlayer(event.getPlayer(), "");
    }

    /**
     * Spectator's chat
     *
     * @param event Event
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        if (!this.game.getStatus().equals(Status.IN_GAME))
            return;

        if (!this.game.isSpectator(event.getPlayer()))
            return;

        String finalMessage = ChatColor.GRAY + "[Spectateur] " + event.getPlayer().getName() + ": " + event.getMessage();

        ((Collection<SurvivalPlayer>) this.game.getSpectatorPlayers().values()).stream().filter(spectator -> spectator.getPlayerIfOnline() != null).forEach(spectator -> spectator.getPlayerIfOnline().sendMessage(finalMessage));
    }

    private boolean hasToCancel(Player player)
    {
        return this.game.getStatus() != Status.IN_GAME  || !this.game.hasPlayer(player) || this.game.isSpectator(player);
    }
}
