package net.samagames.survivalapi.modules.gameplay;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.Random;

/**
 * RottenPotionsModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class RottenPotionsModule extends AbstractSurvivalModule
{
    private final Random random;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public RottenPotionsModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);

        this.random = new Random();
    }

    /**
     * Give a random potion effect when a player eat rotten flesh
     *
     * @param event Event
     */
    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event)
    {
        if (event.getItem().getType() == Material.ROTTEN_FLESH)
            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.values()[this.random.nextInt(PotionEffectType.values().length)], 200, 1));
    }
}
