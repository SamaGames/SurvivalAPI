package net.samagames.survivalapi.game.events;

import net.samagames.survivalapi.game.*;
import net.samagames.survivalapi.game.types.SurvivalTeamGame;
import net.samagames.survivalapi.game.types.run.RunBasedGame;
import net.samagames.tools.GameUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.logging.Level;

public class GameListener implements Listener
{
    private final SurvivalGame game;

    public GameListener(SurvivalGame game)
    {
        this.game = game;
    }

    /**
     * Save the last damager of a damaged player
     *
     * @param event Event
     */
    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
    {
        if (event.getEntityType().equals(EntityType.PLAYER))
        {
            Player damaged = (Player) event.getEntity();
            Entity damager = event.getDamager();

            if (damager instanceof Player)
            {
                if (!this.game.isPvPActivated() || !this.game.hasPlayer((Player)damager) || this.game instanceof SurvivalTeamGame && ((SurvivalTeamGame) this.game).getPlayerTeam(damager.getUniqueId()).hasPlayer(damaged.getUniqueId()))
                {
                    event.setCancelled(true);
                    return;
                }

                if (damaged.hasMetadata("lastDamager"))
                    damaged.removeMetadata("lastDamager", this.game.getPlugin());

                damaged.setMetadata("lastDamager", new FixedMetadataValue(this.game.getPlugin(), damager));

                if (damaged.hasMetadata("lastDamagerKeeping"))
                {
                    ((BukkitTask) damaged.getMetadata("lastDamagerKeeping").get(0).value()).cancel();
                    damaged.removeMetadata("lastDamagerKeeping", this.game.getPlugin());
                }

                if (damaged.hasMetadata("lastDamagerKeepingValue"))
                    damaged.removeMetadata("lastDamagerKeepingValue", this.game.getPlugin());

                damaged.setMetadata("lastDamagerKeeping", new FixedMetadataValue(this.game.getPlugin(), Bukkit.getScheduler().runTaskLater(this.game.getPlugin(), () -> damaged.removeMetadata("lastDamagerKeepingValue", this.game.getPlugin()), 20L * 10)));
                damaged.setMetadata("lastDamagerKeepingValue", new FixedMetadataValue(this.game.getPlugin(), damager));

                if (((Player) damager).hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
                    event.setDamage(EntityDamageEvent.DamageModifier.MAGIC, event.getDamage(EntityDamageEvent.DamageModifier.MAGIC) / 2);

                ((SurvivalPlayer) this.game.getPlayer(damager.getUniqueId())).getDamageReporter().addPlayerDamages(damaged.getUniqueId(), event.getDamage());
            }
            else if (damager instanceof Projectile)
            {
                Projectile arrow = (Projectile) damager;

                if (arrow.getShooter() instanceof Player)
                {
                    Player shooter = (Player) arrow.getShooter();
                    if (!this.game.isPvPActivated() || (this.game instanceof SurvivalTeamGame && ((SurvivalTeamGame<SurvivalGameLoop>) this.game).getPlayerTeam(shooter.getUniqueId()).hasPlayer(damaged.getUniqueId())))
                    {
                        event.setCancelled(true);
                        return;
                    }

                    if (damaged.hasMetadata("lastDamager"))
                        damaged.removeMetadata("lastDamager", this.game.getPlugin());

                    damaged.setMetadata("lastDamager", new FixedMetadataValue(this.game.getPlugin(), shooter));

                    if (damaged.hasMetadata("lastDamagerKeeping"))
                    {
                        ((BukkitTask) damaged.getMetadata("lastDamagerKeeping").get(0).value()).cancel();
                        damaged.removeMetadata("lastDamagerKeeping", this.game.getPlugin());
                    }

                    if (damaged.hasMetadata("lastDamagerKeepingValue"))
                        damaged.removeMetadata("lastDamagerKeepingValue", this.game.getPlugin());

                    damaged.setMetadata("lastDamagerKeeping", new FixedMetadataValue(this.game.getPlugin(), Bukkit.getScheduler().runTaskLater(this.game.getPlugin(), () -> damaged.removeMetadata("lastDamagerKeepingValue", this.game.getPlugin()), 20L * 10)));
                    damaged.setMetadata("lastDamagerKeepingValue", new FixedMetadataValue(this.game.getPlugin(), shooter));

                    if (shooter.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
                        event.setDamage(EntityDamageEvent.DamageModifier.MAGIC, event.getDamage(EntityDamageEvent.DamageModifier.MAGIC) / 2);

                    ((SurvivalPlayer) this.game.getPlayer(shooter.getUniqueId())).getDamageReporter().addPlayerDamages(damaged.getUniqueId(), event.getDamage());
                }
            }
            else
            {
                if (damaged.hasMetadata("lastDamager"))
                    damaged.removeMetadata("lastDamager", this.game.getPlugin());

                damaged.setMetadata("lastDamager", new FixedMetadataValue(this.game.getPlugin(), damager));
            }
        }
        else if (event.getDamager().getType() == EntityType.PLAYER)
        {
            ((SurvivalPlayer) this.game.getPlayer(event.getDamager().getUniqueId())).getDamageReporter().addEntityDamages(event.getEntityType(), event.getDamage());
        }
    }

    /**
     * Increase the Regeneration boost when a golden apple is eaten
     *
     * @param event Event
     */
    @EventHandler
    public void onItemConsume(PlayerItemConsumeEvent event)
    {
        if (this.game instanceof RunBasedGame && event.getItem().getType() == Material.GOLDEN_APPLE)
            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10 * 20, 1));
    }

