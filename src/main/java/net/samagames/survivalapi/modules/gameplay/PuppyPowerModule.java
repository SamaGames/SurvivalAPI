package net.samagames.survivalapi.modules.gameplay;

import com.google.gson.JsonElement;
import net.samagames.api.games.GamePlayer;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.survivalapi.modules.IConfigurationBuilder;
import net.samagames.tools.MojangShitUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * PuppyPowerModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class PuppyPowerModule extends AbstractSurvivalModule
{
    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public PuppyPowerModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        Validate.notNull(moduleConfiguration, "Configuration cannot be null!");
    }

    /**
     * Give 64 bones, 64 rotten flesh and 64 wolf eggs to all the players
     *
     * @param game Game
     */
    @Override
    public void onGameStart(SurvivalGame game)
    {
        ItemStack eggs = MojangShitUtils.getMonsterEgg(EntityType.WOLF);
        eggs.setAmount((int) this.moduleConfiguration.get("eggs"));

        for (GamePlayer player : (Collection<GamePlayer>) game.getInGamePlayers().values())
        {
            Player p = player.getPlayerIfOnline();

            if (p == null)
                continue;

            p.getInventory().addItem(new ItemStack(Material.BONE, (int) this.moduleConfiguration.get("bones")));
            p.getInventory().addItem(new ItemStack(Material.ROTTEN_FLESH, (int) this.moduleConfiguration.get("rotten-flesh")));
            p.getInventory().addItem(eggs);
        }
    }

    public static class ConfigurationBuilder implements IConfigurationBuilder
    {
        private int eggsAmount;
        private int bonesAmount;
        private int rottenFleshAmount;

        public ConfigurationBuilder()
        {
            this.eggsAmount = 64;
            this.bonesAmount = 64;
            this.rottenFleshAmount = 64;
        }

        @Override
        public Map<String, Object> build()
        {
            Map<String, Object> moduleConfiguration = new HashMap<>();

            moduleConfiguration.put("eggs", this.eggsAmount);
            moduleConfiguration.put("bones", this.bonesAmount);
            moduleConfiguration.put("rotten-flesh", this.rottenFleshAmount);

            return moduleConfiguration;
        }

        @Override
        public Map<String, Object> buildFromJson(Map<String, JsonElement> configuration) throws Exception
        {
            if (configuration.containsKey("eggs"))
                this.setEggsAmount(configuration.get("eggs").getAsInt());

            if (configuration.containsKey("bones"))
                this.setEggsAmount(configuration.get("bones").getAsInt());

            if (configuration.containsKey("rotten-flesh"))
                this.setEggsAmount(configuration.get("rotten-flesh").getAsInt());

            return this.build();
        }

        public PuppyPowerModule.ConfigurationBuilder setEggsAmount(int eggsAmount)
        {
            this.eggsAmount = eggsAmount;
            return this;
        }

        public PuppyPowerModule.ConfigurationBuilder setBonesAmount(int bonesAmount)
        {
            this.bonesAmount = bonesAmount;
            return this;
        }

        public PuppyPowerModule.ConfigurationBuilder setRottenFleshAmount(int rottenFleshAmount)
        {
            this.rottenFleshAmount = rottenFleshAmount;
            return this;
        }
    }
}

