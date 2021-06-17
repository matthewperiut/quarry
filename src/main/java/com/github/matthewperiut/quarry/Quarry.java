package com.github.matthewperiut.quarry;

import com.github.matthewperiut.quarry.block.LandmarkBlock;
import com.github.matthewperiut.quarry.block.QuarryBlock;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.PickaxeItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Quarry implements ModInitializer {
	public static final Block LANDMARK_BLOCK = new LandmarkBlock(FabricBlockSettings.of(Material.WOOD).nonOpaque().noCollision().luminance(13));
	public static final Block MINING_WELL_BLOCK = new QuarryBlock(FabricBlockSettings.of(Material.METAL).strength(0.9f, 0.9f).breakByTool(FabricToolTags.PICKAXES).requiresTool());
	@Override
	public void onInitialize()
	{
		BlockRenderLayerMap.INSTANCE.putBlock(LANDMARK_BLOCK, RenderLayer.getCutout());
		Registry.register(Registry.BLOCK, new Identifier("quarry", "landmark"), LANDMARK_BLOCK);
		Registry.register(Registry.BLOCK, new Identifier("quarry", "mining_well"), MINING_WELL_BLOCK);
		Registry.register(Registry.ITEM, new Identifier("quarry", "landmark"), new BlockItem(LANDMARK_BLOCK, new FabricItemSettings().group(ItemGroup.MISC)));
		Registry.register(Registry.ITEM, new Identifier("quarry", "mining_well"), new BlockItem(MINING_WELL_BLOCK, new FabricItemSettings().group(ItemGroup.MISC)));
	}
}
