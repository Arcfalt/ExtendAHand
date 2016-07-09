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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
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
		RayTraceResult mouseOver = getMouseOver();
		BlockPos blockPos = getTargetBlockPos(player, mouseOver);
		if(blockPos == null) return;
		Set<BlockPos> positions = actingBlocks(blockPos, mouseOver.sideHit, player.worldObj, player, false);
		RenderUtils.renderBlockOverlays(event, player, positions, 1f, .3f, 1f, 0.001f);
	}

	/*
	Get the object position that is under the mouse at long range
	 */
	@SideOnly(Side.CLIENT)
	protected RayTraceResult getMouseOver()
	{
		// Non-use of partial ticks intended to match up render area with placement area bound to tick
		return Minecraft.getMinecraft().getRenderViewEntity().rayTrace(getMaxDistance(), 1f);
	}

	/*
	Get the block position that is at the object position given
	 */
	@SideOnly(Side.CLIENT)
	protected BlockPos getTargetBlockPos(EntityPlayer player, RayTraceResult mouseOver)
	{
		if(mouseOver == null || player == null) return null;

		// Get the block position and make sure it is a block
		World world = player.worldObj;
		BlockPos blockPos = mouseOver.getBlockPos();
		if(blockPos == null) return null;

		// Make sure the block is valid
		IBlockState blockState = world.getBlockState(blockPos);
		Block block = blockState.getBlock();
		if(block == null || block.getMaterial(blockState) == Material.AIR) return null;

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
	public Set<BlockPos> actingBlocks(BlockPos blockPos, EnumFacing sideHit, World world, EntityPlayer player, boolean trimAmount)
	{
		Set<BlockPos> positions = new HashSet<BlockPos>();
		return positions;
	}

	/*
	Send a message to the player from the extendo
	 */
	protected void sendMessage(String message, EntityPlayer player)
	{
		player.addChatComponentMessage(new TextComponentString(message));
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
		Block block = Block.REGISTRY.getObjectById(eTag.getInteger("block"));
		if(block == null) return fallbackState;
		IBlockState state = block.getStateFromMeta(eTag.getInteger("meta"));
		if(state == null) return fallbackState;
		return state;
	}

	/*
	Handle the right click functionality, which is by default building whatever actingBlocks selects
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		ActionResult<ItemStack> returnVal = new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
		if(!worldIn.isRemote) return returnVal;

		Minecraft minecraft = Minecraft.getMinecraft();
		RayTraceResult mouseOver = minecraft.getRenderViewEntity().rayTrace(90.0, 1f);

		// Make sure the target is a valid block
		if(mouseOver == null)
		{
			sendMessage("No block targeted!", playerIn);
			return returnVal;
		}
		BlockPos blockPos = mouseOver.getBlockPos();
		if(blockPos == null)
		{
			sendMessage("No block targeted!", playerIn);
			return returnVal;
		}
		IBlockState blockState = worldIn.getBlockState(blockPos);
		Block block = blockState.getBlock();
		if(block == null || block.getMaterial(blockState) == Material.AIR)
		{
			sendMessage("No block targeted!", playerIn);
			return returnVal;
		}
		//worldIn.playSoundAtEntity(playerIn, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

		// Send placement packet
		PacketHandler.sendExtendoPlacement(blockPos, mouseOver.sideHit);
		return returnVal;
	}

	/*
	Handle item usage, which by default is selecting and deselecting resources when in sneak mode
	 */
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(!playerIn.isSneaking()) return EnumActionResult.PASS;
		if(pos == null) return EnumActionResult.PASS;
		IBlockState blockState = worldIn.getBlockState(pos);
		Block block = blockState.getBlock();
		if(block == null || block.getMaterial(blockState) == Material.AIR) return EnumActionResult.PASS;
		if(worldIn.isRemote) return EnumActionResult.SUCCESS;

		int blockId = Block.REGISTRY.getIDForObject(block);
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
				sendMessage("Building resource deselected!", playerIn);
				return EnumActionResult.SUCCESS;
			}
		}

		NBTTagCompound resourceTag = new NBTTagCompound();
		resourceTag.setInteger("block", blockId);
		resourceTag.setInteger("meta", blockMeta);
		tags.setTag("extendoResource", resourceTag);
		sendMessage("Building resource selected!", playerIn);
		return EnumActionResult.SUCCESS;
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		if(!isDamageable()) return super.getIsRepairable(toRepair, repair);
		return repair.getItem() == Items.ENDER_PEARL || repair.getItem() == Items.ENDER_EYE || super.getIsRepairable(toRepair, repair);
	}

	public boolean getHasCooldown()
	{
		return Config.baseCooldown > 0;
	}

	public int getCooldown()
	{
		return Config.baseCooldown;
	}
}