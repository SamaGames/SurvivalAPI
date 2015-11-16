package net.samagames.survivalapi.modules.gameplay;

import SafePortals.SafePortalsUtils;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.Witch;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class UltraHardCoreModule extends AbstractSurvivalModule
{
    private SurvivalGame game;

    public UltraHardCoreModule(SurvivalPlugin plugin, SurvivalAPI api, HashMap<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        this.patchWorlds(plugin);
    }

    @Override
    public void onGameStart(SurvivalGame game)
    {
        this.game = game;
    }

    /**
     * Setting world's parameters
     *
     * @param plugin Plugin
     */
    public void patchWorlds(SurvivalPlugin plugin)
    {
        for (World world : plugin.getServer().getWorlds())
        {
            world.setDifficulty(Difficulty.NORMAL);
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setTime(1000L);
        }
    }

    /**
     * Cancelling natural regeneration
     *
     * @param event Event
     */
    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent event)
    {
        if (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.EATING || event.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED)
            event.setCancelled(true);
    }

    /**
     * Patching witch's potions
     *
     * @param event Event
     */
    @EventHandler
    public void onPotionSplash(PotionSplashEvent event)
    {
        ThrownPotion potion = event.getPotion();

        if (potion.getShooter() instanceof Witch)
        {
            event.setCancelled(true);

            List<PotionEffectType> potionEffects = new ArrayList<>();
            potionEffects.add(PotionEffectType.SLOW_DIGGING);
            potionEffects.add(PotionEffectType.CONFUSION);
            potionEffects.add(PotionEffectType.NIGHT_VISION);
            potionEffects.add(PotionEffectType.HUNGER);
            potionEffects.add(PotionEffectType.BLINDNESS);

            PotionEffect selected = new PotionEffect(potionEffects.get(new Random().nextInt(potionEffects.size())), 20 * 15, 1);

            for (LivingEntity ent : event.getAffectedEntities())
                ent.addPotionEffect(selected);
        }
    }

    /**
     * Patch player teleporting through portals to be INSIDE the world border
     *
     * @param event Event
     */
    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event)
    {
        TravelAgent travelAgent = event.getPortalTravelAgent();
        Location destination = travelAgent.findPortal(event.getTo());

        if(!SafePortalsUtils.isInsideBorder(destination))
        {
            event.useTravelAgent(false);
            boolean success = travelAgent.createPortal(event.getTo());

            if(success)
            {
                event.setTo(travelAgent.findPortal(event.getTo()));

                if (!SafePortalsUtils.isSafeSpot(event.getTo()))
                {
                    Location safeTo = SafePortalsUtils.searchSafeSpot(event.getTo());

                    if (safeTo != null)
                        event.setTo(safeTo);
                }
            }
        }
    }

    /**
     * Prevent portal trap
     *
     * @param event Event
     */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        if(!this.game.isGameStarted() || !this.game.hasPlayer(event.getPlayer()))
        {
            event.setCancelled(true);
            return;
        }

        boolean flag = false;

        if(event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation().add(1.0D, 0.0D, 0.0D)).getType() == Material.PORTAL)
            flag = true;

        if(event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation().subtract(1.0D, 0.0D, 0.0D)).getType() == Material.PORTAL)
            flag = true;

        if(event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation().add(0.0D, 0.0D, 1.0D)).getType() == Material.PORTAL)
            flag = true;

        if(event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation().subtract(0.0D, 0.0D, 1.0D)).getType() == Material.PORTAL)
            flag = true;

        if(flag)
        {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "Vous ne pouvez pas placer de bloc ici !");
        }
    }
}
