package net.samagames.survivalapi.modules.block;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.survivalapi.utils.Meta;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * WorldDropModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class WorldDropModule extends AbstractSurvivalModule
{
    private final Map<Material, ItemStack> drops;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public WorldDropModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        Validate.notNull(moduleConfiguration, "Configuration cannot be null!");

        this.drops = (Map<Material, ItemStack>) this.moduleConfiguration.get("drops");
    }

    /**
     * Replace world's drop by custom ones
     *
     * @param event Event
     */
    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event)
    {
        if (event.getEntityType() != EntityType.DROPPED_ITEM)
            return;

        if (Meta.hasMeta(event.getEntity().getItemStack()))
            return;

        if (!this.drops.containsKey(event.getEntity().getItemStack().getType()))
            return;

        event.getEntity().setItemStack(Meta.addMeta(this.drops.get(event.getEntity().getItemStack().getType())));
    }

    public static class ConfigurationBuilder
    {
        private final Map<Material, ItemStack> drops;

        public ConfigurationBuilder()
        {
            this.drops = new HashMap<>();
        }

        public Map<String, Object> build()
        {
            Map<String, Object> moduleConfiguration = new HashMap<>();

            moduleConfiguration.put("drops", this.drops);

            return moduleConfiguration;
        }

        public ConfigurationBuilder addCustomDrop(Material origin, ItemStack drop)
        {
            this.drops.put(origin, drop);
            return this;
        }
    }
}