    /**
     * Block Minecraft utilization
     *
     * @param event Event
     */
    @SuppressWarnings("deprecation")
    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().getType() == (Material.MINECART))
        {
            if (event.getPlayer().getItemInHand().getType() == Material.MINECART)
            {
                event.getPlayer().sendMessage(ChatColor.RED + "L'utilisation de Minecart est bloqué.");
                event.setCancelled(true);
            }
            else if (event.getPlayer().getItemInHand().getType() == Material.FLINT_AND_STEEL && !this.game.isPvPActivated())
            {
                event.getPlayer().sendMessage(ChatColor.RED + "L'utilisation du briquet est interdit en phase de préparation.");
                event.setCancelled(true);
            }
        }
    }

    /**
     * Handle player death
     *
     * @param event Event
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        if (this.game.hasPlayer(event.getEntity()) && !this.game.isSpectator(event.getEntity()))
        {
            try
            {
                this.game.stumpPlayer(event.getEntity().getUniqueId(), false, false);
            }
            catch (GameException e)
            {
                this.game.getPlugin().getLogger().log(Level.SEVERE, "Error stumping player", e);
            }

            event.getDrops().add(new ItemStack(Material.GOLDEN_APPLE));
            event.setDeathMessage("");

            if (event.getEntity().getKiller() != null)
            {
                event.getEntity().getKiller().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20 * 20, 1));
            }

            //new DeadCorpses(event.getEntity()).spawn(event.getEntity().getLocation());

            GameUtils.broadcastSound(Sound.ENTITY_WITHER_SPAWN);
        }
    }

    /**
     * Disable Guardian spawn (Mining Fatique effect)
     *
     * @param event Event
     */
    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event)
    {
        if (event.getEntityType() == EntityType.GUARDIAN || event.getEntityType() == EntityType.WITCH)
            event.setCancelled(true);
    }

    /**
     * Cancel damages if the game doesn't activate them
     * Also set last damage cause of the damaged player
     *
     * @param event Event
     */
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event)
    {
        if (event.getEntity() instanceof Player)
        {
            if (!this.game.isDamagesActivated())
            {
                event.setCancelled(true);
                return;
            }

            if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK && event.getCause() != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)
            {
                while (event.getEntity().hasMetadata("lastDamager"))
                    event.getEntity().removeMetadata("lastDamager", this.game.getPlugin());

                event.getEntity().setMetadata("lastDamager", new FixedMetadataValue(this.game.getPlugin(), event.getCause()));
            }
        }
    }

    /**
     * Handle Towers
     *
     * @param event Event
     */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        /*if (this.game.isPvPActivated() && event.getBlockPlaced().getY() > event.getBlock().getWorld().getHighestBlockYAt(event.getBlockPlaced().getX(), event.getBlockPlaced().getZ()) + 15)
        {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + "Tours" + ChatColor.DARK_RED + "] " + ChatColor.RED + "Les Tours sont interdites !");

            return;
        }*/

        if (!this.game.isPvPActivated() && event.getBlock().getType() == Material.TNT)
        {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "Vous ne pouvez pas utiliser de la TNT tant que le PvP est désactivé !");
        }
    }

    /**
     * Disable lava buckets if the PvP isn't activated
     *
     * @param event Event
     */
    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event)
    {
        if (event.getBucket().equals(Material.LAVA_BUCKET) && !this.game.isPvPActivated())
        {
            event.getPlayer().sendMessage(ChatColor.RED + "Le PvP est désactivé, l'utilisation de sources de lave est interdite.");
            event.getPlayer().getWorld().getBlockAt(event.getBlockClicked().getLocation().add(event.getBlockFace().getModX(), event.getBlockFace().getModY(), event.getBlockFace().getModZ())).setType(Material.AIR);

            ItemStack rightHand = event.getPlayer().getInventory().getItemInMainHand();
            boolean right = rightHand != null && rightHand.getType() == Material.BUCKET;
            (right ? event.getPlayer().getInventory().getItemInMainHand() : event.getPlayer().getInventory().getItemInOffHand()).setType(Material.LAVA_BUCKET);

            event.setCancelled(true);
        }
    }

    /**
     * Control sign contents
     *
     * @param event Event
     */
    @EventHandler(ignoreCancelled = true)
    public void onSignChange(SignChangeEvent event)
    {
        for (int i = 0; i < 4; i++)
            if (event.getLine(i).matches("^[a-zA-Z0-9ÀÁÂÄÇÈÉÊËÌÍÎÏÒÓÔÖÙÚÛÜàáâäçèéêëîïôöûü &]*$") && event.getLine(i).length() > 20)
                event.setCancelled(true);
    }
}
