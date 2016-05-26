package net.samagames.survivalapi.modules.gameplay;

import net.samagames.api.games.GamePlayer;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Map;

/**
 * HighwayToHellModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class HighwayToHellModule extends AbstractSurvivalModule
{
    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public HighwayToHellModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * Give all player 14 obsidian, 1 flint and steel, 1 lava bucket, 1 iron pickaxe and 64 steaks
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
                continue ;
            p.getInventory().addItem(new ItemStack(Material.OBSIDIAN, 4));
            p.getInventory().addItem(new ItemStack(Material.FLINT_AND_STEEL));
            p.getInventory().addItem(new ItemStack(Material.LAVA_BUCKET));
            p.getInventory().addItem(new ItemStack(Material.IRON_PICKAXE));
            p.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 64));
        }
    }
}

