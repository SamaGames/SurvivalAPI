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

public class RottenPotions extends AbstractSurvivalModule
{
    private final Random random;

    public RottenPotions(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
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
            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.values()[this.random.nextInt(PotionEffectType.values().length)], 10, 1));
    }
}
