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
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class DropMyEffectsModule extends AbstractSurvivalModule
{
    private final ArrayList<PotionEffectType> blacklist;

    public DropMyEffectsModule(SurvivalPlugin plugin, SurvivalAPI api, HashMap<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);

        blacklist = new ArrayList<>();
        if(moduleConfiguration != null)
        {
            this.blacklist.addAll((Collection<? extends PotionEffectType>) moduleConfiguration.get("blacklist"));
        }
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
            if (this.blacklist.contains(potionEffect.getType()))
                continue;

            if (PotionType.getByEffect(potionEffect.getType()) == null)
                continue;

            Potion potion = new Potion(PotionType.getByEffect(potionEffect.getType()), (potionEffect.getAmplifier() + 1));
            ItemStack stack = potion.toItemStack(1);

            PotionMeta meta = (PotionMeta) stack.getItemMeta();
            meta.clearCustomEffects();
            meta.addCustomEffect(new PotionEffect(potionEffect.getType(), potionEffect.getDuration(), potionEffect.getAmplifier()), true);

            stack.setItemMeta(meta);

            event.getDrops().add(stack);
        }
    }

    public static class ConfigurationBuilder
    {
        private ArrayList<PotionEffectType> blacklist;

        public ConfigurationBuilder()
        {
            this.blacklist = new ArrayList<>();
        }

        public HashMap<String, Object> build()
        {
            HashMap<String, Object> moduleConfiguration = new HashMap<>();

            moduleConfiguration.put("blacklist", this.blacklist);

            return moduleConfiguration;
        }

        public ConfigurationBuilder blacklistPotionEffect(PotionEffectType potionEffectType)
        {
            this.blacklist.add(potionEffectType);
            return this;
        }
    }
}
