package net.samagames.survivalapi.modules.combat;

import com.google.gson.JsonElement;
import net.samagames.api.games.GamePlayer;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.survivalapi.modules.IConfigurationBuilder;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * BombersModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class BombersModule extends AbstractSurvivalModule
{
    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public BombersModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        Validate.notNull(moduleConfiguration, "Configuration cannot be null!");
    }

    /**
     * Give an unbreakable flint and steel to all the players
     *
     * @param game Game
     */
    @Override
    public void onGameStart(SurvivalGame game)
    {
        ItemStack flintAndSteel = new ItemStack(Material.FLINT_AND_STEEL, 1);
        flintAndSteel.addUnsafeEnchantment(Enchantment.DURABILITY, 10);

        for (GamePlayer player : (Collection<GamePlayer>) game.getInGamePlayers().values())
        {
            Player p = player.getPlayerIfOnline();

            if (p != null)
                p.getInventory().addItem(flintAndSteel);
        }
    }

    /**
     * Drop a TNT when a entity die
     *
     * @param event Event
     */
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event)
    {
        event.getDrops().add(new ItemStack(Material.TNT, (int) this.moduleConfiguration.get("tnt-dropped")));
    }

    public static class ConfigurationBuilder implements IConfigurationBuilder
    {
        private int tnt;

        public ConfigurationBuilder()
        {
            this.tnt = 1;
        }

        @Override
        public Map<String, Object> build()
        {
            Map<String, Object> moduleConfiguration = new HashMap<>();

            moduleConfiguration.put("tnt-dropped", this.tnt);

            return moduleConfiguration;
        }

        @Override
        public Map<String, Object> buildFromJson(Map<String, JsonElement> configuration) throws Exception
        {
            if (configuration.containsKey("tnt-dropped"))
                this.setTNTDropped(configuration.get("tnt-dropped").getAsInt());

            return this.build();
        }

        public BombersModule.ConfigurationBuilder setTNTDropped(int tnt)
        {
            this.tnt = tnt;
            return this;
        }
    }
}
