package net.samagames.survivalapi.nms.stack;

import net.minecraft.server.v1_10_R1.EntityHuman;
import net.minecraft.server.v1_10_R1.EntityLiving;
import net.minecraft.server.v1_10_R1.ItemSoup;
import net.minecraft.server.v1_10_R1.ItemStack;
import net.minecraft.server.v1_10_R1.StatisticList;
import net.minecraft.server.v1_10_R1.World;

/**
 * CustomSoup class
 *
 * Copyright (c) for SamaGames
 * All right reserved
 */
public class CustomSoup extends ItemSoup
{
    /**
     * Constructor
     */
    public CustomSoup()
    {
        super(6);
        this.d(64);
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
    public ItemStack a(ItemStack var1, World var2, EntityLiving var3)
    {
        EntityHuman var4 = var3 instanceof EntityHuman ? (EntityHuman) var3: null;

        if(var4 == null || !var4.abilities.canInstantlyBuild)
            --var1.count;

        if(var4 != null)
            var4.b(StatisticList.b(this));

        return var1;
    }
}
