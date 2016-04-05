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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
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

	@Override
	@SideOnly(Side.CLIENT)
	public void drawHighlight(RenderWorldLastEvent event, EntityPlayerSP player, ItemStack stack)
	{
		RayTraceResult mouseOver = getMouseOver();
		BlockPos blockPos = getTargetBlockPos(player, mouseOver);
		Set<BlockPos> actingBlocks = actingBlocks(blockPos, mouseOver.sideHit, player.worldObj, player, false);
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
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		ActionResult<ItemStack> returnVal = new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
		if(!worldIn.isRemote) return returnVal;

		Minecraft minecraft = Minecraft.getMinecraft();
		RayTraceResult mouseOver = minecraft.getRenderViewEntity().rayTrace(90.0, 1f);

		// Make sure the target is a valid block
		if(mouseOver == null)
		{
			flipPointTarget(itemStackIn);
			return returnVal;
		}
		BlockPos blockPos = mouseOver.getBlockPos();
		if(blockPos == null)
		{
			flipPointTarget(itemStackIn);
			return returnVal;
		}
		IBlockState blockState = worldIn.getBlockState(blockPos);
		Block block = blockState.getBlock();

		NBTTagCompound gotTags = itemStackIn.getTagCompound();
		if(playerIn.isSneaking() && gotTags != null && gotTags.hasKey(LOC + 0) && gotTags.hasKey(LOC + 1))
		{
			PacketHandler.sendExtendoPlacement(blockPos, mouseOver.sideHit);
			return returnVal;
		}

		if(block == null || block.getMaterial(blockState) == Material.air)
		{
			flipPointTarget(itemStackIn);
			return returnVal;
		}
		//worldIn.playSoundAtEntity(playerIn, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

		PacketHandler.sendExtendoNBT(blockPos);
		return returnVal;
	}

	protected void flipPointTarget(ItemStack itemStackIn)
	{
		NBTTagCompound gotTags = itemStackIn.getTagCompound();
		if(gotTags == null) return;
		if(!gotTags.hasKey(LOC_NEXT)) return;
		int next = MathHelper.clamp_int(gotTags.getInteger(LOC_NEXT), 0, 1);
		if(!gotTags.hasKey(LOC + next)) return;
		BlockPos pos = BlockPos.fromLong(gotTags.getLong(LOC + next));
		if(pos == null) return;
		PacketHandler.sendExtendoNBT(pos);
	}
}
