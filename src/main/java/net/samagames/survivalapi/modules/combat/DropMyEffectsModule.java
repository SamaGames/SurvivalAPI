package net.samagames.survivalapi.modules.combat;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import java.util.HashMap;

public class DropMyEffectsModule extends AbstractSurvivalModule
{
    public DropMyEffectsModule(SurvivalPlugin plugin, SurvivalAPI api, HashMap<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * Drop player's potion effect on his death
     *
     * @param event Event
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        for (PotionEffect potionEffect : event.getEntity().getActivePotionEffects())
        {
            Potion potion = new Potion(PotionType.getByEffect(potionEffect.getType()), potionEffect.getAmplifier());
            ItemStack stack = potion.toItemStack(1);

            PotionMeta meta = (PotionMeta) stack.getItemMeta();
            meta.clearCustomEffects();
            meta.addCustomEffect(new PotionEffect(potionEffect.getType(), potionEffect.getDuration(), potionEffect.getAmplifier()), true);

            stack.setItemMeta(meta);

            event.getDrops().add(stack);
        }
    }
}
