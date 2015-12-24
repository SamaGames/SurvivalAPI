package net.samagames.survivalapi.nms.stack;

import net.minecraft.server.v1_8_R3.*;

import java.util.Iterator;
import java.util.List;

public class CustomPotion extends ItemPotion
{
    public CustomPotion()
    {
        this.c(64);
    }

    @Override
    public ItemStack b(ItemStack var1, World var2, EntityHuman var3)
    {
        if(!var3.abilities.canInstantlyBuild)
            --var1.count;

        if(!var2.isClientSide)
        {
            List var4 = this.h(var1);

            if(var4 != null)
            {
                for (Object aVar4 : var4)
                {
                    MobEffect var6 = (MobEffect) aVar4;
                    var3.addEffect(new MobEffect(var6));
                }
            }
        }

        var3.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);

        return var1;
    }
}
