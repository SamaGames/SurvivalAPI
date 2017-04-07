package net.samagames.survivalapi.nms.stack;

import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.ItemPotion;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.MobEffect;
import net.minecraft.server.v1_8_R3.World;

import java.util.List;

/**
 * CustomPotion class
 *
 * Copyright (c) for SamaGames
 * All right reserved
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
