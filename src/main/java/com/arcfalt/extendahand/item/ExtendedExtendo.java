package com.arcfalt.extendahand.item;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;
import java.util.Set;

public class ExtendedExtendo extends BaseExtendo
{
	public ExtendedExtendo()
	{
		setRegistryName("extendedextendo");
		setUnlocalizedName("extendedextendo");
		setMaxStackSize(1);
		GameRegistry.registerItem(this);
	}

	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

	@Override
	protected Set<BlockPos> actingBlocks(BlockPos blockPos, EnumFacing sideHit, World world, EntityPlayer player)
	{
		Set<BlockPos> positions = new HashSet<BlockPos>();

		BlockPos offsetPos = blockPos.offset(sideHit);
		BlockPos playerPos = player.getPosition();

		int x = MathHelper.abs_int(playerPos.getX() - offsetPos.getX());
		int y = MathHelper.abs_int(playerPos.getY() - offsetPos.getY());
		int z = MathHelper.abs_int(playerPos.getZ() - offsetPos.getZ());

		if(x > y && x > z) fillPosXLine(positions, playerPos, offsetPos, world);
		else if(y > x && y > z) fillPosYLine(positions, playerPos, offsetPos, world);
		else fillPosZLine(positions, playerPos, offsetPos, world);

		return positions;
	}

	private void checkAddPos(Set<BlockPos> positions, BlockPos blockPos, World world)
	{
		IBlockState blockState = world.getBlockState(blockPos);
		Block block = blockState.getBlock();
		if(block.getMaterial() == Material.air) positions.add(blockPos);
	}

	private BlockPos fillPosXLine(Set<BlockPos> positions, BlockPos playerPos, BlockPos blockPos, World world)
	{
		BlockPos usePos = blockPos;
		if(blockPos.getX() < playerPos.getX())
		{
			for(int i = blockPos.getX(); i < playerPos.getX() - 1; i++)
			{
				checkAddPos(positions, usePos, world);
				usePos = usePos.east();
			}
		}
		else if(playerPos.getX() < blockPos.getX())
		{
			for(int i = blockPos.getX(); i > playerPos.getX(); i--)
			{
				checkAddPos(positions, usePos, world);
				usePos = usePos.west();
			}
		}
		return usePos;
	}

	private BlockPos fillPosZLine(Set<BlockPos> positions, BlockPos playerPos, BlockPos blockPos, World world)
	{
		BlockPos usePos = blockPos;
		if(blockPos.getZ() < playerPos.getZ())
		{
			for(int i = blockPos.getZ(); i < playerPos.getZ() - 1; i++)
			{
				checkAddPos(positions, usePos, world);
				usePos = usePos.south();
			}
		}
		else if(playerPos.getZ() < blockPos.getZ())
		{
			for(int i = blockPos.getZ(); i > playerPos.getZ(); i--)
			{
				checkAddPos(positions, usePos, world);
				usePos = usePos.north();
			}
		}
		return usePos;
	}

	private BlockPos fillPosYLine(Set<BlockPos> positions, BlockPos playerPos, BlockPos blockPos, World world)
	{
		BlockPos usePos = blockPos;
		if(blockPos.getY() < playerPos.getY())
		{
			for(int i = blockPos.getY(); i < playerPos.getY() - 1; i++)
			{
				checkAddPos(positions, usePos, world);
				usePos = usePos.up();
			}
		}
		else if(playerPos.getY() < blockPos.getY())
		{
			for(int i = blockPos.getY(); i > playerPos.getY() + 2; i--)
			{
				checkAddPos(positions, usePos, world);
				usePos = usePos.down();
			}
		}
		return usePos;
	}
}
