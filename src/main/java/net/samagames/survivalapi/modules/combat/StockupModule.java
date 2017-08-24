package net.samagames.survivalapi.modules.combat;

import com.google.gson.JsonElement;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.samagames.api.SamaGamesAPI;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.game.SurvivalPlayer;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.survivalapi.modules.IConfigurationBuilder;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

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
        Validate.notNull(moduleConfiguration, "Configuration cannot be null!");
    }

    /**
     * Give x absorption to all players on deaths
     *
     * @param event The player death event instance
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        for (SurvivalPlayer player : (Collection<SurvivalPlayer>) SamaGamesAPI.get().getGameManager().getGame().getInGamePlayers().values())
        {
            if (!player.getUUID().equals(event.getEntity().getUniqueId()))
            {
                Player p = player.getPlayerIfOnline();

                if (p == null)
                    continue;

                EntityPlayer entityPlayer = ((CraftPlayer) p).getHandle();
                entityPlayer.setAbsorptionHearts(entityPlayer.getAbsorptionHearts() + (int) this.moduleConfiguration.get("hearts"));
            }
        }
    }

    public static class ConfigurationBuilder implements IConfigurationBuilder
    {
        private int hearts;

        public ConfigurationBuilder()
        {
            this.hearts = 1;
        }

        @Override
        public Map<String, Object> build()
        {
            Map<String, Object> moduleConfiguration = new HashMap<>();

            moduleConfiguration.put("hearts", this.hearts);

            return moduleConfiguration;
        }

        @Override
        public Map<String, Object> buildFromJson(Map<String, JsonElement> configuration) throws Exception
        {
            if (configuration.containsKey("hearts"))
                this.setHeartsAmount(configuration.get("hearts").getAsInt());

            return this.build();
        }

        public StockupModule.ConfigurationBuilder setHeartsAmount(int hearts)
        {
            this.hearts = hearts;
            return this;
        }
    }
}
