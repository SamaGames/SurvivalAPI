package net.samagames.survivalapi.nms.potions;

import net.minecraft.server.v1_9_R2.*;

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
    public PotionAttackDamageNerf()
    {
        super(false, 9643043, 3.0D);

        this.b(4, 0);
        this.c("potion.damageBoost");
        this.a(GenericAttributes.ATTACK_DAMAGE, "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9", 0.0D, 0);
        this.j();
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

        if (this == MobEffects.INCREASE_DAMAGE)
            result /= 3;

        return result;
    }
}