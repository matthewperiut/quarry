package com.github.matthewperiut.quarry.block.landmark_functionality;
import static com.github.matthewperiut.quarry.Quarry.LANDMARK_BLOCK;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LandmarkDetection {
    BlockPos[] positions = new BlockPos[3];

    public int getCount(BlockPos pos, World world) {
        int limit = 128;
        boolean[] functional = {false, false, false};
        for (int i = -limit / 2; i < limit / 2; i++) {
            // Making sure a terrible arrangement isn't made
            if (i > -2 && i < 2)
                continue;

            BlockPos xCheck = new BlockPos(pos.getX() + i, pos.getY(), pos.getZ());
            BlockPos zCheck = new BlockPos(pos.getX(), pos.getY(), pos.getZ() + i);
            if (world.getBlockState(xCheck).getBlock() == LANDMARK_BLOCK)
            {
                functional[0] = true;
                positions[0] = xCheck;
            }
            if (world.getBlockState(zCheck).getBlock() == LANDMARK_BLOCK)
            {
                functional[1] = true;
                positions[1] = zCheck;
            }
        }

        int linkStatus = 1; // Start at one bc we include ourselves
        for (int j = 0; j < 3; j++) {
            if (functional[j]) {
                linkStatus++;
            }
        }
        return linkStatus;
    }

    public BlockPos getStart()
    {
        int x = Math.min(positions[0].getX(), positions[1].getX()) + 1;
        int z = Math.min(positions[0].getZ(), positions[1].getZ()) + 1;
        int y = positions[0].getY()-1;

        return new BlockPos(x,y,z);
    }
    public int getXSize()
    {
        return Math.abs(positions[0].getX() - positions[1].getX())-2;
    }
    public int getZSize()
    {
        return Math.abs(positions[0].getZ() - positions[1].getZ())-2;
    }
}
