package net.samagames.survivalapi.modules.gameplay;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * BloodDiamondModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class BloodDiamondModule extends AbstractSurvivalModule
{
    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public BloodDiamondModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        Validate.notNull(moduleConfiguration, "Configuration cannot be null!");
    }

    /**
     * Damage the player when he mine a diamond ore
     *
     * @param event Event
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        if (event.getBlock().getType() == Material.DIAMOND_ORE)
            event.getPlayer().damage((double) this.moduleConfiguration.get("damages"));
    }

    public static class ConfigurationBuilder
    {
        private double damages;

        public ConfigurationBuilder()
        {
            this.damages = 0.5D;
        }

        public Map<String, Object> build()
        {
            HashMap<String, Object> moduleConfiguration = new HashMap<>();

            moduleConfiguration.put("damages", this.damages);

            return moduleConfiguration;
        }

        public ConfigurationBuilder setDamages(double damages)
        {
            this.damages = damages;
            return this;
        }
    }
}
