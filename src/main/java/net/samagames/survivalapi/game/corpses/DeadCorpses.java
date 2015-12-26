package net.samagames.survivalapi.game.corpses;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.games.themachine.messages.IMessageManager;
import net.samagames.tools.LocationUtils;
import net.samagames.tools.ParticleEffect;
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
import org.bukkit.util.EulerAngle;

import java.util.Random;

public class DeadCorpses
{
    private static final String corpsesPart1 = "execute @e[name=base_%player%] ~ ~-1.2 ~-0.5 summon ArmorStand ~ ~ ~ {CustomName:\"part_1_%player%\",NoBasePlate:1,NoGravity:1,Invulnerable:1,DisabledSlots:2039583,Pose:{Body:[-88f,0f,0f],Head:[-90f,0f,0f],RightArm:[90f,0f,0f],LeftArm:[90f,0f,0f]},Invisible:1,ShowArms:1}";
    private static final String corpsesPart2 = "execute @e[name=base_%player%] ~ ~ ~ summon ArmorStand ~ ~-0.596 ~ {CustomName:\"part_2_%player%\",NoBasePlate:1,NoGravity:1,Invulnerable:1,DisabledSlots:2039583,Pose:{Body:[0f,0f,0f],RightLeg:[-90f,0f,0f],LeftLeg:[-90f,0f,0f]},Invisible:1}";

    private final Player player;
    private final Random random;

    public DeadCorpses(Player player)
    {
        this.player = player;
        this.random = new Random();
    }

    public void spawn(Location location)
    {
        IMessageManager messageManager = SamaGamesAPI.get().getGameManager().getCoherenceMachine().getMessageManager();

        ParticleEffect.SMOKE_LARGE.display(0.5F, 0.5F, 0.5F, 0.025F, 1, location, 120.0D);
        ParticleEffect.SMOKE_LARGE.display(0.5F, 0.5F, 0.5F, 0.025F, 1, location, 120.0D);
        ParticleEffect.SMOKE_LARGE.display(0.5F, 0.5F, 0.5F, 0.025F, 1, location, 120.0D);

        messageManager.writeCustomMessage("Spawning dead corpses...", true);

        ArmorStand armorStand = location.getWorld().spawn(location, ArmorStand.class);
        armorStand.setCustomName("base_" + this.player.getName());
        armorStand.setCustomNameVisible(false);
        armorStand.setGravity(false);
        armorStand.setVisible(false);

        messageManager.writeCustomMessage("Spawned base at " + LocationUtils.loc2str(armorStand.getLocation()), true);

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), corpsesPart1.replaceAll("%player%", this.player.getName()));
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), corpsesPart2.replaceAll("%player%", this.player.getName()));

        armorStand.remove();

        messageManager.writeCustomMessage("Searching for spawned parts...", true);

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
            messageManager.writeCustomMessage("One of two parts not found!", true);
            return;
        }

        SamaGamesAPI.get().getGameManager().getCoherenceMachine().getMessageManager().writeCustomMessage("Two parts found!", true);
        SamaGamesAPI.get().getGameManager().getCoherenceMachine().getMessageManager().writeCustomMessage("Setting inventory", true);

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

        messageManager.writeCustomMessage("Randomizing Euler angles...", true);

        corpsesPart1.setHeadPose(new EulerAngle(corpsesPart1HeadPose.getX(), -50 + this.random.nextInt(100), -25 + this.random.nextInt(50)));
        corpsesPart1.setLeftArmPose(new EulerAngle(corpsesPart1LeftArmPose.getX(), 160.0D - this.random.nextInt(140), corpsesPart1LeftArmPose.getZ()));
        corpsesPart1.setRightArmPose(new EulerAngle(corpsesPart1RightArmPose.getX(), 140.0D + this.random.nextInt(80), -90.0D));
        corpsesPart2.setLeftLegPose(new EulerAngle(corpsesPart2LeftLegPose.getX(), -80.0D + this.random.nextInt(80), corpsesPart2LeftLegPose.getZ()));
        corpsesPart2.setRightLegPose(new EulerAngle(corpsesPart2RightLegPose.getX(), 80.0D - this.random.nextInt(80), corpsesPart2RightLegPose.getZ()));
    }
}
