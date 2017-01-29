package net.samagames.survivalapi.nms.stack;

import com.google.common.collect.Multimap;
import net.minecraft.server.v1_10_R1.AttributeModifier;
import net.minecraft.server.v1_10_R1.EnumItemSlot;
import net.minecraft.server.v1_10_R1.GenericAttributes;
import net.minecraft.server.v1_10_R1.ItemAxe;

public class CustomAxe extends ItemAxe
{
    public CustomAxe(EnumToolMaterial enumToolMaterial)
    {
        super(enumToolMaterial);
    }

    @Override
    public Multimap<String, AttributeModifier> a(EnumItemSlot var1)
    {
        Multimap var2 = super.a(var1);

        if(var1 == EnumItemSlot.MAINHAND)
        {
            if (var2.containsKey(GenericAttributes.ATTACK_DAMAGE.getName()))
                var2.removeAll(GenericAttributes.ATTACK_DAMAGE.getName());

            var2.put(GenericAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(h, "Tool modifier", (double) this.b - (this.g().c() / 2), 0));
            var2.put(GenericAttributes.f.getName(), new AttributeModifier(i, "Tool modifier", (double)this.c, 0));
        }

        return var2;
    }
}
