package net.samagames.survivalapi.modules.gameplay;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.game.SurvivalPlayer;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.Map;

/**
 * SuperheroesModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class SuperheroesModule extends AbstractSurvivalModule
{
    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public SuperheroesModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * Give effects to all players
     *
     * @param game Game instance
     */
    @Override
    public void onGameStart(SurvivalGame game)
    {
        for (SurvivalPlayer player : (Collection<SurvivalPlayer>)game.getInGamePlayers().values())
        {
            Player p = player.getPlayerIfOnline();
            p.addPotionEffect(PotionEffectType.SPEED.createEffect(Integer.MAX_VALUE, 1));
            p.addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(Integer.MAX_VALUE, 1));
            p.addPotionEffect(PotionEffectType.DAMAGE_RESISTANCE.createEffect(Integer.MAX_VALUE, 2));
            p.addPotionEffect(PotionEffectType.JUMP.createEffect(Integer.MAX_VALUE, 4));
            p.setMaxHealth(32);
            p.setHealth(32);
        }
    }

    /**
     * Cancel fall damages
     *
     * @param event Event instance
     */
    @EventHandler
    public void onDamage(EntityDamageEvent event)
    {
        if (event.getEntityType() == EntityType.PLAYER && event.getCause() == EntityDamageEvent.DamageCause.FALL)
        {
            event.setCancelled(true);
            event.setDamage(0);
        }
    }
}
