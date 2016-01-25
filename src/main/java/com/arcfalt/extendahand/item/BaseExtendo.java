package com.arcfalt.extendahand.item;

import com.arcfalt.extendahand.config.Config;
import com.arcfalt.extendahand.packet.PacketHandler;
import com.arcfalt.extendahand.utils.ItemUtils;
import com.arcfalt.extendahand.utils.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;
import java.util.Set;

public class BaseExtendo extends Item
{
	/*
	Get the maximum amount of blocks this tool can place in a single action
	 */
	public int getMaxBlocks()
	{
		return 1;
	}

	/*
	Get the maximum block distance this tool can reach away
	 */
	public double getMaxDistance()
	{
		return Config.baseMaxDistance;
	}

	/*
	Draw the highlight effect over the area selected by the actingBlocks function
	Called by subscription event from render last
	 */
	@SideOnly(Side.CLIENT)
	public void drawHighlight(RenderWorldLastEvent event, EntityPlayerSP player, ItemStack stack)
	{
		MovingObjectPosition mouseOver = getMouseOver();
		BlockPos blockPos = getTargetBlockPos(player, mouseOver);
		if(blockPos == null) return;
		Set<BlockPos> positions = actingBlocks(blockPos, mouseOver.sideHit, player.worldObj, player, false);
		RenderUtils.renderBlockOverlays(event, player, positions, 1f, .3f, 1f, 0.001f);
	}

	/*
	Get the object position that is under the mouse at long range
	 */
	@SideOnly(Side.CLIENT)
	protected MovingObjectPosition getMouseOver()
	{
		// Non-use of partial ticks intended to match up render area with placement area bound to tick
		return Minecraft.getMinecraft().getRenderViewEntity().rayTrace(getMaxDistance(), 1f);
	}

	/*
	Get the block position that is at the object position given
	 */
	@SideOnly(Side.CLIENT)
	protected BlockPos getTargetBlockPos(EntityPlayer player, MovingObjectPosition mouseOver)
	{
		if(mouseOver == null || player == null) return null;

		// Get the block position and make sure it is a block
		World world = player.worldObj;
		BlockPos blockPos = mouseOver.getBlockPos();
		if(blockPos == null) return null;

		// Make sure the block is valid
		IBlockState blockState = world.getBlockState(blockPos);
		Block block = blockState.getBlock();
		if(block == null || block.getMaterial() == Material.air) return null;

		// All valid, return it
		return blockPos;
	}

	/*
	Get the block position that is under the mouse cursor up to long range
	 */
	@SideOnly(Side.CLIENT)
	protected BlockPos getTargetBlockPos(EntityPlayer player)
	{
		return getTargetBlockPos(player, getMouseOver());
	}

	/*
	Find the blocks to act upon
	Override this to return the desired blocks in child items
	 */
	protected Set<BlockPos> actingBlocks(BlockPos blockPos, EnumFacing sideHit, World world, EntityPlayer player, boolean trimAmount)
	{
		Set<BlockPos> positions = new HashSet<BlockPos>();
		return positions;
	}

	/*
	Send a message to the player from the extendo
	 */
	protected void sendMessage(String message, EntityPlayer player)
	{
		player.addChatComponentMessage(new ChatComponentText(message));
	}

	/*
	Get the current block state that the extendo has selected, falling back if nothing is selected
	 */
	public IBlockState getResourceState(ItemStack stack, IBlockState fallbackState)
	{
		NBTTagCompound tags = ItemUtils.getOrCreateTagCompound(stack);
		if(!tags.hasKey("extendoResource")) return fallbackState;
		NBTTagCompound eTag = (NBTTagCompound)tags.getTag("extendoResource");
		if(!eTag.hasKey("block") || !eTag.hasKey("meta")) return fallbackState;
		Block block = Block.blockRegistry.getObjectById(eTag.getInteger("block"));
		if(block == null) return fallbackState;
		IBlockState state = block.getStateFromMeta(eTag.getInteger("meta"));
		if(state == null) return fallbackState;
		return state;
	}

	/*
	Handle the right click functionality, which is by default building whatever actingBlocks selects
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
	{
		if(!worldIn.isRemote) return itemStackIn;

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
		worldIn.playSoundAtEntity(playerIn, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

		// Get necessary block data
		IBlockState setState = getResourceState(itemStackIn, blockState);
		Block useBlock = setState.getBlock();
		int meta = useBlock.getMetaFromState(setState);
		Set<BlockPos> positions = actingBlocks(blockPos, mouseOver.sideHit, worldIn, playerIn, true);

		// Send placement packet
		PacketHandler.sendExtendoPlacement(useBlock, meta, positions);
		return itemStackIn;
	}

	/*
	Handle item usage, which by default is selecting and deselecting resources when in sneak mode
	 */
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if(!playerIn.isSneaking()) return false;
		if(pos == null) return false;
		IBlockState blockState = worldIn.getBlockState(pos);
		Block block = blockState.getBlock();
		if(block == null || block.getMaterial() == Material.air) return false;
		if(worldIn.isRemote) return true;

		int blockId = Block.blockRegistry.getIDForObject(block);
		int blockMeta = block.getMetaFromState(blockState);
		NBTTagCompound tags = ItemUtils.getOrCreateTagCompound(stack);
		if(tags.hasKey("extendoResource"))
		{
			NBTTagCompound existingTag = (NBTTagCompound)tags.getTag("extendoResource");
			int existingId = existingTag.getInteger("block");
			int existingMeta = existingTag.getInteger("meta");
			if(existingId == blockId && existingMeta == blockMeta)
			{
				tags.removeTag("extendoResource");
				sendMessage(EnumChatFormatting.LIGHT_PURPLE + "Building resource deselected!", playerIn);
				return true;
			}
		}

		NBTTagCompound resourceTag = new NBTTagCompound();
		resourceTag.setInteger("block", blockId);
		resourceTag.setInteger("meta", blockMeta);
		tags.setTag("extendoResource", resourceTag);
		sendMessage(EnumChatFormatting.AQUA + "Building resource selected!", playerIn);
		return true;
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		if(!isDamageable()) return super.getIsRepairable(toRepair, repair);
		return repair.getItem() == Items.ender_pearl || repair.getItem() == Items.ender_eye || super.getIsRepairable(toRepair, repair);
	}
}