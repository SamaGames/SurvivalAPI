package net.samagames.survivalapi.nms.potions;

import net.minecraft.server.v1_8_R3.*;

public class PotionAttackDamageNerf extends MobEffectAttackDamage
{
    public PotionAttackDamageNerf(int i, MinecraftKey minecraftKey, boolean b, int i1)
    {
        super(i, minecraftKey, b, i1);

        this.b(4, 0);
        this.a(GenericAttributes.ATTACK_DAMAGE, "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9", 2.5D, 2);
        this.c("potion.damageBoost");
    }

    @Override
    public double a(int id, AttributeModifier modifier)
    {
        double result = super.a(id, modifier);

        if (this.id == MobEffectList.INCREASE_DAMAGE.id)
            result /= 3;

        return result;
    }
}