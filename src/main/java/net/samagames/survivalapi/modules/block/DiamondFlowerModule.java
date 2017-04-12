package net.samagames.survivalapi.modules.block;

import com.google.gson.JsonElement;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.survivalapi.modules.IConfigurationBuilder;
import net.samagames.survivalapi.modules.utility.DropTaggingModule;
import net.samagames.survivalapi.utils.Meta;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * DiamondFlowerModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class DiamondFlowerModule extends AbstractSurvivalModule
{
    private final Random random;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public DiamondFlowerModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        Validate.notNull(moduleConfiguration, "Configuration cannot be null!");

        this.random = new Random();
    }

    /**
     * Chance to drop a diamond when a player breaks a flower
     *
     * @param event Event
     */
    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event)
    {
        if (event.getEntityType() != EntityType.DROPPED_ITEM)
            return;

        if (event.getEntity().hasMetadata("playerDrop"))
            return;

        if (event.getEntity().getItemStack().getType() != Material.YELLOW_FLOWER
                && event.getEntity().getItemStack().getType() != Material.RED_ROSE
                && (event.getEntity().getItemStack().getType() != Material.DOUBLE_PLANT
                    || (event.getEntity().getItemStack().getDurability() > 1 && event.getEntity().getItemStack().getDurability() < 4)))
            return;

        if (this.random.nextDouble() <= (double) this.moduleConfiguration.get("chance"))
            event.getEntity().getWorld().dropItemNaturally(event.getLocation(), Meta.addMeta(new ItemStack(Material.DIAMOND, (int) this.moduleConfiguration.get("diamonds"))));

        event.setCancelled(true);
    }

    @Override
    public List<Class<? extends AbstractSurvivalModule>> getRequiredModules()
    {
        ArrayList<Class<? extends AbstractSurvivalModule>> requiredModules = new ArrayList<>();

        requiredModules.add(DropTaggingModule.class);

        return requiredModules;
    }

    public static class ConfigurationBuilder implements IConfigurationBuilder
    {
        private int diamonds;
        private double chance;

        public ConfigurationBuilder()
        {
            this.diamonds = 1;
            this.chance = 0.3D;
        }

        @Override
        public Map<String, Object> build()
        {
            Map<String, Object> moduleConfiguration = new HashMap<>();

            moduleConfiguration.put("diamonds", this.diamonds);
            moduleConfiguration.put("chance", this.chance);

            return moduleConfiguration;
        }

        @Override
        public Map<String, Object> buildFromJson(Map<String, JsonElement> configuration) throws Exception
        {
            if (configuration.containsKey("diamonds"))
                this.setDiamondAmount(configuration.get("diamonds").getAsInt());

            if (configuration.containsKey("chance"))
                this.setChance(configuration.get("chance").getAsDouble());

            return this.build();
        }

        public DiamondFlowerModule.ConfigurationBuilder setDiamondAmount(int diamonds)
        {
            this.diamonds = diamonds;
            return this;
        }

        public DiamondFlowerModule.ConfigurationBuilder setChance(double chance)
        {
            this.chance = chance;
            return this;
        }
    }
}
