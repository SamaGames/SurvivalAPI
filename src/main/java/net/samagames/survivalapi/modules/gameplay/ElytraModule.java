package net.samagames.survivalapi.modules.gameplay;

import net.samagames.api.games.GamePlayer;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collection;
import java.util.Map;

/**
 * ElytraModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class ElytraModule extends AbstractSurvivalModule
{
    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public ElytraModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * Give elytra to players
     *
     * @param game Game
     */
    @Override
    public void onGameStart(SurvivalGame game)
    {
        ItemStack elytra = new ItemStack(Material.ELYTRA);
        ItemMeta itemMeta = elytra.getItemMeta();
        itemMeta.spigot().setUnbreakable(true);
        elytra.setItemMeta(itemMeta);

        for (GamePlayer player : (Collection<GamePlayer>) game.getInGamePlayers().values())
            player.getPlayerIfOnline().getInventory().setChestplate(elytra);
    }
}
