package net.samagames.survivalapi.modules.block;

import net.minecraft.server.v1_8_R3.EntityExperienceOrb;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.World;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.survivalapi.modules.utility.DropTaggingModule;
import net.samagames.survivalapi.utils.AttributeStorage;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class RapidOresModule extends AbstractSurvivalModule
{
    public final UUID ID = UUID.fromString("3745e6a8-821a-4c53-bd7c-3a1246a458f0");
    private final Random random;

    public RapidOresModule(SurvivalPlugin plugin, SurvivalAPI api, HashMap<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        Validate.notNull(moduleConfiguration, "Configuration cannot be null!");

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

        if(hasMeta(event.getEntity().getItemStack()))
            return;

        Material material = event.getEntity().getItemStack().getType();
        boolean flag = false;

        switch(material)
        {
            case COAL:
                if (!this.api.isModuleEnabled(TorchThanCoalModule.class))
                {
                    event.getEntity().setItemStack(new ItemStack(Material.COAL, (int) this.moduleConfiguration.get("coal")));
                    flag = true;
                }
                break;

            case IRON_ORE:
                event.getEntity().setItemStack(new ItemStack(Material.IRON_INGOT, (int) this.moduleConfiguration.get("iron")));
                flag = true;
                break;

            case GOLD_ORE:
                event.getEntity().setItemStack(new ItemStack(Material.GOLD_INGOT, (int) this.moduleConfiguration.get("gold")));
                flag = true;
                break;

            case DIAMOND:
                event.getEntity().setItemStack(new ItemStack(Material.DIAMOND, (int) this.moduleConfiguration.get("diamond")));
                flag = true;
                break;

            case EMERALD:
                event.getEntity().setItemStack(new ItemStack(Material.EMERALD, (int) this.moduleConfiguration.get("emerald")));
                flag = true;
                break;

            case INK_SACK:
                if (event.getEntity().getItemStack().getDurability() == 4)
                {
                    event.getEntity().setItemStack(new ItemStack(Material.INK_SACK, event.getEntity().getItemStack().getAmount() + this.random.nextInt(5) + 1, (short) 4));
                    flag = true;
                }
                break;

            case QUARTZ:
            	flag = true;
                break;

            default:
                break;
        }

        if (flag)
            event.getEntity().setItemStack(this.addMeta(event.getEntity().getItemStack()));

        this.spawnXPFromItemStack(event.getEntity(), event.getEntity().getItemStack());
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

    public ItemStack addMeta(ItemStack stack)
    {
        stack = new ItemStack(stack.getType(), stack.getAmount(), stack.getDurability());

        AttributeStorage storage = AttributeStorage.newTarget(stack, ID);
        storage.setData("dropped");

        ItemMeta itemMeta = stack.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        stack.setItemMeta(itemMeta);

        return storage.getTarget();
    }

    public boolean hasMeta(ItemStack stack)
    {
        if (stack == null)
            return false;

        ItemStack itemStack = new ItemStack(stack.clone());
        AttributeStorage storage = AttributeStorage.newTarget(itemStack, ID);

        return storage.getData("").equals("dropped");
    }

    public boolean isDoubledType(Material name)
    {
        switch(name)
        {
            case COAL:
            case IRON_INGOT:
            case GOLD_INGOT:
            case DIAMOND:
            case EMERALD:
                return true;

            default:
        }
        return false;
    }

    private void spawnXPFromItemStack(Entity entity, ItemStack ore)
    {
        World world = ((CraftEntity) entity).getHandle().getWorld();

        int i = 0;

        switch (ore.getType())
        {
            case QUARTZ:
            	i = MathHelper.nextInt(world.random, 2, 5);
            	break;

            case INK_SACK:
                if (ore.getDurability() == 4)
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
