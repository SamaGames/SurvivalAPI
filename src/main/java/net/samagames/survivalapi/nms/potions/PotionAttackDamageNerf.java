package net.samagames.survivalapi.nms.potions;

import net.minecraft.server.v1_8_R3.*;

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