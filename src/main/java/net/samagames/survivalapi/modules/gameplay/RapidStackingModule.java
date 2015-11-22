package net.samagames.survivalapi.modules.gameplay;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.survivalapi.modules.utility.DropTaggingModule;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class RapidStackingModule extends AbstractSurvivalModule
{
    private final Random random;

    public RapidStackingModule(SurvivalPlugin plugin, SurvivalAPI api, HashMap<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        this.random = new Random();
    }

    /**
     * Delete different type of certains blocks
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

        if (material == Material.LOG || material == Material.LOG_2)
            event.getEntity().setItemStack(new ItemStack(Material.LOG, event.getEntity().getItemStack().getAmount()));
        else if (material == Material.COBBLESTONE)
            event.getEntity().setItemStack(new ItemStack(Material.COBBLESTONE, event.getEntity().getItemStack().getAmount()));
        else if (material == Material.STONE)
            event.getEntity().setItemStack(new ItemStack(Material.COBBLESTONE, event.getEntity().getItemStack().getAmount()));
        else if (material == Material.SAPLING)
            event.getEntity().remove();
        else if (material == Material.SUGAR_CANE)
            event.getEntity().setItemStack(new ItemStack(Material.SUGAR_CANE, this.random.nextInt(3) + 1));
    }

    @Override
    public ArrayList<Class<? extends AbstractSurvivalModule>> getRequiredModules()
    {
        ArrayList<Class<? extends AbstractSurvivalModule>> requiredModules = new ArrayList<>();

        requiredModules.add(DropTaggingModule.class);

        return requiredModules;
    }
}
