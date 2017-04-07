package net.samagames.survivalapi.nms.potions;

import net.minecraft.server.v1_8_R3.*;

/**
 * PotionAttackDamageNerf class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class PotionAttackDamageNerf extends MobEffectAttackDamage
{
    /**
     * Constructor
     */
    public PotionAttackDamageNerf(int i, MinecraftKey minecraftKey, boolean b, int i1)
    {
        super(i, minecraftKey, b, i1);
    }

    /**
     * Fired when someone fight a player with a strength effect
     *
     * @param id Potion ID
     * @param modifier Modifier
     *
     * @return The damages to add
     */
    @Override
    public double a(int id, AttributeModifier modifier)
    {
        double result = super.a(id, modifier);

        if (this.id == MobEffectList.INCREASE_DAMAGE.id)
            result /= 3;

        return result;
    }
}