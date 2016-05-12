package net.samagames.survivalapi.modules.combat;

import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.samagames.api.SamaGamesAPI;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.game.SurvivalPlayer;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Collection;
import java.util.Map;

/**
 * StockupModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class StockupModule extends AbstractSurvivalModule
{
    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public StockupModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * Give 1 absorption to all players on deaths
     *
     * @param event The player death event instance
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        for (SurvivalPlayer survivalPlayer : (Collection<SurvivalPlayer>) SamaGamesAPI.get().getGameManager().getGame().getInGamePlayers().values())
            if (!survivalPlayer.getUUID().equals(event.getEntity().getUniqueId()))
            {
                EntityPlayer player = ((CraftPlayer)survivalPlayer.getPlayerIfOnline()).getHandle();
                player.setAbsorptionHearts(player.getAbsorptionHearts() + 1);
            }
    }
}
