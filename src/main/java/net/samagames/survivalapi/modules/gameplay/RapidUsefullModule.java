package net.samagames.survivalapi.modules.gameplay;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.survivalapi.modules.utility.DropTaggingModule;
import org.bukkit.Material;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RapidUsefullModule extends AbstractSurvivalModule
{
    public RapidUsefullModule(SurvivalPlugin plugin, SurvivalAPI api, HashMap<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
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

        if (event.getEntity().hasMetadata("playerDrop"))
            return;

        Material material = event.getEntity().getItemStack().getType();

        if (material == Material.CACTUS)
            event.getEntity().setItemStack(new ItemStack(Material.LOG, 2));
        else if (material == Material.SAND)
            event.getEntity().setItemStack(new ItemStack(Material.GLASS_BOTTLE, 1));
        else if (material == Material.GRAVEL)
            event.getEntity().setItemStack(new ItemStack(Material.FLINT, 1));
    }

    /**
     * Drop some utilities
     *
     * @param event Event
     */
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event)
    {
        LivingEntity entity = event.getEntity();
        List<ItemStack> newDrops = null;

        if (entity instanceof Creeper)
        {
            newDrops = new ArrayList<>();

            for (ItemStack stack : event.getDrops())
                if (stack.getType() == Material.SULPHUR)
                    newDrops.add(new ItemStack(Material.TNT, stack.getAmount() * 2));
        }

        if (newDrops != null)
        {
            event.getDrops().clear();
            event.getDrops().addAll(newDrops);
        }

        event.setDroppedExp(event.getDroppedExp() * 2);
    }

    @Override
    public ArrayList<Class<? extends AbstractSurvivalModule>> getRequiredModules()
    {
        ArrayList<Class<? extends AbstractSurvivalModule>> requiredModules = new ArrayList<>();

        requiredModules.add(DropTaggingModule.class);

        return requiredModules;
    }
}
