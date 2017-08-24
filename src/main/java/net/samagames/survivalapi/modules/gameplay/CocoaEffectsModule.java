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
public class CocoaEffectsModule extends AbstractSurvivalModule
{
    private final int bonusTime;
    private final int penaltyTime;

    private final ItemStack cocoa;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public CocoaEffectsModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        Validate.notNull(moduleConfiguration, "Configuration cannot be null!");

        this.bonusTime = (int) moduleConfiguration.get("bonus-time");
        this.penaltyTime = (int) moduleConfiguration.get("penalty-time");

        this.cocoa = new ItemStack(Material.INK_SACK, 5, (short) 3);
        ItemMeta meta = this.cocoa.getItemMeta();
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Chaque utilisation vous donne " + this.bonusTime + " secondes", ChatColor.GRAY + " de force et vitesse améliorées", ChatColor.GRAY + " mais les effets contraires par la suite", ChatColor.GRAY + " pendant " + this.penaltyTime + " secondes."));
        meta.setDisplayName(ChatColor.AQUA + "Coco");
        this.cocoa.setItemMeta(meta);
    }

    /**
     * Give 5 coca beans to all the players
     *
     * @param game Game
     */
    @Override
    public void onGameStart(SurvivalGame game)
    {
        for (GamePlayer player : (Collection<GamePlayer>) game.getInGamePlayers().values())
        {
            Player p = player.getPlayerIfOnline();

            if (p != null)
                p.getInventory().addItem(this.cocoa);
        }
    }

    /**
     * Give speed 1 & strenght 1 effect to all players, then give them slowness & weakness after 30sec.
     * @param event Event
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        if (event.getItem() != null && event.getItem().isSimilar(this.cocoa))
        {
            event.getPlayer().addPotionEffect(PotionEffectType.SPEED.createEffect(this.bonusTime * 20, 1));
            event.getPlayer().addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(this.bonusTime * 20, 1));

            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () ->
            {
                event.getPlayer().addPotionEffect(PotionEffectType.SLOW.createEffect(this.penaltyTime * 20, 1));
                event.getPlayer().addPotionEffect(PotionEffectType.WEAKNESS.createEffect(this.penaltyTime * 20, 1));
            }, this.bonusTime * 20);

            if (event.getItem().getAmount() > 1)
                event.getItem().setAmount(event.getItem().getAmount() - 1);
            else
                event.getPlayer().setItemInHand(new ItemStack(Material.AIR));

            event.setCancelled(true);
        }
    }

    public static class ConfigurationBuilder implements IConfigurationBuilder
    {
        private int bonusTime;
        private int penaltyTime;

        public ConfigurationBuilder()
        {
            this.bonusTime = 30;
            this.penaltyTime = 30;
        }

        @Override
        public Map<String, Object> build()
        {
            Map<String, Object> moduleConfiguration = new HashMap<>();

            moduleConfiguration.put("bonus-time", this.bonusTime);
            moduleConfiguration.put("penalty-time", this.penaltyTime);

            return moduleConfiguration;
        }

        @Override
        public Map<String, Object> buildFromJson(Map<String, JsonElement> configuration) throws Exception
        {
            if (configuration.containsKey("bonus-time"))
                this.setBonusTime(configuration.get("bonus-time").getAsInt());

            if (configuration.containsKey("penalty-time"))
                this.setPenaltyTime(configuration.get("penalty-time").getAsInt());

            return this.build();
        }

        public CocoaEffectsModule.ConfigurationBuilder setBonusTime(int bonusTime)
        {
            this.bonusTime = bonusTime;
            return this;
        }

        public CocoaEffectsModule.ConfigurationBuilder setPenaltyTime(int penaltyTime)
        {
            this.penaltyTime = penaltyTime;
            return this;
        }
    }
}

