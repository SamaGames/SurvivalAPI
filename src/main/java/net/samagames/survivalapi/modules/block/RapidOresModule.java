package net.samagames.survivalapi.modules.block;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.survivalapi.modules.utility.DropTaggingModule;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class RapidOresModule extends AbstractSurvivalModule
{
    public RapidOresModule(SurvivalPlugin plugin, SurvivalAPI api, HashMap<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        Validate.notNull(moduleConfiguration, "Configuration cannot be null!");
    }

    /**
     * Double ore's drop
     *
     * @param event Event
     */
    @EventHandler(ignoreCancelled = true)
    public void onItemSpawn(ItemSpawnEvent event)
    {
        if (event.getEntityType() != EntityType.DROPPED_ITEM)
            return;

        if (event.getEntity().hasMetadata("playerDrop"))
            return;

        Material material = event.getEntity().getItemStack().getType();

        if (material == Material.COAL && !this.api.isModuleEnabled(TorchThanCoalModule.class))
            event.getEntity().setItemStack(new ItemStack(Material.COAL, (int) this.moduleConfiguration.get("coal")));
        else if (material == Material.IRON_ORE)
            event.getEntity().setItemStack(new ItemStack(Material.IRON_INGOT, (int) this.moduleConfiguration.get("iron")));
        else if (material == Material.GOLD_ORE)
            event.getEntity().setItemStack(new ItemStack(Material.GOLD_INGOT, (int) this.moduleConfiguration.get("gold")));
        else if (material == Material.DIAMOND)
            event.getEntity().setItemStack(new ItemStack(Material.DIAMOND, (int) this.moduleConfiguration.get("diamond")));
        else if (material == Material.EMERALD)
            event.getEntity().setItemStack(new ItemStack(Material.DIAMOND, (int) this.moduleConfiguration.get("emerald")));
    }

    @Override
    public ArrayList<Class<? extends AbstractSurvivalModule>> getRequiredModules()
    {
        ArrayList<Class<? extends AbstractSurvivalModule>> requiredModules = new ArrayList<>();

        requiredModules.add(DropTaggingModule.class);

        return requiredModules;
    }

    public static class ConfigurationBuilder
    {
        private int coal, iron, gold, diamond, emerald;

        public ConfigurationBuilder()
        {
            this.coal = 2;
            this.iron = 2;
            this.gold = 2;
            this.diamond = 2;
            this.emerald = 2;
        }

        public HashMap<String, Object> build()
        {
            HashMap<String, Object> moduleConfiguration = new HashMap<>();

            moduleConfiguration.put("coal", this.coal);
            moduleConfiguration.put("iron", this.iron);
            moduleConfiguration.put("gold", this.gold);
            moduleConfiguration.put("diamond", this.diamond);
            moduleConfiguration.put("emerald", this.emerald);

            return moduleConfiguration;
        }

        public ConfigurationBuilder setCoalAmount(int coal)
        {
            this.coal = coal;
            return this;
        }

        public ConfigurationBuilder setIronAmount(int iron)
        {
            this.iron = iron;
            return this;
        }

        public ConfigurationBuilder setGoldAmount(int gold)
        {
            this.gold = gold;
            return this;
        }

        public ConfigurationBuilder setDiamondAmount(int diamond)
        {
            this.diamond = diamond;
            return this;
        }

        public ConfigurationBuilder setEmeraldAmount(int emerald)
        {
            this.emerald = emerald;
            return this;
        }
    }
}
