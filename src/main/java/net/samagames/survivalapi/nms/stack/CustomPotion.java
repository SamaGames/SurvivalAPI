package net.samagames.survivalapi.nms.stack;

import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.ItemPotion;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.MobEffect;
import net.minecraft.server.v1_8_R3.World;

import java.util.List;

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
public class CustomPotion extends ItemPotion
{
    /**
     * Constructor
     */
    public CustomPotion()
    {
        this.c(64);
    }

    /**
     * Override the consume event to remove the stack if empty
     *
     * @param var1 Stack
     * @param var2 World
     * @param var3 Human
     *
     * @return The new ItemStack
     */
    @Override
    public ItemStack b(ItemStack var1, World var2, EntityHuman var3)
    {
        if(var3 == null || !var3.abilities.canInstantlyBuild)
            --var1.count;

        if(!var2.isClientSide)
        {
            List var5 = this.h(var1);

            if (var5 != null)
            {
                for (Object aVar5 : var5)
                {
                    MobEffect var7 = (MobEffect) aVar5;
                    var3.addEffect(new MobEffect(var7));
                }
            }
        }

        return var1;
    }
}
