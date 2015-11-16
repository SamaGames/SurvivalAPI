package net.samagames.survivalapi.modules.gameplay;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.Witch;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class UltraHardCoreModule extends AbstractSurvivalModule
{
    public UltraHardCoreModule(SurvivalPlugin plugin, SurvivalAPI api, HashMap<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        this.patchWorlds(plugin);
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
}
