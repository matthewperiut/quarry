package com.github.matthewperiut.quarry.functionality;

import com.github.matthewperiut.quarry.Quarry;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.text.LiteralText;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

import team.reborn.energy.EnergySide;
import team.reborn.energy.EnergyStorage;
import team.reborn.energy.EnergyTier;

public class QuarryEntity extends BlockEntity implements EnergyStorage// implements EnergyIo
{
    private final QuarryValidity quarryValidity = new QuarryValidity();
    private double energy = 0.f;
    private int progress = 0;
    boolean init = false;

    public QuarryEntity(BlockPos pos, BlockState state) {
        super(Quarry.MINING_WELL_BLOCK_ENTITY, pos, state);
    }

    public void validityNotification(World world, BlockPos blockPos)
    {
        quarryValidity.nearValidLandmark(world, blockPos);
        if (!quarryValidity.valid)
        {
            PlayerEntity player = world.getClosestPlayer(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 10, false);
            if (player != null)
                player.sendMessage(new LiteralText("Invalid placement: use landmark"), true);

        }
    }

    public static <T extends BlockEntity> void tick(World world, BlockPos blockPos, BlockState blockState, T t) {
        // Checks of validity
        if (world.isClient())
            return;
        QuarryEntity me = (QuarryEntity) world.getBlockEntity(blockPos);
        if (me == null)
            return;
        if (!me.init)
        {
            me.init = true;
            me.validityNotification(world,blockPos);
        }
        if (!me.quarryValidity.valid && me.init)
            return;

        // Implement quarry actions

        // Debugging energy
        // System.out.println(me.energy);

        // Next thing is an algorithm to deduct 1000 energy whenever possible and remove the proper blocks within the area.
        // I'll make it look prettier over time.
        // I need this bad boy -> world.getBlockState(blockPos).getBlock().asItem();
        // and this one =-= https://fabricmc.net/wiki/tutorial:inventory
    }

    // Energize the BlockEntity
    @Override
    public double getStored(EnergySide face) {
        return energy;
    }

    @Override
    public void setStored(double amount) {
        energy = amount;
    }

    @Override
    public double getMaxStoredPower() {
        return 1000;
    }

    @Override
    public EnergyTier getTier() {
        return EnergyTier.MEDIUM;
    }

    // Serialize the BlockEntity
    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        tag.putInt("progress", progress);
        tag.putDouble("energy", energy);
        tag.putBoolean("init", init);
        return tag;
    }
    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        progress = tag.getInt("progress");
        energy = tag.getDouble("energy");
        init = tag.getBoolean("init");
    }
}
