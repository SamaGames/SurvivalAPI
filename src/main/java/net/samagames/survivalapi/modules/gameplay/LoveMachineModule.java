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

        this.couples.add(new ImmutablePair<>(UUID.fromString("012bee10-0032-42e2-9f3c-9c6b4e4f1fff"), UUID.fromString("b158fe72-dcac-49da-ac60-99feb4e29a8f"))); // Wosty & Pepette_
        this.couples.add(new ImmutablePair<>(UUID.fromString("29b2b527-1b59-45df-b7b0-d5ab20d8731a"), UUID.fromString("dfd16cea-d6d8-4f51-aade-8c7ad157c93f"))); // Blue & Maela
        this.couples.add(new ImmutablePair<>(UUID.fromString("6a7f2000-5853-4934-981d-5077be5a0b50"), UUID.fromString("61d0de9f-a2a8-4d07-899a-f3ddf18240b5"))); // Thog & Mino

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
