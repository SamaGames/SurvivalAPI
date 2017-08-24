package net.samagames.survivalapi.game;

import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/*
 * This file is part of SurvivalAPI.
 *
 * SurvivalAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SurvivalAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SurvivalAPI.  If not, see <http://www.gnu.org/licenses/>.
 */
public class DamageReporter
{
    private static SurvivalGame game;

    private final UUID owner;
    private final Map<EntityType, Double> entityDamages;
    private final Map<UUID, Double> playerDamages;

    public DamageReporter(UUID owner)
    {
        this.owner = owner;
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

        if (game.getSurvivalGameStatisticsHelper() != null)
            game.getSurvivalGameStatisticsHelper().increaseDamages(this.owner, damages);
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

    public static void setGame(SurvivalGame instance)
    {
        game = instance;
    }
}
