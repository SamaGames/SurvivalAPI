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
 * InfiniteEnchanterModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class InfiniteEnchanterModule extends AbstractSurvivalModule
{
    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public InfiniteEnchanterModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * Give 128 bookshelves, thousands of xp levels, 64 tables and 64 anvils to all the players
     *
     * @param game Game
     */
    @Override
    public void onGameStart(SurvivalGame game)
    {
        ItemStack tables = new ItemStack(Material.ENCHANTMENT_TABLE, 64);
        ItemStack anvils = new ItemStack(Material.ANVIL, 64);
        ItemStack bookshelves = new ItemStack(Material.BOOKSHELF, 64);
        ItemStack lapis = new ItemStack(Material.INK_SACK, 64, (short)4);

        for (GamePlayer player : (Collection<GamePlayer>) game.getInGamePlayers().values())
        {
            Player p = player.getPlayerIfOnline();
            if (p == null)
                continue ;
            p.getInventory().addItem(tables);
            p.getInventory().addItem(anvils);
            p.getInventory().addItem(bookshelves);
            p.getInventory().addItem(lapis);
            p.setLevel(111111);
        }
    }
}
