package net.samagames.survivalapi.modules.gameplay;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.survivalapi.modules.utility.DropTaggingModule;
import net.samagames.survivalapi.utils.Meta;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Tree;

import java.util.*;

public class RapidUsefullModule extends AbstractSurvivalModule
{
    private final Map<ItemStack, ConfigurationBuilder.IRapidUsefulHook> drops;
    private final Random random;

    public RapidUsefullModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        Validate.notNull(moduleConfiguration, "Configuration cannot be null!");

        this.drops = (Map<ItemStack, ConfigurationBuilder.IRapidUsefulHook>) moduleConfiguration.get("drops");
        this.random = new Random();
    }

    /**
     * Drop some utilities
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

        ItemStack stack = event.getEntity().getItemStack();

        if (this.drops.containsKey(stack))
        {
            if (this.drops.get(stack) == null)
                event.setCancelled(true);
            else
                event.getEntity().setItemStack(this.drops.get(stack).getDrop(stack, this.random));
        }
    }

    /**
     * Increase the xp dropped
     *
     * @param event Event
     */
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event)
    {
        event.setDroppedExp(event.getDroppedExp() * 2);
    }

    @Override
    public List<Class<? extends AbstractSurvivalModule>> getRequiredModules()
    {
        List<Class<? extends AbstractSurvivalModule>> requiredModules = new ArrayList<>();

        requiredModules.add(DropTaggingModule.class);

        return requiredModules;
    }

    public static class ConfigurationBuilder
    {
        private final Map<ItemStack, IRapidUsefulHook> drops;

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

        public ConfigurationBuilder addDefaults()
        {
            this.addDrop(new ItemStack(Material.GRAVEL, 1), (base, random) ->
            {
                if (random.nextDouble() < 0.75)
                {
                    return new ItemStack(Material.ARROW, 3);
                }
                else
                {
                    return base;
                }
            }, false);

            this.addDrop(new ItemStack(Material.FLINT, 1), (base, random) ->
            {
                if (random.nextDouble() < 0.75)
                {
                    return new ItemStack(Material.ARROW, 3);
                }
                else
                {
                    return base;
                }
            }, false);

            this.addDrop(new ItemStack(Material.SAPLING, 1), (base, random) ->
            {
                double percent = ((Tree) base.getData()).getSpecies().equals(TreeSpecies.GENERIC) ? 0.1 : 0.3;

                if (random.nextDouble() <= percent)
                    return new ItemStack(Material.APPLE, 1);
                else
                    return null;
            }, false);

            this.addDrop(new ItemStack(Material.SAPLING, 1), (base, random) -> new ItemStack(Material.APPLE), false);
            this.addDrop(new ItemStack(Material.SAND, 1), (base, random) -> new ItemStack(Material.GLASS_BOTTLE, 1), false);
            this.addDrop(new ItemStack(Material.CACTUS, 1), (base, random) -> new ItemStack(Material.LOG, 2), false);
            this.addDrop(new ItemStack(Material.SUGAR_CANE, 1), (base, random) -> Meta.addMeta(new ItemStack(Material.SUGAR_CANE, 2)), false);
            this.addDrop(new ItemStack(Material.SULPHUR, 1), (base, random) -> Meta.addMeta(new ItemStack(Material.TNT, 1)), false);

            return this;
        }

        public ConfigurationBuilder addDrop(ItemStack base, IRapidUsefulHook rapidFoodHook, boolean override)
        {
            if (!this.drops.containsKey(base))
            {
                this.drops.put(base, rapidFoodHook);
            }
            else if (override)
            {
                this.drops.remove(base);
                this.drops.put(base, rapidFoodHook);
            }

            return this;
        }

        public interface IRapidUsefulHook
        {
            ItemStack getDrop(ItemStack base, Random random);
        }
    }
}
