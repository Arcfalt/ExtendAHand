package com.arcfalt.extendahand.item;

import com.arcfalt.extendahand.packet.PacketHandler;
import com.arcfalt.extendahand.utils.ItemUtils;
import com.arcfalt.extendahand.utils.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;
import java.util.Set;

public class BasePointExtendo extends BaseExtendo
{
	static final String LOC = "extendoLoc";
	static final String LOC_NEXT = "extendoLocNext";

	@Override
	@SideOnly(Side.CLIENT)
	public void drawHighlight(RenderWorldLastEvent event, EntityPlayerSP player, ItemStack stack)
	{
		MovingObjectPosition mouseOver = getMouseOver();
		BlockPos blockPos = getTargetBlockPos(player, mouseOver);
		Set<BlockPos> actingBlocks = actingBlocks(blockPos, mouseOver.sideHit, player.worldObj, player);
		RenderUtils.renderBlockOverlays(event, player, actingBlocks, 1f, .8f, 1f, 0.001f);
		float targetOffset = 0.006f;

		// Draw existing selected points
		NBTTagCompound tags = ItemUtils.getOrCreateTagCompound(stack);
		String posTag = LOC + 0;
		if(tags.hasKey(posTag))
		{
			Set<BlockPos> loc0pos = new HashSet<BlockPos>();
			loc0pos.add(BlockPos.fromLong(tags.getLong(posTag)));
			RenderUtils.renderBlockOverlays(event, player, loc0pos, 1f, .3f, .3f, targetOffset);
		}
		posTag = LOC + 1;
		if(tags.hasKey(posTag))
		{
			Set<BlockPos> loc1pos = new HashSet<BlockPos>();
			loc1pos.add(BlockPos.fromLong(tags.getLong(posTag)));
			RenderUtils.renderBlockOverlays(event, player, loc1pos, .3f, .3f, 1f, targetOffset);
		}

		// Find mouseover object
		if(blockPos == null) return;

		// Draw mouseover point placement
		float r = 1f;
		float g = .3f;
		float b = 1f;
		if(tags.hasKey(LOC_NEXT) && tags.getInteger(LOC_NEXT) != 0) r = .3f;
		else b = .3f;
		Set<BlockPos> positions = new HashSet<BlockPos>();
		positions.add(blockPos);
		RenderUtils.renderBlockOverlays(event, player, positions, r, g, b, targetOffset + 0.0001f);
	}

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

		NBTTagCompound gotTags = itemStackIn.getTagCompound();
		if(playerIn.isSneaking() && gotTags != null && gotTags.hasKey(LOC + 0) && gotTags.hasKey(LOC + 1))
		{
			IBlockState setState = getResourceState(itemStackIn, blockState);
			Block useBlock = setState.getBlock();
			int meta = useBlock.getMetaFromState(setState);
			Set<BlockPos> positions = actingBlocks(blockPos, mouseOver.sideHit, playerIn.worldObj, playerIn);
			PacketHandler.sendExtendoPlacement(useBlock, meta, positions);
			return itemStackIn;
		}

		if(block == null || block.getMaterial() == Material.air)
		{
			sendMessage(EnumChatFormatting.AQUA + "No block targeted!", playerIn);
			return itemStackIn;
		}
		worldIn.playSoundAtEntity(playerIn, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

		NBTTagCompound tags;

		if(gotTags == null) tags = new NBTTagCompound();
		else tags = (NBTTagCompound) gotTags.copy();

		int placeIn = 0;
		if(tags.hasKey(LOC_NEXT))
		{
			placeIn = tags.getInteger(LOC_NEXT);
			placeIn = MathHelper.clamp_int(placeIn, 0, 1);
		}
		tags.setLong(LOC + placeIn, blockPos.toLong());
		tags.setInteger(LOC_NEXT, 1 - placeIn);
		//tags.
		PacketHandler.sendExtendoNBT(itemStackIn, tags);

		/*
		// Get necessary block data
		IBlockState setState = getResourceState(itemStackIn, blockState);
		Block useBlock = setState.getBlock();
		int meta = useBlock.getMetaFromState(setState);
		Set<BlockPos> positions = actingBlocks(blockPos, mouseOver.sideHit, worldIn, playerIn);

		// Send placement packet
		PacketHandler.sendExtendoPlacement(useBlock, meta, positions);
		*/
		return itemStackIn;
	}
}
