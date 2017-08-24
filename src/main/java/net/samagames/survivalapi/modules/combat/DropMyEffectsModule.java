package net.samagames.survivalapi.modules.combat;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.survivalapi.modules.IConfigurationBuilder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.*;

/*
 * This file is part of SurvivalAPI.
 *
 * SurvivalAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SurvivalAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SurvivalAPI.  If not, see <http://www.gnu.org/licenses/>.
 */
public class DropMyEffectsModule extends AbstractSurvivalModule
{
    private final List<PotionEffectType> blacklist;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public DropMyEffectsModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);

        this.blacklist = new ArrayList<>();

        if(moduleConfiguration != null)
            this.blacklist.addAll((Collection<? extends PotionEffectType>) moduleConfiguration.get("blacklist"));
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

            if(potionEffect.getDuration() > 10000)
                continue;

            Potion potion = new Potion(PotionType.getByEffect(potionEffect.getType()), potionEffect.getAmplifier() + 1);
            ItemStack stack = potion.toItemStack(1);

            PotionMeta meta = (PotionMeta) stack.getItemMeta();
            meta.clearCustomEffects();
            meta.addCustomEffect(new PotionEffect(potionEffect.getType(), potionEffect.getDuration(), potionEffect.getAmplifier()), true);

            stack.setItemMeta(meta);

            event.getDrops().add(stack);
            event.getEntity().removePotionEffect(potionEffect.getType());
        }
    }

    public static class ConfigurationBuilder implements IConfigurationBuilder
    {
        private final ArrayList<PotionEffectType> blacklist;

        public ConfigurationBuilder()
        {
            this.blacklist = new ArrayList<>();
        }

        @Override
        public Map<String, Object> build()
        {
            HashMap<String, Object> moduleConfiguration = new HashMap<>();

            moduleConfiguration.put("blacklist", this.blacklist);

            return moduleConfiguration;
        }

        @Override
        public Map<String, Object> buildFromJson(Map<String, JsonElement> configuration) throws Exception
        {
            if (configuration.containsKey("blacklist"))
            {
                JsonArray blacklistJson = configuration.get("blacklist").getAsJsonArray();
                blacklistJson.forEach(jsonElement -> this.blacklistPotionEffect(PotionEffectType.getByName(jsonElement.getAsString())));
            }

            return this.build();
        }

        public ConfigurationBuilder blacklistPotionEffect(PotionEffectType potionEffectType)
        {
            this.blacklist.add(potionEffectType);
            return this;
        }
    }
}
