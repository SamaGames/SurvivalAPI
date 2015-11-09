package net.samagames.survivalapi.modules.gameplay;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractConfigurationBuilder;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class TorchThanCoalModule extends AbstractSurvivalModule
{
    public TorchThanCoalModule(SurvivalPlugin plugin, SurvivalAPI api, HashMap<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        Validate.notNull(moduleConfiguration, "Configuration cannot be null!");

    }

    /**
     * Replace coal by torch
     *
     * @param event Event
     */
    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event)
    {
        if (event.getEntity().getItemStack().getType() == Material.COAL)
            event.getEntity().setItemStack(new ItemStack(Material.TORCH, (int) this.moduleConfiguration.get("torch")));
    }

    public static class ConfigurationBuilder extends AbstractConfigurationBuilder
    {
        private int torch;

        public ConfigurationBuilder()
        {
            this.torch = 3;
        }

        public HashMap<String, Object> build()
        {
            HashMap<String, Object> moduleConfiguration = new HashMap<>();

            moduleConfiguration.put("torch", this.torch);

            return moduleConfiguration;
        }

        public ConfigurationBuilder setTorchAmount(int torch)
        {
            this.torch = torch;
            return this;
        }
    }
}
