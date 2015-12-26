package net.samagames.survivalapi.game.corpses;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.tools.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import java.util.Random;

public class DeadCorpses
{
    private static final String corpsesPart1 = "execute @e[name=base_%player%,r=15] ~ ~-1.2 ~-0.5 summon ArmorStand ~ ~ ~ {CustomName:\"part_1_%player%\",NoGravity:1,Invulnerable:1,DisabledSlots:2039583,Pose:{Body:[-88f,0f,0f],Head:[-90f,0f,0f],RightArm:[90f,0f,0f],LeftArm:[90f,0f,0f]},ShowArms:1}";
    private static final String corpsesPart2 = "execute @e[name=base_%player%,r=15] ~ ~ ~ summon ArmorStand ~ ~-0.596 ~ {CustomName:\"part_2_%player%\",NoGravity:1,Invulnerable:1,DisabledSlots:2039583,Pose:{Body:[0f,0f,0f],RightLeg:[-90f,0f,0f],LeftLeg:[-90f,0f,0f]},Invisible:1}";

    private final Player player;
    private final Random random;

    public DeadCorpses(Player player)
    {
        this.player = player;
        this.random = new Random();
    }

    public void spawn(Location location)
    {
        Bukkit.broadcastMessage("Spawning dead corpses...");

        ArmorStand armorStand = location.getWorld().spawn(location, ArmorStand.class);
        armorStand.setCustomName("corpses_base");
        armorStand.setCustomNameVisible(false);

        Bukkit.broadcastMessage("Spawned base at " + LocationUtils.loc2str(armorStand.getLocation()));

        Bukkit.dispatchCommand(armorStand, corpsesPart1.replaceAll("%player%", this.player.getName()));
        Bukkit.dispatchCommand(armorStand, corpsesPart2.replaceAll("%player%", this.player.getName()));

        armorStand.remove();

        Bukkit.broadcastMessage("Searching for spawned parts...");

        ArmorStand corpsesPart1 = null;
        ArmorStand corpsesPart2 = null;

        for (Entity entity : location.getWorld().getNearbyEntities(location, 15.0D, 15.0D, 15.0D))
        {
            if (entity.getType() == EntityType.ARMOR_STAND && entity.getCustomName() != null)
            {
                if (entity.getCustomName().equals("part_1_" + this.player.getName()))
                    corpsesPart1 = (ArmorStand) entity;
                else if (entity.getCustomName().equals("part_2_" + this.player.getName()))
                    corpsesPart2 = (ArmorStand) entity;
            }
        }

        if (corpsesPart1 == null || corpsesPart2 == null)
        {
            Bukkit.broadcastMessage("One of two parts not found!");

            SurvivalAPI.get().getPlugin().getLogger().severe("Can't spawn dead corpses of " + this.player.getName() + " because one of two armor stand isn't exist!");
            return;
        }

        Bukkit.broadcastMessage("Two parts found!");
        Bukkit.broadcastMessage("Setting inventory...");

        ItemStack playerHead = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta playerHeadMeta = (SkullMeta) playerHead.getItemMeta();
        playerHeadMeta.setOwner(this.player.getName());
        playerHead.setItemMeta(playerHeadMeta);

        corpsesPart1.setHelmet(playerHead);
        corpsesPart1.setChestplate(this.player.getInventory().getChestplate());
        corpsesPart2.setLeggings(this.player.getInventory().getLeggings());
        corpsesPart2.setBoots(this.player.getInventory().getBoots());

        corpsesPart1.setItemInHand(this.player.getItemInHand());

        EulerAngle corpsesPart1HeadPose = corpsesPart1.getHeadPose();
        EulerAngle corpsesPart1LeftArmPose = corpsesPart1.getLeftArmPose();
        EulerAngle corpsesPart1RightArmPose = corpsesPart1.getRightArmPose();
        EulerAngle corpsesPart2LeftLegPose = corpsesPart2.getLeftLegPose();
        EulerAngle corpsesPart2RightLegPose = corpsesPart2.getRightLegPose();

        Bukkit.broadcastMessage("Randomizing Euler angles...");

        corpsesPart1.setHeadPose(new EulerAngle(corpsesPart1HeadPose.getX(), -50 + this.random.nextInt(100), -25 + this.random.nextInt(50)));
        corpsesPart1.setLeftArmPose(new EulerAngle(corpsesPart1LeftArmPose.getX(), 160.0D - this.random.nextInt(140), corpsesPart1LeftArmPose.getZ()));
        corpsesPart2.setLeftLegPose(new EulerAngle(corpsesPart2LeftLegPose.getX(), -85.0D + this.random.nextInt(85), corpsesPart2LeftLegPose.getZ()));
        corpsesPart2.setRightLegPose(new EulerAngle(corpsesPart2RightLegPose.getX(), 85.0D - this.random.nextInt(85), corpsesPart2RightLegPose.getZ()));

        EulerAngle temporaryArmPose = new EulerAngle(corpsesPart1RightArmPose.getX(), 140.0D + this.random.nextInt(80), -30.0D);

        final ArmorStand finalCorpsesPart = corpsesPart1;

        new BukkitRunnable()
        {
            private double temporaryArmPoseZ = -30.0D;

            @Override
            public void run()
            {
                if (this.temporaryArmPoseZ <= -90.0D)
                    this.cancel();

                finalCorpsesPart.setRightArmPose(temporaryArmPose.setZ(this.temporaryArmPoseZ));

                this.temporaryArmPoseZ -= 3.0D;
            }
        }.runTaskTimer(SurvivalAPI.get().getPlugin(), 1L, 1L);

        Bukkit.broadcastMessage("Done.");
    }
}
