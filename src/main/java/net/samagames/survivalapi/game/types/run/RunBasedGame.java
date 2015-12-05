package net.samagames.survivalapi.game.types.run;

import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.modules.block.RandomChestModule;
import net.samagames.survivalapi.modules.block.RapidOresModule;
import net.samagames.survivalapi.modules.block.TorchThanCoalModule;
import net.samagames.survivalapi.modules.combat.AutomaticTNTModule;
import net.samagames.survivalapi.modules.combat.DropMyEffectsModule;
import net.samagames.survivalapi.modules.craft.*;
import net.samagames.survivalapi.modules.gameplay.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public interface RunBasedGame
{
    void teleportDeathMatch();

    default void applyModules()
    {
        SurvivalAPI.get().loadModule(DisableLevelTwoPotionModule.class, null);
        SurvivalAPI.get().loadModule(DisableNotchAppleModule.class, null);
        SurvivalAPI.get().loadModule(DisableSpeckedMelonModule.class, null);
        //SurvivalAPI.get().loadModule(StackableItemModule.class, null); //WIP

        SurvivalAPI.get().loadModule(RapidOresModule.class, new RapidOresModule.ConfigurationBuilder().build());
        SurvivalAPI.get().loadModule(RapidToolsModule.class, null);
        SurvivalAPI.get().loadModule(RapidFoodModule.class, null);
        SurvivalAPI.get().loadModule(RapidStackingModule.class, null);
        SurvivalAPI.get().loadModule(RapidUsefullModule.class, null);

        SurvivalAPI.get().loadModule(AutomaticTNTModule.class, null);
        SurvivalAPI.get().loadModule(FastTreeModule.class, null);
        SurvivalAPI.get().loadModule(PersonalBlocksModule.class, null);
        SurvivalAPI.get().loadModule(TorchThanCoalModule.class, new TorchThanCoalModule.ConfigurationBuilder().build());
        SurvivalAPI.get().loadModule(RemoveItemOnUseModule.class, null);


        ConstantPotionModule.ConfigurationBuilder constantPotionConfiguration = new ConstantPotionModule.ConfigurationBuilder();
        constantPotionConfiguration.addPotionEffect(PotionEffectType.SPEED, 0);
        constantPotionConfiguration.addPotionEffect(PotionEffectType.FAST_DIGGING, 0);

        SurvivalAPI.get().loadModule(ConstantPotionModule.class, constantPotionConfiguration.build());

        RandomChestModule.ConfigurationBuilder randomChestConfiguration = new RandomChestModule.ConfigurationBuilder();
        randomChestConfiguration.addItemWithPercentage(new ItemStack(Material.DIAMOND, 2), 40);
        randomChestConfiguration.addItemWithPercentage(new ItemStack(Material.REDSTONE, 4), 60);
        randomChestConfiguration.addItemWithPercentage(new ItemStack(Material.NETHER_STALK, 3), 55);
        randomChestConfiguration.addItemWithPercentage(new ItemStack(Material.GOLD_INGOT, 2), 50);

        SurvivalAPI.get().loadModule(RandomChestModule.class, randomChestConfiguration.build());

        DropMyEffectsModule.ConfigurationBuilder dropMyEffectsConfiguration = new DropMyEffectsModule.ConfigurationBuilder();
        dropMyEffectsConfiguration.blacklistPotionEffect(PotionEffectType.SPEED);
        dropMyEffectsConfiguration.blacklistPotionEffect(PotionEffectType.FAST_DIGGING);

        SurvivalAPI.get().loadModule(DropMyEffectsModule.class, dropMyEffectsConfiguration.build());
    }

    default void removeEffects(Player player)
    {
        player.removePotionEffect(PotionEffectType.SPEED);
        player.removePotionEffect(PotionEffectType.FAST_DIGGING);
    }
}
