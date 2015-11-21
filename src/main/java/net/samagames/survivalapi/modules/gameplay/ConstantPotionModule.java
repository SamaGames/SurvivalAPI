package net.samagames.survivalapi.modules.gameplay;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.game.SurvivalPlayer;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.apache.commons.lang.Validate;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class ConstantPotionModule extends AbstractSurvivalModule
{
    public ConstantPotionModule(SurvivalPlugin plugin, SurvivalAPI api, HashMap<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        Validate.notNull(moduleConfiguration, "Configuration cannot be null!");
    }

    /**
     * Give the configured effects on game's start
     *
     * @param game Event
     */
    @Override
    public void onGameStart(SurvivalGame game)
    {
        ArrayList<PotionEffect> potionEffects = (ArrayList<PotionEffect>) this.moduleConfiguration.get("potion-effects");

        for (SurvivalPlayer player : (Collection<SurvivalPlayer>) game.getInGamePlayers().values())
            player.getPlayerIfOnline().addPotionEffects(potionEffects);
    }

    public static class ConfigurationBuilder
    {
        private ArrayList<PotionEffect> potionEffects;

        public ConfigurationBuilder()
        {
            this.potionEffects = new ArrayList<>();
        }

        public HashMap<String, Object> build()
        {
            HashMap<String, Object> moduleConfiguration = new HashMap<>();

            moduleConfiguration.put("potion-effects", this.potionEffects);

            return moduleConfiguration;
        }

        public ConfigurationBuilder addPotionEffect(PotionEffect potionEffect)
        {
            this.potionEffects.add(potionEffect);
            return this;
        }
    }
}
