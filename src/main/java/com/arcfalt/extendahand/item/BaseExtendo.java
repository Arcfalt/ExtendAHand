package com.arcfalt.extendahand.item;

import com.arcfalt.extendahand.utils.ItemUtils;
import com.arcfalt.extendahand.utils.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;
import java.util.Set;

public class BaseExtendo extends Item
{
	@SideOnly(Side.CLIENT)
	public void drawHighlight(RenderWorldLastEvent event, EntityPlayerSP player, ItemStack stack)
	{
		// Find whatever is under the cursor up to a certain distance away
		Minecraft minecraft = Minecraft.getMinecraft();
		MovingObjectPosition mouseOver = minecraft.getRenderViewEntity().rayTrace(90.0, event.partialTicks);
		if(mouseOver == null) return;

		// Get the block position and make sure it is a block
		World world = player.worldObj;
		BlockPos blockPos = mouseOver.getBlockPos();
		if(blockPos == null) return;

		IBlockState blockState = world.getBlockState(blockPos);
		Block block = blockState.getBlock();
		if(block != null && block.getMaterial() != Material.air)
		{
			Set<BlockPos> positions = actingBlocks(blockPos, mouseOver.sideHit, world, player);
			RenderUtils.renderBlockOverlays(event, player, positions, 0.01f);
		}
	}

	protected Set<BlockPos> actingBlocks(BlockPos blockPos, EnumFacing sideHit, World world, EntityPlayer player)
	{
		Set<BlockPos> positions = new HashSet<BlockPos>();
		BlockPos offsetPos = blockPos.offset(sideHit);

		IBlockState blockState = world.getBlockState(offsetPos);
		Block block = blockState.getBlock();

		if(block.getMaterial() == Material.air) positions.add(offsetPos);
		return positions;
	}

	protected void sendMessage(String message, EntityPlayer player)
	{
		player.addChatComponentMessage(new ChatComponentText(message));
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
	{
		if(worldIn.isRemote) return itemStackIn;

		Minecraft minecraft = Minecraft.getMinecraft();
		MovingObjectPosition mouseOver = minecraft.getRenderViewEntity().rayTrace(90.0, 1f);

		// Make sure the target is a valid block
		if(mouseOver == null)
		{
			sendMessage(EnumChatFormatting.AQUA + "No block targeted!", playerIn);
			return itemStackIn;
		}
		BlockPos blockPos = mouseOver.getBlockPos();
		if(blockPos == null)
		{
			sendMessage(EnumChatFormatting.AQUA + "No block targeted!", playerIn);
			return itemStackIn;
		}
		IBlockState blockState = worldIn.getBlockState(blockPos);
		Block block = blockState.getBlock();
		if(block == null || block.getMaterial() == Material.air)
		{
			sendMessage(EnumChatFormatting.AQUA + "No block targeted!", playerIn);
			return itemStackIn;
		}

		// Place the necessary blocks
		int meta = block.getMetaFromState(blockState);
		Set<BlockPos> positions = actingBlocks(blockPos, mouseOver.sideHit, worldIn, playerIn);
		for(BlockPos pos : positions)
		{
			if(!ItemUtils.useItemWithMeta(Item.getItemFromBlock(block), meta, playerIn.inventory, playerIn))
			{
				sendMessage(EnumChatFormatting.AQUA + "Building resource depleted!", playerIn);
				break;
			}
			else
			{
				// todo - play sound
				IBlockState state = block.getStateFromMeta(meta);
				worldIn.setBlockState(pos, state, 2);
				playerIn.openContainer.detectAndSendChanges();
			}
		}
		return itemStackIn;
	}
}