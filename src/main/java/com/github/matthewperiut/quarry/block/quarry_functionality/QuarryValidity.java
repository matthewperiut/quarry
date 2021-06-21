package com.github.matthewperiut.quarry.block.quarry_functionality;

import com.github.matthewperiut.quarry.Quarry;
import com.github.matthewperiut.quarry.block.landmark_functionality.LandmarkDetection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class QuarryValidity
{
    public LandmarkDetection landmarkDetection = new LandmarkDetection();
    public BlockPos landmarkPosition;
    public boolean valid = false;

    public boolean nearValidLandmark(World world, BlockPos pos)
    {
        BlockPos start = pos.subtract(new Vec3i(1,0,1));
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 3; j++)
            {
                BlockPos current = new BlockPos(start.getX()+i,start.getY(),start.getZ()+j);
                if(world.getBlockState(current) == Quarry.LANDMARK_BLOCK.getDefaultState())
                {
                    if(landmarkDetection.getCount(current, world) == 3)
                    {
                        // I still need to check if I'm inside of the area given.
                        landmarkPosition = current;
                        valid = true;
                        break;
                    }

                }
            }
            if(valid)
                break;
        }

        return valid;
    }
}
