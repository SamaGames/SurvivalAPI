package net.samagames.survivalapi.modules.utility;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;

public class DropTaggingModule extends AbstractSurvivalModule
{
    public DropTaggingModule(SurvivalPlugin plugin, SurvivalAPI api, HashMap<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * Add a metadata to the item thrown by a player
     *
     * @param event Event
     */
    @EventHandler
    public void onPlayerItemDrop(PlayerDropItemEvent event)
    {
        if (!event.getItemDrop().hasMetadata("playerDrop"))
            event.getItemDrop().setMetadata("playerDrop", new FixedMetadataValue(this.plugin, true));
    }
}
