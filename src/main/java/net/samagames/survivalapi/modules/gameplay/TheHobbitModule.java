package net.samagames.survivalapi.modules.gameplay;

import com.google.gson.JsonElement;
import net.samagames.api.games.GamePlayer;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.survivalapi.modules.IConfigurationBuilder;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
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
public class TheHobbitModule extends AbstractSurvivalModule
{
    private final int bonusTime;

    private final ItemStack ring;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public TheHobbitModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        Validate.notNull(moduleConfiguration, "Configuration cannot be null!");

        this.bonusTime = (int) moduleConfiguration.get("bonus-time");

        this.ring = new ItemStack(Material.GOLD_NUGGET);
        ItemMeta meta = this.ring.getItemMeta();
        meta.setLore(Arrays.asList("Une fois utilisé, cet anneau vous", "permettra d'être invisible pendant", this.bonusTime + " secondes."));
        meta.setDisplayName(ChatColor.GOLD + "Anneau");
        this.ring.setItemMeta(meta);
    }

    /**
     * Give 1 ring to all the players
     *
     * @param game Game
     */
    @Override
    public void onGameStart(SurvivalGame game)
    {
        for (GamePlayer player : (Collection<GamePlayer>) game.getInGamePlayers().values())
        {
            Player player1 = player.getPlayerIfOnline();
            if (player1 != null)
                player1.getInventory().addItem(this.ring);
        }
    }

    /**
     * Give invisibility on ring use
     * @param event Event
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        if (event.getItem() != null && event.getItem().isSimilar(this.ring))
        {
            event.getPlayer().addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(this.bonusTime * 20, 1));
            event.getPlayer().setItemInHand(new ItemStack(Material.AIR));
            event.setCancelled(true);
        }
    }

    public static class ConfigurationBuilder implements IConfigurationBuilder
    {
        private int bonusTime;

        public ConfigurationBuilder()
        {
            this.bonusTime = 30;
        }

        @Override
        public Map<String, Object> build()
        {
            Map<String, Object> moduleConfiguration = new HashMap<>();

            moduleConfiguration.put("bonus-time", this.bonusTime);

            return moduleConfiguration;
        }

        @Override
        public Map<String, Object> buildFromJson(Map<String, JsonElement> configuration) throws Exception
        {
            if (configuration.containsKey("bonus-time"))
                this.setBonusTime(configuration.get("bonus-time").getAsInt());

            return this.build();
        }

        public TheHobbitModule.ConfigurationBuilder setBonusTime(int bonusTime)
        {
            this.bonusTime = bonusTime;
            return this;
        }
    }
}

