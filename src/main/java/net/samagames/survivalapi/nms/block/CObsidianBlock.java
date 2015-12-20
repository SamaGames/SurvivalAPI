package net.samagames.survivalapi.nms.block;

import net.minecraft.server.v1_8_R3.BlockObsidian;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.World;

public class CObsidianBlock extends BlockObsidian
{
    public CObsidianBlock()
    {
        super();

        this.c(1.5F);
        this.b(10.0F);
    }

    @Override
    public float g(World world, BlockPosition blockposition)
    {
        return 1.5F;
    }
}
