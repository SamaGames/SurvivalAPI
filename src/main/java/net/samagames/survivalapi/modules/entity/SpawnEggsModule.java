package net.samagames.survivalapi.modules.entity;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.Map;
import java.util.Random;

/**
 * SpawnEggsModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class SpawnEggsModule extends AbstractSurvivalModule
{
    private static final EntityType[] TYPES = new EntityType[] {
            EntityType.CAVE_SPIDER, EntityType.COW, EntityType.CREEPER, EntityType.ENDERMAN, EntityType.ENDERMITE,
            EntityType.IRON_GOLEM, EntityType.MAGMA_CUBE, EntityType.OCELOT, EntityType.PIG,
            EntityType.PIG_ZOMBIE, EntityType.RABBIT, EntityType.SHEEP, EntityType.SILVERFISH, EntityType.SKELETON,
            EntityType.SLIME, EntityType.SNOWMAN, EntityType.SPIDER, EntityType.SQUID, EntityType.VILLAGER,
            EntityType.WOLF, EntityType.ZOMBIE, EntityType.HORSE, EntityType.MUSHROOM_COW,
            EntityType.WITHER_SKULL, EntityType.WITHER
    };
    private Random random;

    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public SpawnEggsModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
        this.random = new Random();
    }

    /**
     * Spawn mob on block hit
     *
     * @param event Event
     */
    @EventHandler
    public void onEntityLand(ProjectileHitEvent event)
    {
        onHit(event.getEntity());
    }

    /**
     * Spawn mob on mob hit
     *
     * @param event Event
     */
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event)
    {
        onHit(event.getDamager());
    }

    /**
     * Spawn random mob
     *
     * @param entity Source entity
     */
    private void onHit(Entity entity)
    {
        if (entity == null || !(entity instanceof Egg))
            return ;
        entity.getWorld().spawnEntity(entity.getLocation(), TYPES[random.nextInt(TYPES.length)]);
    }
}
