package net.samagames.survivalapi.modules.combat;

import net.minecraft.server.v1_9_R2.EntityTippedArrow;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftLivingEntity;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Map;

/**
 * ThreeArrowModule class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class ThreeArrowModule extends AbstractSurvivalModule
{
    /**
     * Constructor
     *
     * @param plugin Parent plugin
     * @param api API instance
     * @param moduleConfiguration Module configuration
     */
    public ThreeArrowModule(SurvivalPlugin plugin, SurvivalAPI api, Map<String, Object> moduleConfiguration)
    {
        super(plugin, api, moduleConfiguration);
    }

    /**
     * Launch 2 more arrows when one is launched
     *
     * @param event Event
     */
    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event)
    {
        if (event.getEntity().getType() != EntityType.ARROW || !(event.getEntity().getShooter() instanceof Player) || event.getEntity().hasMetadata("TAM"))
            return;

        for(int i = 0; i < 2; i++)
            Bukkit.getScheduler().runTaskLater(this.plugin, () ->
            {
                EntityTippedArrow entityTippedArrow = new EntityTippedArrow(((CraftWorld)event.getEntity().getWorld()).getHandle(), ((CraftLivingEntity)event.getEntity().getShooter()).getHandle());
                entityTippedArrow.a(((CraftLivingEntity)event.getEntity().getShooter()).getHandle(), ((CraftLivingEntity)event.getEntity().getShooter()).getHandle().pitch, ((CraftLivingEntity)event.getEntity().getShooter()).getHandle().yaw, 0.0F, 3.0F, 1.0F);
                entityTippedArrow.getBukkitEntity().setMetadata("TAM", new FixedMetadataValue(this.plugin, true));
                entityTippedArrow.getBukkitEntity().setVelocity(event.getEntity().getVelocity());
                ((CraftWorld)event.getEntity().getWorld()).getHandle().addEntity(entityTippedArrow);
            }, 5L * (i + 1));
    }
}
