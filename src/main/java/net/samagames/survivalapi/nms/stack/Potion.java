package net.samagames.survivalapi.nms.stack;

import net.minecraft.server.v1_8_R3.*;

public class Potion extends ItemPotion
{
    public Potion()
    {
        this.c(64);
        this.setMaxDurability(0);
        this.a(CreativeModeTab.k);
    }

    @Override
    public ItemStack b(ItemStack var1, World var2, EntityHuman var3)
    {
        if(!var3.abilities.canInstantlyBuild)
            --var1.count;

        var3.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);

        if(!var3.abilities.canInstantlyBuild)
        {
            if(var1.count <= 0)
                return new ItemStack(Items.GLASS_BOTTLE);

            var3.inventory.pickup(new ItemStack(Items.GLASS_BOTTLE));
        }

        return var1;
    }

    @Override
    public int d(ItemStack var1)
    {
        return 32;
    }

    @Override
    public EnumAnimation e(ItemStack var1)
    {
        return EnumAnimation.DRINK;
    }

    @Override
    public ItemStack a(ItemStack var1, World var2, EntityHuman var3)
    {
        if(f(var1.getData()))
        {
            if(!var3.abilities.canInstantlyBuild)
                --var1.count;

            var2.makeSound(var3, "random.bow", 0.5F, 0.4F / (g.nextFloat() * 0.4F + 0.8F));

            if(!var2.isClientSide)
                var2.addEntity(new EntityPotion(var2, var3, var1));

            var3.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);

            return var1;
        }
        else
        {
            var3.a(var1, this.d(var1));

            return var1;
        }
    }
}
