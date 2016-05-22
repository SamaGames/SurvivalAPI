package net.samagames.survivalapi.modules.block;

import net.samagames.api.SamaGamesAPI;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.*;

/**
 * PainfullStonesModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class PainfullStonesModule extends AbstractSurvivalModule
{
    private Set<UUID> damaged;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public PainfullStonesModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        this.damaged = new HashSet<>();
    }

    /**
     * Damage player when walking on gravel if he does not have boots
     * @param event Move event
     */
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        if (!((SurvivalGame) SamaGamesAPI.get().getGameManager().getGame()).isDamagesActivated())
            return ;
        Block block = event.getTo().clone().subtract(0, 1, 0).getBlock();
        if (block != null && block.getType() == Material.GRAVEL
                && (event.getPlayer().getInventory().getBoots() == null || event.getPlayer().getInventory().getBoots().getType() == Material.AIR)
                && !damaged.contains(event.getPlayer().getUniqueId()))
        {

            damaged.add(event.getPlayer().getUniqueId());
            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> damaged.remove(event.getPlayer().getUniqueId()), 20L);
            event.getPlayer().damage(1D);
        }
    }
}

