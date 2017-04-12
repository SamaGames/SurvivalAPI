package net.samagames.survivalapi.modules.gameplay;

import net.samagames.api.games.GamePlayer;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * GoneFishingModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class GoneFishingModule extends AbstractSurvivalModule
{
    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public GoneFishingModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * Give a cheated fishing rod to all the players
     *
     * @param game Game
     */
    @Override
    public void onGameStart(SurvivalGame game)
    {
        ItemStack rod = new ItemStack(Material.FISHING_ROD);
        rod.addUnsafeEnchantment(Enchantment.LUCK, 255);
        rod.addUnsafeEnchantment(Enchantment.DURABILITY, 255);
        ItemMeta meta = rod.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        rod.setItemMeta(meta);

        for (GamePlayer player : (Collection<GamePlayer>) game.getInGamePlayers().values())
        {
            Player p = player.getPlayerIfOnline();

            if (p != null)
                p.getInventory().addItem(rod);
        }
    }

    @Override
    public List<Class<? extends AbstractSurvivalModule>> getRequiredModules()
    {
        List<Class<? extends AbstractSurvivalModule>> list = new ArrayList<>();
        list.add(InfiniteEnchanterModule.class);
        return list;
    }
}
