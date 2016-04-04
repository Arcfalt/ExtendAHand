package com.arcfalt.extendahand.item;

import com.arcfalt.extendahand.config.Config;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;
import java.util.Set;

public class HandyExtendo extends BaseExtendo
{
	public HandyExtendo()
	{
		setRegistryName("handyextendo");
		setUnlocalizedName("handyextendo");
		setMaxStackSize(1);
		if(Config.baseDurability > 0)
		{
			setMaxDamage(Config.baseDurability);
		}
		GameRegistry.registerItem(this);
	}

	@Override
	public int getMaxBlocks()
	{
		return 1;
	}

	@Override
	public double getMaxDistance()
	{
		return Config.baseMaxDistance;
	}

	@Override
	public boolean getHasCooldown()
	{
		return Config.baseCooldown > 0;
	}

	@Override
	public int getCooldown()
	{
		return Config.baseCooldown;
	}

	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

	@Override
	public Set<BlockPos> actingBlocks(BlockPos blockPos, EnumFacing sideHit, World world, EntityPlayer player, boolean trimAmount)
	{
		Set<BlockPos> positions = new HashSet<BlockPos>();
		BlockPos offsetPos = blockPos.offset(sideHit);

		IBlockState blockState = world.getBlockState(offsetPos);
		Block block = blockState.getBlock();

		if(block.getMaterial(blockState) == Material.air) positions.add(offsetPos);
		return positions;
	}
}