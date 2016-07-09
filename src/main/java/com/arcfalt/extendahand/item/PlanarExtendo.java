package com.arcfalt.extendahand.item;

import com.arcfalt.extendahand.config.Config;
import com.arcfalt.extendahand.utils.ItemUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
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
		if(Config.boxDurability > 0)
		{
			setMaxDamage(Config.boxDurability);
		}
		GameRegistry.registerItem(this);
	}

	@Override
	public int getMaxBlocks()
	{
		return Config.boxMaxBlocks;
	}

	@Override
	public double getMaxDistance()
	{
		return Config.boxMaxDistance;
	}

	@Override
	public boolean getHasCooldown()
	{
		return Config.boxCooldown > 0;
	}

	@Override
	public int getCooldown()
	{
		return Config.boxCooldown;
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

		ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
		NBTTagCompound tags = ItemUtils.getOrCreateTagCompound(stack);
		String posTag0 = LOC + 0;
		String posTag1 = LOC + 1;
		if(!tags.hasKey(posTag0) || !tags.hasKey(posTag1)) return positions;

		BlockPos locA = BlockPos.fromLong(tags.getLong(posTag0));
		BlockPos locB = BlockPos.fromLong(tags.getLong(posTag1));
		Vec3i minLoc = new Vec3i(Math.min(locA.getX(), locB.getX()), Math.min(locA.getY(), locB.getY()), Math.min(locA.getZ(), locB.getZ()));
		Vec3i maxLoc = new Vec3i(Math.max(locA.getX(), locB.getX()), Math.max(locA.getY(), locB.getY()), Math.max(locA.getZ(), locB.getZ()));

		int maxAxis = Config.boxMaxAxis;
		if(maxLoc.getX() - minLoc.getX() > maxAxis || maxLoc.getY() - minLoc.getY() > maxAxis || maxLoc.getZ() - minLoc.getZ() > maxAxis)
		{
			return positions;
		}

		int amount = 0;
		int maxBlocks = getMaxBlocks();

		for(int x = minLoc.getX(); x <= maxLoc.getX(); x++)
		{
			for(int y = minLoc.getY(); y <= maxLoc.getY(); y++)
			{
				for(int z = minLoc.getZ(); z <= maxLoc.getZ(); z++)
				{
					if(x == minLoc.getX() || x == maxLoc.getX() || y == minLoc.getY() || y == maxLoc.getY() || z == minLoc.getZ() || z == maxLoc.getZ())
					{
						BlockPos acting = new BlockPos(x, y, z);
						IBlockState blockState = world.getBlockState(acting);
						Block block = blockState.getBlock();
						if(block.getMaterial(blockState) == Material.AIR)
						{
							amount += 1;
							if(amount > maxBlocks && trimAmount)
							{
								if(stack.isItemStackDamageable() && stack.getItemDamage() >= stack.getMaxDamage()) return positions;
								sendMessage("Maximum limit of " + maxBlocks + " blocks created!", player);
								return positions;
							}
							positions.add(acting);
						}
					}
				}
			}
		}
		return positions;
	}
}
