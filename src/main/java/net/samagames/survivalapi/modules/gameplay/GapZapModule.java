package net.samagames.survivalapi.modules.gameplay;

import net.samagames.api.SamaGamesAPI;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

/**
 * GapZapModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class GapZapModule extends AbstractSurvivalModule
{
    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public GapZapModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * Cancel regen on damage
     *
     * @param event Event
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageEvent event)
    {
        if (((SurvivalGame)SamaGamesAPI.get().getGameManager().getGame()).isDamagesActivated() && event.getEntityType() == EntityType.PLAYER)
            ((Player)event.getEntity()).removePotionEffect(PotionEffectType.REGENERATION);
    }
}

