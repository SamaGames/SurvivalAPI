package net.samagames.survivalapi.modules.gameplay;

import com.google.gson.JsonElement;
import net.samagames.api.games.GamePlayer;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.survivalapi.modules.IConfigurationBuilder;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/*
 * This file is part of SurvivalAPI.
 *
 * SurvivalAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SurvivalAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SurvivalAPI.  If not, see <http://www.gnu.org/licenses/>.
 */
public class ChickenModule extends AbstractSurvivalModule
{
    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public ChickenModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        Validate.notNull(moduleConfiguration, "Configuration cannot be null!");
    }

    /**
     * Set everybody's life to 1.5 hearts and give notch apple
     *
     * @param game Game
     */
    @Override
    public void onGameStart(SurvivalGame game)
    {
        for (GamePlayer player : (Collection<GamePlayer>) game.getInGamePlayers().values())
        {
            Player p = player.getPlayerIfOnline();

            if (p == null)
                continue;

            p.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, (int) this.moduleConfiguration.get("golden-apples"), (short) 1));
            p.setHealth((double) this.moduleConfiguration.get("start-health"));
        }
    }

    public static class ConfigurationBuilder implements IConfigurationBuilder
    {
        private double startHealth;
        private int goldenApples;

        public ConfigurationBuilder()
        {
            this.startHealth = 3.0D;
        }

        @Override
        public Map<String, Object> build()
        {
            Map<String, Object> moduleConfiguration = new HashMap<>();

            moduleConfiguration.put("start-health", this.startHealth);
            moduleConfiguration.put("golden-apples", this.goldenApples);

            return moduleConfiguration;
        }

        @Override
        public Map<String, Object> buildFromJson(Map<String, JsonElement> configuration) throws Exception
        {
            if (configuration.containsKey("start-health"))
                this.setStartHealth(configuration.get("start-health").getAsDouble());

            if (configuration.containsKey("golden-apples"))
                this.setGoldenApples(configuration.get("golden-apples").getAsInt());

            return this.build();
        }

        public ChickenModule.ConfigurationBuilder setStartHealth(double startHealth)
        {
            this.startHealth = startHealth;
            return this;
        }

        public ChickenModule.ConfigurationBuilder setGoldenApples(int goldenApples)
        {
            this.goldenApples = goldenApples;
            return this;
        }
    }
}
