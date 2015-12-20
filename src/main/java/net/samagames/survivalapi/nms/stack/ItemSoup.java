package net.samagames.survivalapi.nms.stack;

import net.minecraft.server.v1_8_R3.*;

public class ItemSoup extends ItemFood
{
    public ItemSoup(int var1)
    {
        super(var1, true);
        this.c(64);
    }

    @Override
    public ItemStack b(ItemStack var1, World var2, EntityHuman var3)
    {
        return super.b(var1, var2, var3);
    }
}
