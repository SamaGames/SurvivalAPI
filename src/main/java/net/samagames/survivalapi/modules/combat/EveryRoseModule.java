package net.samagames.survivalapi.modules.combat;

import net.samagames.api.games.GamePlayer;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Map;

/**
 * EveryRoseModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class EveryRoseModule extends AbstractSurvivalModule
{
    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public EveryRoseModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * Give a Thorns 3 gold chestplate to all the players
     *
     * @param game Game
     */
    @Override
    public void onGameStart(SurvivalGame game)
    {
        ItemStack chestplate = new ItemStack(Material.GOLD_CHESTPLATE, 1);
        chestplate.addUnsafeEnchantment(Enchantment.THORNS, 3);

        for (GamePlayer player : (Collection<GamePlayer>) game.getInGamePlayers().values())
        {
            Player player1 = player.getPlayerIfOnline();
            if (player1 != null)
                player1.getInventory().setChestplate(chestplate);
        }
    }
}
