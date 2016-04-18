package net.samagames.survivalapi.modules.gameplay;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.games.GamePlayer;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Map;

/**
 * PyroTechnicsModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class PyroTechnicsModule extends AbstractSurvivalModule
{
    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public PyroTechnicsModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * Give 1 water block to all the players
     *
     * @param game Game
     */
    @Override
    public void onGameStart(SurvivalGame game)
    {
        for (GamePlayer player : (Collection<GamePlayer>) game.getInGamePlayers().values())
            player.getPlayerIfOnline().getInventory().addItem(new ItemStack(Material.WATER_BUCKET));
    }

    /**
     * Fire player on damage
     * @param event Event
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageEvent event)
    {
        if (((SurvivalGame)SamaGamesAPI.get().getGameManager().getGame()).isDamagesActivated()
                && event.getEntityType() == EntityType.PLAYER
                && event.getCause() != EntityDamageEvent.DamageCause.FIRE
                && event.getCause() != EntityDamageEvent.DamageCause.FIRE_TICK)
            event.getEntity().setFireTicks(100);
    }
}

