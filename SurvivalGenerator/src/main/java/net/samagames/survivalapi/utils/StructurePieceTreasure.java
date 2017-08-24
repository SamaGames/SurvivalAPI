package net.samagames.survivalapi.utils;

import net.minecraft.server.v1_8_R3.*;

import java.util.List;
import java.util.Random;

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
public class StructurePieceTreasure extends WeightedRandom.WeightedRandomChoice
{
    private ItemStack b;
    private int c;
    private int d;

    public StructurePieceTreasure(Item paramItem, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
        super(paramInt4);
        this.b = new ItemStack(paramItem, 1, paramInt1);
        this.c = paramInt2;
        this.d = paramInt3;
    }

    public static void a(Random paramRandom, List<StructurePieceTreasure> paramList, TileEntityDispenser paramTileEntityDispenser, int paramInt)
    {
        for (int i = 0; i < paramInt; i++)
        {
            StructurePieceTreasure localStructurePieceTreasure = (StructurePieceTreasure)WeightedRandom.a(paramRandom, paramList);
            int j = localStructurePieceTreasure.c + paramRandom.nextInt(localStructurePieceTreasure.d - localStructurePieceTreasure.c + 1);
            if (localStructurePieceTreasure.b.getMaxStackSize() >= j)
            {
                ItemStack localItemStack1 = localStructurePieceTreasure.b.cloneItemStack();
                localItemStack1.count = j;
                paramTileEntityDispenser.setItem(paramRandom.nextInt(paramTileEntityDispenser.getSize()), localItemStack1);
            }
            else
            {
                for (int k = 0; k < j; k++)
                {
                    ItemStack localItemStack2 = localStructurePieceTreasure.b.cloneItemStack();
                    localItemStack2.count = 1;
                    paramTileEntityDispenser.setItem(paramRandom.nextInt(paramTileEntityDispenser.getSize()), localItemStack2);
                }
            }
        }
    }
}
