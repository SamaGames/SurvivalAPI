package net.samagames.survivalapi.modules.block;

import net.minecraft.server.v1_8_R3.EntityExperienceOrb;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.World;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.survivalapi.modules.utility.DropTaggingModule;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
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
    @EventHandler
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

        this.spawnXPFromItemStack(event.getEntity(), event.getEntity().getItemStack().getType());
    }

    /**
     * Cancel ore break event
     *
     * @return event Event
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
                event.setExpToDrop(0);
                event.getBlock().breakNaturally(new ItemStack(Material.DIAMOND_PICKAXE));
                event.setCancelled(true);
                break;
            default:
                break;
        }
    }

    @Override
    public ArrayList<Class<? extends AbstractSurvivalModule>> getRequiredModules()
    {
        ArrayList<Class<? extends AbstractSurvivalModule>> requiredModules = new ArrayList<>();

        requiredModules.add(DropTaggingModule.class);

        return requiredModules;
    }

    private void spawnXPFromItemStack(Entity entity, Material ore)
    {
        World world = ((CraftEntity) entity).getHandle().getWorld();

        int i = 0;

        switch (ore)
        {
            case QUARTZ:
            case INK_SACK:
                i = MathHelper.nextInt(world.random, 2, 5);
                break;
            case EMERALD:
            case DIAMOND:
                i = MathHelper.nextInt(world.random, 3, 7);
                break;
            case COAL:
            case GOLD_INGOT:
            case IRON_INGOT:
                i = MathHelper.nextInt(world.random, 0, 2);
                break;
            default:
                break;

        }

        if (i == 0)
            return;

        int orbSize;

        while (i > 0)
        {
            orbSize = EntityExperienceOrb.getOrbValue(i);
            i -= orbSize;
            world.addEntity(new EntityExperienceOrb(world, entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ(), orbSize));
        }
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
