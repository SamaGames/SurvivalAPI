package net.samagames.survivalapi.modules.gameplay;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.tools.ParticleEffect;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * LoveMachineModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class LoveMachineModule extends AbstractSurvivalModule implements Runnable
{
    private final List<Pair<UUID, UUID>> couples;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public LoveMachineModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);

        this.couples = new ArrayList<>();

        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this, 20L, 20L);
    }

    @Override
    public void run()
    {
        for (Pair<UUID, UUID> entry : couples)
        {
            Player playerA;
            Player playerB;

            if ((playerA = this.plugin.getServer().getPlayer(entry.getLeft())) != null
                    && (playerB = this.plugin.getServer().getPlayer(entry.getRight())) != null)
            {
                for (Player player : this.plugin.getServer().getOnlinePlayers())
                {
                    ParticleEffect.HEART.display(1.5F, 1.5F, 1.5F, 0.5F, 3, playerA.getLocation(), player);
                    ParticleEffect.HEART.display(1.5F, 1.5F, 1.5F, 0.5F, 3, playerB.getLocation(), player);
                }
            }
        }
    }
}
