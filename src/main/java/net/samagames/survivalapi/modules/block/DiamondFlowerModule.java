package net.samagames.survivalapi.modules.block;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.survivalapi.modules.utility.DropTaggingModule;
import net.samagames.survivalapi.utils.Meta;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class DiamondFlowerModule extends AbstractSurvivalModule
{
    private final Random random;

    public DiamondFlowerModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        this.random = new Random();
    }

    /**
     * Chance to drop a diamond when a player breaks a flower
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

        if (event.getEntity().getItemStack().getType() != Material.YELLOW_FLOWER && event.getEntity().getItemStack().getType() != Material.RED_ROSE)
            return;

        if (this.random.nextInt(100) <= 30)
            event.getEntity().getWorld().dropItemNaturally(event.getLocation(), verifyStack(new ItemStack(Material.DIAMOND, 1)));
    }

    private ItemStack verifyStack(ItemStack stack)
    {
        return Meta.addMeta(stack);
    }

    @Override
    public List<Class<? extends AbstractSurvivalModule>> getRequiredModules()
    {
        ArrayList<Class<? extends AbstractSurvivalModule>> requiredModules = new ArrayList<>();

        requiredModules.add(DropTaggingModule.class);

        return requiredModules;
    }
}
