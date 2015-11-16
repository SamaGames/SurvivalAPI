package net.samagames.survivalapi.game.events;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.games.Status;
import net.samagames.survivalapi.game.SurvivalGame;
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

public class SpectatorListener implements Listener
{
    private final SurvivalGame game;

    public SpectatorListener(SurvivalGame game)
    {
        this.game = game;
    }

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

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event)
    {
        event.setCancelled(this.hasToCancel((Player) event.getEntity()));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        event.setCancelled(this.hasToCancel(event.getPlayer()));
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event)
    {
        if (event.getEntity() instanceof Player)
            event.setCancelled(this.hasToCancel((Player) event.getEntity()));
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event)
    {
        event.setCancelled(this.hasToCancel(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event)
    {
        event.setCancelled(this.hasToCancel(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
    {
        event.setCancelled(this.hasToCancel(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerBucketFill(PlayerBucketFillEvent event)
    {
        event.setCancelled(this.hasToCancel(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event)
    {
        event.setCancelled(this.hasToCancel(event.getPlayer()));
    }

    @EventHandler
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event)
    {
        if (event.getEntity() instanceof Player)
            event.setCancelled(this.hasToCancel((Player) event.getEntity()));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (this.game.getStatus() != Status.IN_GAME && event.getItem().isSimilar(SamaGamesAPI.get().getGameManager().getCoherenceMachine().getLeaveItem()))
            SamaGamesAPI.get().getGameManager().kickPlayer(event.getPlayer(), "");
    }

    private boolean hasToCancel(Player player)
    {
        return this.game.getStatus() != Status.IN_GAME  || !this.game.hasPlayer(player) || this.game.isSpectator(player);
    }
}
