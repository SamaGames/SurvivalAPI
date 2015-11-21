package net.samagames.survivalapi.modules.gameplay;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.tools.ParticleEffect;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class LoveMachineModule extends AbstractSurvivalModule implements Runnable
{
    private final UUID a;
    private final UUID b;

    public LoveMachineModule(SurvivalPlugin plugin, SurvivalAPI api, HashMap<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);

        this.a = UUID.fromString("29b2b527-1b59-45df-b7b0-d5ab20d8731a");
        this.b = UUID.fromString("dfd16cea-d6d8-4f51-aade-8c7ad157c93f");

        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this, 10L, 20L);
    }

    @Override
    public void run()
    {
        if (this.plugin.getServer().getPlayer(this.a) != null && this.plugin.getServer().getPlayer(this.b) != null)
        {
            Player playerA = this.plugin.getServer().getPlayer(this.a);
            Player playerB = this.plugin.getServer().getPlayer(this.b);

            for (Player player : this.plugin.getServer().getOnlinePlayers())
            {
                ParticleEffect.HEART.display(1.5F, 1.5F, 1.5F, 0.5F, 3, playerA.getLocation(), player);
                ParticleEffect.HEART.display(1.5F, 1.5F, 1.5F, 0.5F, 3, playerB.getLocation(), player);
            }
        }
    }
}
