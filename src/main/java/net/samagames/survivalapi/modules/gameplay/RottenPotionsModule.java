package net.samagames.survivalapi.modules.gameplay;

import com.google.gson.JsonElement;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.survivalapi.modules.IConfigurationBuilder;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * RottenPotionsModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class RottenPotionsModule extends AbstractSurvivalModule
{
    private final Random random;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public RottenPotionsModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        Validate.notNull(moduleConfiguration, "Configuration cannot be null!");

        this.random = new Random();
    }

    /**
     * Give a random potion effect when a player eat rotten flesh
     *
     * @param event Event
     */
    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event)
    {
        if (event.getItem().getType() == Material.ROTTEN_FLESH)
        {
            PotionEffectType effectType = PotionEffectType.values()[this.random.nextInt(PotionEffectType.values().length)];

            if (effectType != null)
                event.getPlayer().addPotionEffect(new PotionEffect(effectType, (int) this.moduleConfiguration.get("effect-time") * 20, 1));
        }
    }

    public static class ConfigurationBuilder implements IConfigurationBuilder
    {
        private int effectTime;

        public ConfigurationBuilder()
        {
            this.effectTime = 10;
        }

        @Override
        public Map<String, Object> build()
        {
            Map<String, Object> moduleConfiguration = new HashMap<>();

            moduleConfiguration.put("effect-time", this.effectTime);

            return moduleConfiguration;
        }

        @Override
        public Map<String, Object> buildFromJson(Map<String, JsonElement> configuration) throws Exception
        {
            if (configuration.containsKey("effect-time"))
                this.setEffectTime(configuration.get("effect-time").getAsInt());

            return this.build();
        }

        public RottenPotionsModule.ConfigurationBuilder setEffectTime(int effectTime)
        {
            this.effectTime = effectTime;
            return this;
        }
    }
}
