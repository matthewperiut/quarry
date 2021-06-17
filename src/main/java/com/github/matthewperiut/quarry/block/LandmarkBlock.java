package com.github.matthewperiut.quarry.block;

import com.github.matthewperiut.quarry.Quarry;
import net.minecraft.block.BlockState;
import net.minecraft.block.TorchBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class LandmarkBlock extends TorchBlock {
    public LandmarkBlock(Settings settings) {
        super(settings, null);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random)
    {
        //remove particle effects
    }

    //@Override
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
    {
        // Limit 63 blocks out to search for another
        int limit = 128;
        if(!world.isClient)
            for(int i = -limit/2; i < limit/2; i++)
            {
                if(i == 0)
                    continue;

                BlockPos xCheck = new BlockPos(pos.getX()+i, pos.getY(), pos.getZ());
                BlockPos yCheck = new BlockPos(pos.getX(), pos.getY()+i, pos.getZ());
                BlockPos zCheck = new BlockPos(pos.getX(), pos.getY(), pos.getZ()+i);
                if(world.getBlockState(xCheck) == Quarry.LANDMARK_BLOCK.getDefaultState())
                {
                    System.out.println(xCheck);
                }
                if(world.getBlockState(yCheck) == Quarry.LANDMARK_BLOCK.getDefaultState())
                {
                    System.out.println(yCheck);
                }
                if(world.getBlockState(zCheck) == Quarry.LANDMARK_BLOCK.getDefaultState())
                {
                    System.out.println(zCheck);
                }
            }
        return ActionResult.SUCCESS;
        //return super.onUse(state, world, pos, player, hand, hit);
    }


}
