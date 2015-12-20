package net.samagames.survivalapi.modules.gameplay;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.survivalapi.modules.utility.DropTaggingModule;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class RapidFoodModule extends AbstractSurvivalModule
{
    private final Random random;

    public RapidFoodModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        this.random = new Random();
    }

    /**
     * Bread dropping
     *
     * @param event Event
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        Material material = event.getBlock().getType();

        if (material == Material.DEAD_BUSH)
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.BREAD, this.random.nextInt(3) + 1));
    }

    /**
     * Soup dropping
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

        if (material == Material.BROWN_MUSHROOM || material == Material.RED_MUSHROOM)
            event.getEntity().setItemStack(new ItemStack(Material.MUSHROOM_SOUP, 2));
        else if (material == Material.POTATO_ITEM)
            event.getEntity().setItemStack(new ItemStack(Material.BAKED_POTATO, 1));
        else if (material == Material.CARROT_ITEM)
            event.getEntity().setItemStack(new ItemStack(Material.GOLDEN_CARROT, 1));
        else if (material == Material.WHEAT)
            event.getEntity().setItemStack(new ItemStack(Material.BREAD, 1));
        else if (material == Material.PUMPKIN)
            event.getEntity().setItemStack(new ItemStack(Material.PUMPKIN_PIE, 1));
    }

    /**
     * Drop cooked food
     * 
     * @param event Event
     */
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event)
    {
        LivingEntity entity = event.getEntity();
        List<ItemStack> newDrops = null;

        if (entity instanceof Cow || entity instanceof Horse)
        {
            newDrops = new ArrayList<>();

            for (ItemStack stack : event.getDrops())
            {
                if (stack.getType() == Material.RAW_BEEF)
                    newDrops.add(new ItemStack(Material.COOKED_BEEF, stack.getAmount() * 2));
                else if (stack.getType() == Material.LEATHER)
                    newDrops.add(new ItemStack(Material.LEATHER, stack.getAmount() * 2));
            }
        }
        else if (entity instanceof Sheep)
        {
            newDrops = event.getDrops().stream().filter(stack -> stack.getType() == Material.MUTTON).map(stack -> new ItemStack(Material.COOKED_MUTTON, stack.getAmount() * 2)).collect(Collectors.toList());

            if (this.random.nextInt(32) >= 16)
                newDrops.add(new ItemStack(Material.LEATHER, this.random.nextInt(5) + 1));
            if (this.random.nextInt(32) >= 16)
                newDrops.add(new ItemStack(Material.STRING, this.random.nextInt(2) + 1));
        }
        else if (entity instanceof Pig)
        {
            newDrops = event.getDrops().stream().filter(stack -> stack.getType() == Material.PORK).map(stack -> new ItemStack(Material.GRILLED_PORK, stack.getAmount() * 2)).collect(Collectors.toList());
            
            if (this.random.nextInt(32) >= 16)
                newDrops.add(new ItemStack(Material.LEATHER, this.random.nextInt(5) + 1));
        }
        else if (entity instanceof Rabbit)
        {
            newDrops = event.getDrops().stream().filter(stack -> stack.getType() == Material.RABBIT).map(stack -> new ItemStack(Material.COOKED_RABBIT, stack.getAmount() * 2)).collect(Collectors.toList());
        }
        else if (entity instanceof Chicken)
        {
            newDrops = new ArrayList<>();
            
            for (ItemStack stack : event.getDrops())
            {
                if (stack.getType() == Material.RAW_CHICKEN)
                    newDrops.add(new ItemStack(Material.COOKED_CHICKEN, stack.getAmount() * 2));
                else if (stack.getType() == Material.FEATHER)
                    newDrops.add(new ItemStack(Material.ARROW, stack.getAmount()));
            }
        }
        else if (entity instanceof Squid)
        {
            newDrops = new ArrayList<>();
            newDrops.add(new ItemStack(Material.COOKED_FISH, this.random.nextInt(5) + 1));
        }
        else if (entity instanceof Skeleton)
        {
            newDrops = new ArrayList<>();
            
            for (ItemStack stack : event.getDrops())
            {
                if (stack.getType() == Material.ARROW)
                {
                    newDrops.add(new ItemStack(Material.ARROW, stack.getAmount() * 2));
                }
                if (stack.getType() == Material.BOW)
                {
                    stack.setDurability((short) 0);
                    newDrops.add(stack);
                }
            }
        }
        else if (entity instanceof Zombie)
        {
            newDrops = event.getDrops().stream().filter(stack -> stack.getType() == Material.ROTTEN_FLESH).map(stack -> new ItemStack(Material.COOKED_BEEF, stack.getAmount() * 2)).collect(Collectors.toList());
        }
        else if (entity instanceof Bat)
        {
            newDrops = new ArrayList<>();
            newDrops.add(new ItemStack(Material.COOKED_MUTTON, this.random.nextInt(5) + 1));
        }

        if (newDrops != null)
        {
            event.getDrops().clear();
            event.getDrops().addAll(newDrops);
        }

        event.setDroppedExp(event.getDroppedExp() * 2);
    }

    @Override
    public List<Class<? extends AbstractSurvivalModule>> getRequiredModules()
    {
        List<Class<? extends AbstractSurvivalModule>> requiredModules = new ArrayList<>();

        requiredModules.add(DropTaggingModule.class);

        return requiredModules;
    }
}
