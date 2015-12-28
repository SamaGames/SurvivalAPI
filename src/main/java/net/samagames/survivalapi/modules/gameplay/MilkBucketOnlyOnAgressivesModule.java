package net.samagames.survivalapi.modules.gameplay;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MilkBucketOnlyOnAgressivesModule extends AbstractSurvivalModule
{
    public MilkBucketOnlyOnAgressivesModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event)
    {
        if (event.getItem().getType() == Material.MILK_BUCKET)
        {
            event.setCancelled(true);

            List<PotionEffect> toRemove = event.getPlayer().getActivePotionEffects().stream().filter(potionEffect -> isAgressive(potionEffect)).collect(Collectors.toList());
            toRemove.forEach(potionEffect -> event.getPlayer().removePotionEffect(potionEffect.getType()));

            event.setItem(new ItemStack(Material.BUCKET, 1));
        }
    }

    private static boolean isAgressive(PotionEffect potionEffect)
    {
        return potionEffect.getType() == PotionEffectType.BLINDNESS || potionEffect.getType() == PotionEffectType.CONFUSION || potionEffect.getType() == PotionEffectType.HUNGER || potionEffect.getType() == PotionEffectType.POISON || potionEffect.getType() == PotionEffectType.SLOW || potionEffect.getType() == PotionEffectType.SLOW_DIGGING || potionEffect.getType() == PotionEffectType.WEAKNESS || potionEffect.getType() == PotionEffectType.WITHER;
    }
}
