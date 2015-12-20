package net.samagames.survivalapi.modules.block;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;

import java.util.Map;

public class HardObsidianModule extends AbstractSurvivalModule
{
    public HardObsidianModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * Set obsidian only breakable by a diamond pickage
     *
     * @param event Event
     */
    @EventHandler
    public void onBlockDamage(BlockDamageEvent event)
    {
        if (event.getBlock().getType() == Material.OBSIDIAN && event.getItemInHand().getType() != Material.DIAMOND_PICKAXE)
            event.setCancelled(true);
    }

    /**
     * Damage the diamond pickage if an obsidian block is broken
     *
     * @param event Event
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        if (event.getBlock().getType() == Material.OBSIDIAN)
            event.getPlayer().getItemInHand().setDurability((short) (event.getPlayer().getItemInHand().getDurability() - 500));
    }
}
