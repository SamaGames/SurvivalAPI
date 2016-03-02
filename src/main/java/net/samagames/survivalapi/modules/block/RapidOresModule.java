package net.samagames.survivalapi.modules.block;

import net.minecraft.server.v1_9_R1.EntityExperienceOrb;
import net.minecraft.server.v1_9_R1.MathHelper;
import net.minecraft.server.v1_9_R1.World;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.survivalapi.modules.utility.DropTaggingModule;
import net.samagames.survivalapi.utils.Meta;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * RapidOresModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class RapidOresModule extends AbstractSurvivalModule
{
    private final Map<ItemStack, ConfigurationBuilder.IRapidOresHook> drops;
    private final Random random;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public RapidOresModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        Validate.notNull(moduleConfiguration, "Configuration cannot be null!");

        this.drops = (Map<ItemStack, ConfigurationBuilder.IRapidOresHook>) moduleConfiguration.get("drops");
        this.random = new Random();
    }

    /**
     * Double ore's drop
     *
     * @param event Event
     */
    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event)
    {
        if (event.getEntityType() != EntityType.DROPPED_ITEM)
            return;

        if(Meta.hasMeta(event.getEntity().getItemStack()))
            return;

        ItemStack stack = event.getEntity().getItemStack();

        if (this.drops.containsKey(stack))
        {
            ItemStack newStack = this.drops.get(stack).getDrop(stack, this.random);

            if (newStack != null)
            {
                event.getEntity().setItemStack(Meta.addMeta(newStack));
                this.spawnXP(event.getEntity(), this.drops.get(stack).getExperienceModifier(this.random));
            }
        }
    }

    /**
     * Cancel ore break event
     *
     * @param event Event
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        switch (event.getBlock().getType())
        {
            case DIAMOND_ORE:
            case LAPIS_ORE:
            case GOLD_ORE:
            case OBSIDIAN:
            case IRON_ORE:
            case REDSTONE_ORE:
            case QUARTZ_ORE:
            case QUARTZ_BLOCK:
                event.setExpToDrop(0);
                event.getBlock().breakNaturally(new ItemStack(Material.DIAMOND_PICKAXE));
                event.setCancelled(true);
                break;

            default:
                break;
        }
    }

    private void spawnXP(Entity entity, int randomized)
    {
        World world = ((CraftEntity) entity).getHandle().getWorld();

        int i = randomized;
        int orbSize;

        while (i > 0)
        {
            orbSize = EntityExperienceOrb.getOrbValue(i);
            i -= orbSize;
            world.addEntity(new EntityExperienceOrb(world, entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ(), orbSize));
        }
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
        private final Map<ItemStack, IRapidOresHook> drops;

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
            this.addDrop(new ItemStack(Material.COAL, 1), new IRapidOresHook()
            {
                @Override
                public ItemStack getDrop(ItemStack base, Random random)
                {
                    if (SurvivalAPI.get().isModuleEnabled(TorchThanCoalModule.class))
                        return null;

                    return new ItemStack(Material.COAL, 3);
                }

                @Override
                public int getExperienceModifier(Random random)
                {
                    return MathHelper.nextInt(random, 0, 2);
                }
            }, false);

            this.addDrop(new ItemStack(Material.IRON_ORE, 1), new IRapidOresHook()
            {
                @Override
                public ItemStack getDrop(ItemStack base, Random random)
                {
                    return new ItemStack(Material.IRON_INGOT, 2);
                }

                @Override
                public int getExperienceModifier(Random random)
                {
                    return MathHelper.nextInt(random, 0, 2);
                }
            }, false);

            this.addDrop(new ItemStack(Material.GOLD_ORE, 1), new IRapidOresHook()
            {
                @Override
                public ItemStack getDrop(ItemStack base, Random random)
                {
                    return new ItemStack(Material.GOLD_INGOT, 2);
                }

                @Override
                public int getExperienceModifier(Random random)
                {
                    return MathHelper.nextInt(random, 0, 2);
                }
            }, false);

            this.addDrop(new ItemStack(Material.DIAMOND, 1), new IRapidOresHook()
            {
                @Override
                public ItemStack getDrop(ItemStack base, Random random)
                {
                    return new ItemStack(Material.DIAMOND, 2);
                }

                @Override
                public int getExperienceModifier(Random random)
                {
                    return MathHelper.nextInt(random, 3, 7);
                }
            }, false);

            this.addDrop(new ItemStack(Material.EMERALD, 1), new IRapidOresHook()
            {
                @Override
                public ItemStack getDrop(ItemStack base, Random random)
                {
                    return new ItemStack(Material.EMERALD, 2);
                }

                @Override
                public int getExperienceModifier(Random random)
                {
                    return MathHelper.nextInt(random, 3, 7);
                }
            }, false);

            this.addDrop(new ItemStack(Material.QUARTZ, 1), new IRapidOresHook()
            {
                @Override
                public ItemStack getDrop(ItemStack base, Random random)
                {
                    return base;
                }

                @Override
                public int getExperienceModifier(Random random)
                {
                    return MathHelper.nextInt(random, 2, 5);
                }
            }, false);

            this.addDrop(new ItemStack(Material.INK_SACK, 1, (short) 4), new IRapidOresHook()
            {
                @Override
                public ItemStack getDrop(ItemStack base, Random random)
                {
                    return new ItemStack(Material.INK_SACK, base.getAmount() + random.nextInt(5) + 1, (short) 4);
                }

                @Override
                public int getExperienceModifier(Random random)
                {
                    return MathHelper.nextInt(random, 2, 5);
                }
            }, false);

            return this;
        }

        public ConfigurationBuilder addDrop(ItemStack base, IRapidOresHook rapidOresHook, boolean override)
        {
            if (!this.drops.containsKey(base))
            {
                this.drops.put(base, rapidOresHook);
            }
            else if (override)
            {
                this.drops.remove(base);
                this.drops.put(base, rapidOresHook);
            }

            return this;
        }

        public interface IRapidOresHook
        {
            ItemStack getDrop(ItemStack base, Random random);
            int getExperienceModifier(Random random);
        }
    }
}
