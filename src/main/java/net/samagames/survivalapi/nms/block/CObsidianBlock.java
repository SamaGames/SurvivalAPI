package net.samagames.survivalapi.nms.block;

import net.minecraft.server.v1_8_R3.BlockObsidian;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.World;

/**
 * Created by Silva on 07/12/2015.
 */
public class CObsidianBlock extends BlockObsidian {

    public CObsidianBlock()
    {
        super();

        c(1.5F);
        b(10.0F);
    }

    public float g(World world, BlockPosition blockposition) {
        return 1.5F;
    }
}
