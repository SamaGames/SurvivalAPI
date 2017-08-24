package net.samagames.survivalapi.utils;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

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
