package net.samagames.survivalapi.utils;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class Meta
{
    public static ItemStack addMeta(ItemStack stack)
    {
        net.minecraft.server.v1_8_R3.ItemStack cloned = CraftItemStack.asNMSCopy(stack.clone());
        NBTTagCompound nbt = cloned.getTag();

        if (nbt == null)
            nbt = new NBTTagCompound();

        nbt.setString("sg-dropped", "meow");
        cloned.setTag(nbt);

        return CraftItemStack.asBukkitCopy(cloned);
    }

    public static boolean hasMeta(ItemStack stack)
    {
        if (stack == null)
            return false;

        net.minecraft.server.v1_8_R3.ItemStack cloned = CraftItemStack.asNMSCopy(stack.clone());
        NBTTagCompound nbt = cloned.getTag();

        return nbt != null && nbt.hasKey("sg-dropped");
    }
}
