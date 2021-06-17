package com.github.matthewperiut.quarry.utility;
import static com.github.matthewperiut.quarry.Quarry.LANDMARK_BLOCK;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LandmarkDetection {
    public int getCount(BlockPos pos, World world) {
        int limit = 128;
        boolean[] functional = {false, false, false};
        for (int i = -limit / 2; i < limit / 2; i++) {
            if (i == 0)
                continue;

            BlockPos xCheck = new BlockPos(pos.getX() + i, pos.getY(), pos.getZ());
            BlockPos yCheck = new BlockPos(pos.getX(), pos.getY() + i, pos.getZ());
            BlockPos zCheck = new BlockPos(pos.getX(), pos.getY(), pos.getZ() + i);
            if (world.getBlockState(xCheck).getBlock() == LANDMARK_BLOCK)
                functional[0] = true;
            if (world.getBlockState(yCheck).getBlock() == LANDMARK_BLOCK)
                functional[1] = true;
            if (world.getBlockState(zCheck).getBlock() == LANDMARK_BLOCK)
                functional[2] = true;
        }

        int linkStatus = 1; // Start at one bc we include ourselves
        for (int j = 0; j < 3; j++) {
            if (functional[j]) {
                linkStatus++;
            }
        }
        return linkStatus;
    }
}
