package net.samagames.survivalapi.modules.block;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.samagames.api.SamaGamesAPI;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.survivalapi.modules.IConfigurationBuilder;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.*;

/**
 * PainfulStonesModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class PainfulStonesModule extends AbstractSurvivalModule
{
    private final List<Material> blocks;
    private final Set<UUID> damaged;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public PainfulStonesModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        Validate.notNull(moduleConfiguration, "Configuration cannot be null!");

        this.blocks = (List<Material>) moduleConfiguration.get("blocks");
        this.damaged = new HashSet<>();
    }

    /**
     * Damage player when walking on gravel if he does not have boots
     * @param event Move event
     */
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        if (!((SurvivalGame) SamaGamesAPI.get().getGameManager().getGame()).isDamagesActivated())
            return;

        Block block = event.getTo().clone().subtract(0, 1, 0).getBlock();

        if (block != null && this.blocks.contains(block.getType())
                && (event.getPlayer().getInventory().getBoots() == null || event.getPlayer().getInventory().getBoots().getType() == Material.AIR)
                && !this.damaged.contains(event.getPlayer().getUniqueId()))
        {
            this.damaged.add(event.getPlayer().getUniqueId());
            event.getPlayer().damage((double) this.moduleConfiguration.get("damages"));

            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> this.damaged.remove(event.getPlayer().getUniqueId()), 20L);
        }
    }

    public static class ConfigurationBuilder implements IConfigurationBuilder
    {
        private List<Material> blocks;
        private double damages;

        public ConfigurationBuilder()
        {
            this.blocks = new ArrayList<>();
            this.damages = 1.0D;
        }

        @Override
        public Map<String, Object> build()
        {
            Map<String, Object> moduleConfiguration = new HashMap<>();

            moduleConfiguration.put("blocks", this.blocks);
            moduleConfiguration.put("damages", this.damages);

            return moduleConfiguration;
        }

        @Override
        public Map<String, Object> buildFromJson(Map<String, JsonElement> configuration) throws Exception
        {
            if (configuration.containsKey("blocks"))
            {
                JsonArray blocksJson = configuration.get("blocks").getAsJsonArray();
                blocksJson.forEach(element -> this.blocks.add(Material.matchMaterial(element.getAsString())));
            }

            if (configuration.containsKey("damages"))
                this.setDamages(configuration.get("damages").getAsDouble());

            return this.build();
        }

        public PainfulStonesModule.ConfigurationBuilder addDefaults()
        {
            this.blocks.add(Material.GRAVEL);
            return this;
        }

        public PainfulStonesModule.ConfigurationBuilder setDamages(double damages)
        {
            this.damages = damages;
            return this;
        }
    }
}

