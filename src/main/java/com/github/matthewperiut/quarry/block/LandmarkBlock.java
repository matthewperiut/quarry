package com.github.matthewperiut.quarry.block;

import com.github.matthewperiut.quarry.utility.LandmarkDetection;
import net.minecraft.block.BlockState;
import net.minecraft.block.TorchBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class LandmarkBlock extends TorchBlock {
    public LandmarkBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random)
    {
        //remove particle effects
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
    {
        if(world.isClient)
        {
            LandmarkDetection landmarkDetection = new LandmarkDetection();
            int linkStatus = landmarkDetection.getCount(pos, world);

            String defaultText = " (" + linkStatus + "/3)";
            String message = switch (linkStatus) {
                case 1 -> "Place 3 Landmarks in straight lines, check with corner";
                case 2 -> "Make sure you're checking the corner, not edges";
                case 3 -> "Place a Quarry next to the corner Landmark";
                default -> "";
            };

            player.sendMessage(new LiteralText(message+defaultText));
        }

        return ActionResult.SUCCESS;
    }
}
