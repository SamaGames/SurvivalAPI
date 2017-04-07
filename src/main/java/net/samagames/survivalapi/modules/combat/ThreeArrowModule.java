package net.samagames.survivalapi.modules.combat;

import net.minecraft.server.v1_8_R3.EntityArrow;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.SurvivalPlugin;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

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

        final Vector velocity = event.getEntity().getVelocity();

        for(int i = 0; i < 2; i++)
        {
            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () ->
            {
                EntityArrow entityArrow = new EntityArrow(((CraftWorld)event.getEntity().getWorld()).getHandle(), ((CraftLivingEntity)event.getEntity().getShooter()).getHandle(), 1F);
                entityArrow.shoot(((CraftLivingEntity)event.getEntity().getShooter()).getHandle().pitch, ((CraftLivingEntity)event.getEntity().getShooter()).getHandle().yaw, 0.0F, 3.0F, 1.0F);
                entityArrow.getBukkitEntity().setMetadata("TAM", new FixedMetadataValue(this.plugin, true));
                entityArrow.getBukkitEntity().setVelocity(velocity);
                ((CraftWorld)event.getEntity().getWorld()).getHandle().addEntity(entityArrow);
            }, 5L * (i + 1));
        }
    }
}
