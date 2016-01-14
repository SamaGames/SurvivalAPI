package net.samagames.survivalapi.modules.gameplay;

import net.samagames.api.SamaGamesAPI;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.game.SurvivalPlayer;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class ConstantPotionModule extends AbstractSurvivalModule
{
    private final List<PotionEffect> potionEffects;

    public ConstantPotionModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        Validate.notNull(moduleConfiguration, "Configuration cannot be null!");

        this.potionEffects = (ArrayList<PotionEffect>) this.moduleConfiguration.get("potion-effects");
    }

    /**
     * Give the configured effects on game's start
     *
     * @param game Event
     */
    @Override
    public void onGameStart(SurvivalGame game)
    {
        ((Collection<SurvivalPlayer>) game.getInGamePlayers().values()).stream().filter(player -> player.getPlayerIfOnline() != null).forEach(player -> this.setEffectsOnPlayer(player.getPlayerIfOnline()));
    }

    /**
     * Set the effect on the player when he consumes
     * a milk bucket
     *
     * @param event Event
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemConsume(PlayerItemConsumeEvent event)
    {
        if (event.getItem().getType() == Material.MILK_BUCKET)
            this.setEffectsOnPlayer(event.getPlayer());
    }

    private void setEffectsOnPlayer(Player player)
    {
        this.plugin.getServer().getScheduler().runTask(this.plugin, () ->
        {
            if (player == null || !player.isOnline())
                return;

            for (PotionEffect effect : this.potionEffects)
            {
                player.addPotionEffect(effect, true);
            }
        });
    }

    public static class ConfigurationBuilder
    {
        private final ArrayList<PotionEffect> potionEffects;

        public ConfigurationBuilder()
        {
            this.potionEffects = new ArrayList<>();
        }

        public Map<String, Object> build()
        {
            HashMap<String, Object> moduleConfiguration = new HashMap<>();

            moduleConfiguration.put("potion-effects", this.potionEffects);

            return moduleConfiguration;
        }

        public ConfigurationBuilder addPotionEffect(PotionEffectType potionEffectType, int level)
        {
            this.potionEffects.add(new PotionEffect(potionEffectType, Integer.MAX_VALUE, level));
            return this;
        }
    }
}
