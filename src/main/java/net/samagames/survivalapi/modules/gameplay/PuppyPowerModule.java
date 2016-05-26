package net.samagames.survivalapi.modules.gameplay;

import net.samagames.api.games.GamePlayer;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.tools.MojangShitUtils;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Map;

/**
 * PuppyPowerModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class PuppyPowerModule extends AbstractSurvivalModule
{
    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public PuppyPowerModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * Give 64 bones, 64 rotten flesh and 64 wolf eggs to all the players
     *
     * @param game Game
     */
    @Override
    public void onGameStart(SurvivalGame game)
    {
        ItemStack eggs = MojangShitUtils.getMonsterEgg(EntityType.WOLF);
        eggs.setAmount(64);
        for (GamePlayer player : (Collection<GamePlayer>) game.getInGamePlayers().values())
        {
            Player bplayer = player.getPlayerIfOnline();
            if (bplayer == null)
                continue ;
            bplayer.getInventory().addItem(new ItemStack(Material.BONE, 64));
            bplayer.getInventory().addItem(new ItemStack(Material.ROTTEN_FLESH, 64));
            bplayer.getInventory().addItem(eggs);
        }
    }
}

