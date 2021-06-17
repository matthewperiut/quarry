package com.github.matthewperiut.quarry.functionality;

import com.github.matthewperiut.quarry.Quarry;

import com.github.matthewperiut.quarry.utility.LandmarkDetection;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.text.LiteralText;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

import team.reborn.energy.EnergySide;
import team.reborn.energy.EnergyStorage;
import team.reborn.energy.EnergyTier;

import java.util.List;

import static net.minecraft.block.Blocks.*;

public class QuarryEntity extends BlockEntity implements EnergyStorage, QuarryInventory// implements EnergyIo
{
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private final QuarryValidity quarryValidity = new QuarryValidity();
    private double energy = 0.f;
    private int xProgress = 0;
    private int yProgress = 0;
    private int zProgress = 0;

    BlockPos startingPoint = null;
    int xSize = 0;
    int zSize = 0;

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
        else
        {
            startingPoint = quarryValidity.landmarkDetection.getStart();
            xSize = quarryValidity.landmarkDetection.getXSize();
            zSize = quarryValidity.landmarkDetection.getZSize();
            yProgress = startingPoint.getY();
            xProgress = 0;
            zProgress = 0;
        }
    }

    private boolean unsafeBlock(World world, Block block)
    {
        return world.getBlockState(startingPoint.add(new Vec3i(xProgress-1,-(startingPoint.getY()-yProgress),zProgress))).getBlock() == block;
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

        //tif(me.progress)
        if(me.yProgress < 3)
            return;

        if(!me.getStack(0).isEmpty())
            return;

        if (me.energy == 1000)
        {
            if (me.getStack(0).isEmpty())
            {
                me.mine(world);
            }
        }
    }

    private void mine(World world)
    {
        do {
            if(xProgress == xSize+1)
            {
                xProgress = 0;
                zProgress++;
            }
            if(zProgress == zSize+1)
            {
                zProgress = 0;
                yProgress--;
            }

            xProgress++;
        } while(unsafeBlock(world, AIR) || unsafeBlock(world, BEDROCK) || unsafeBlock(world, END_PORTAL_FRAME) || unsafeBlock(world,END_PORTAL) || unsafeBlock(world,END_GATEWAY));

        BlockPos collect = startingPoint.add(new Vec3i(xProgress-1,-(startingPoint.getY()-yProgress),zProgress));

        world.getBlockState(collect).getBlock();
        List<ItemStack> resources = Block.getDroppedStacks(world.getBlockState(collect),(ServerWorld)world,collect,world.getBlockEntity(pos));
        if(!resources.isEmpty())
            setStack(0, resources.get(0));
        world.setBlockState(collect,AIR.getDefaultState());

        energy = 0;
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
        return EnergyTier.INFINITE;
    }

    // Serialize the BlockEntity
    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        tag.putInt("xProgress", xProgress);
        tag.putInt("yProgress", yProgress);
        tag.putInt("zProgress", zProgress);
        tag.putDouble("energy", energy);
        tag.putBoolean("init", init);
        tag.putInt("xSize", xSize);
        tag.putInt("zSize", zSize);
        tag.putInt("x", startingPoint.getX());
        tag.putInt("y", startingPoint.getY());
        tag.putInt("z", startingPoint.getZ());
        Inventories.writeNbt(tag,items);
        return tag;
    }
    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        xProgress = tag.getInt("xProgress");
        yProgress = tag.getInt("yProgress");
        zProgress = tag.getInt("zProgress");
        energy = tag.getDouble("energy");
        init = tag.getBoolean("init");
        xSize = tag.getInt("xSize");
        zSize = tag.getInt("zSize");
        int x = tag.getInt("x");
        int y = tag.getInt("y");
        int z = tag.getInt("z");
        startingPoint = new BlockPos(x,y,z);
        Inventories.readNbt(tag,items);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }
}
