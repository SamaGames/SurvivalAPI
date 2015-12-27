package net.samagames.survivalapi.nms.stack;

import net.minecraft.server.v1_8_R3.*;

public class CustomSoup extends ItemSoup
{
    public CustomSoup()
    {
        super(6);
        this.c(64);
    }

    @Override
    public ItemStack b(ItemStack var1, World var2, EntityHuman var3)
    {
        if(!var3.abilities.canInstantlyBuild)
            --var1.count;

        return var1;
    }
}
