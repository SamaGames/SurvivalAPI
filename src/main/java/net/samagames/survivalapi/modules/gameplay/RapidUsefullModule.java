package net.samagames.survivalapi.modules.gameplay;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.survivalapi.modules.utility.DropTaggingModule;
import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Tree;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class RapidUsefullModule extends AbstractSurvivalModule
{
    private final Random random;

    public RapidUsefullModule(SurvivalPlugin plugin, SurvivalAPI api, HashMap<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
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

        if (event.getEntity().hasMetadata("playerDrop"))
            return;

        Material material = event.getEntity().getItemStack().getType();

        switch(material)
        {
            case SAND:
                event.getEntity().setItemStack(new ItemStack(Material.GLASS_BOTTLE, 1));
                break;

            case SAPLING:
                double percent = ((Tree) event.getEntity().getItemStack().getData()).getSpecies().equals(TreeSpecies.GENERIC) ? 0.1 : 0.3;
                if (this.random.nextDouble() <= percent)
                    event.getEntity().setItemStack(new ItemStack(Material.APPLE));
                else
                    event.setCancelled(true);
                break;

            case GRAVEL:
            case FLINT:
                if (this.random.nextDouble() < 0.75)
                {
                    ItemStack loot = new ItemStack(Material.ARROW, 3);
                    event.getEntity().setItemStack(loot);
                }
                break;

            case CACTUS:
                event.getEntity().setItemStack(new ItemStack(Material.LOG, 2));
                break;

            case SUGAR_CANE:
                event.getEntity().setItemStack(new ItemStack(Material.SUGAR_CANE, 2));
                event.getEntity().setMetadata("playerDrop", new FixedMetadataValue(this.plugin, true));
                break;
        }
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
