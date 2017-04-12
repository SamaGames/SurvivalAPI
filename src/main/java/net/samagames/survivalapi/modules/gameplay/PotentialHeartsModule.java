package net.samagames.survivalapi.modules.gameplay;

import com.google.gson.JsonElement;
import net.samagames.api.games.GamePlayer;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.survivalapi.modules.IConfigurationBuilder;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * PotentialHeartsModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class PotentialHeartsModule extends AbstractSurvivalModule
{
    private final double maxHealth;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public PotentialHeartsModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        Validate.notNull(moduleConfiguration, "Configuration cannot be null!");

        this.maxHealth = (double) moduleConfiguration.get("max-health");
    }

    /**
     * Multiply max health by 2
     *
     * @param game Game
     */
    @Override
    public void onGameStart(SurvivalGame game)
    {
        for (GamePlayer player : (Collection<GamePlayer>) game.getInGamePlayers().values())
        {
            Player p = player.getPlayerIfOnline();

            if (p == null)
                continue;

            p.setMaxHealth(this.maxHealth);
            p.setHealth(this.maxHealth / 2);
        }
    }

    public static class ConfigurationBuilder implements IConfigurationBuilder
    {
        private double maxHealth;

        public ConfigurationBuilder()
        {
            this.maxHealth = 40.0D;
        }

        @Override
        public Map<String, Object> build()
        {
            Map<String, Object> moduleConfiguration = new HashMap<>();

            moduleConfiguration.put("max-health", this.maxHealth);

            return moduleConfiguration;
        }

        @Override
        public Map<String, Object> buildFromJson(Map<String, JsonElement> configuration) throws Exception
        {
            if (configuration.containsKey("max-health"))
                this.setMaxHealth(configuration.get("max-health").getAsDouble());

            return this.build();
        }

        public PotentialHeartsModule.ConfigurationBuilder setMaxHealth(double maxHealth)
        {
            this.maxHealth = maxHealth;
            return this;
        }
    }
}
