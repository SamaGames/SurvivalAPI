package net.samagames.survivalapi.modules.gameplay;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.tools.ParticleEffect;
import org.bukkit.entity.Player;

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
    private final UUID a;
    private final UUID b;

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

        this.a = UUID.fromString("012bee10-0032-42e2-9f3c-9c6b4e4f1fff");
        this.b = UUID.fromString("b158fe72-dcac-49da-ac60-99feb4e29a8f");

        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this, 20L, 20L);
    }

    @Override
    public void run()
    {
        Player playerA = this.plugin.getServer().getPlayer(this.a);
        Player playerB = this.plugin.getServer().getPlayer(this.b);

        if (playerA != null && playerB != null)
        {
            for (Player player : this.plugin.getServer().getOnlinePlayers())
            {
                ParticleEffect.HEART.display(1.5F, 1.5F, 1.5F, 0.5F, 3, playerA.getLocation(), player);
                ParticleEffect.HEART.display(1.5F, 1.5F, 1.5F, 0.5F, 3, playerB.getLocation(), player);
            }
        }
    }
}
