package net.samagames.survivalapi.nms.stack;

import net.minecraft.server.v1_8_R3.*;

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
        if(!var3.abilities.canInstantlyBuild)
            --var1.count;

        return var1;
    }
}
