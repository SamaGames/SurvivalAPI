package net.samagames.survivalapi.game;

import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DamageReporter
{
    private final Map<EntityType, Double> entityDamages;
    private final Map<UUID, Double> playerDamages;

    public DamageReporter()
    {
        this.entityDamages = new HashMap<>();
        this.playerDamages = new HashMap<>();
    }

    public void addPlayerDamages(UUID damaged, double damages)
    {
        double total = damages;

        if (this.playerDamages.containsKey(damaged))
            total += this.playerDamages.get(damaged);

        this.playerDamages.remove(damaged);
        this.playerDamages.put(damaged, total);
    }

    public void addEntityDamages(EntityType damaged, double damages)
    {
        double total = damages;

        if (this.entityDamages.containsKey(damaged))
            total += this.entityDamages.get(damaged);

        this.entityDamages.remove(damaged);
        this.entityDamages.put(damaged, total);
    }

    public double getTotalPlayerDamages()
    {
        double total = 0.0D;

        for (double damages : this.playerDamages.values())
            total += damages;

        return Math.floor(total * 10) / 10D;
    }

    public Map<EntityType, Double> getEntityDamages()
    {
        return this.entityDamages;
    }

    public Map<UUID, Double> getPlayerDamages()
    {
        return this.playerDamages;
    }
}
