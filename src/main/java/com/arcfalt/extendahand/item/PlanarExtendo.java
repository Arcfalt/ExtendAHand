package com.arcfalt.extendahand.item;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;
import java.util.Set;

public class PlanarExtendo extends BasePointExtendo
{
	public PlanarExtendo()
	{
		setRegistryName("planarextendo");
		setUnlocalizedName("planarextendo");
		setMaxStackSize(1);
		GameRegistry.registerItem(this);
	}

	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		//ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

	/*
	@Override
	protected Set<BlockPos> actingBlocks(BlockPos blockPos, EnumFacing sideHit, World world, EntityPlayer player)
	{
		Set<BlockPos> positions = new HashSet<BlockPos>();
		BlockPos offsetPos = blockPos.offset(sideHit);

		IBlockState blockState = world.getBlockState(offsetPos);
		Block block = blockState.getBlock();

		if(block.getMaterial() == Material.air) positions.add(offsetPos);
		return positions;
	}
	*/
}
