package com.arcfalt.extendahand.packet;

import com.arcfalt.extendahand.item.BaseExtendo;
import com.arcfalt.extendahand.utils.ItemUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.*;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Set;

public class ExtendoPlaceHandler implements IMessageHandler<ExtendoPlaceMessage, IMessage>
{
	@Override
	public IMessage onMessage(final ExtendoPlaceMessage message, final MessageContext ctx)
	{
		IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.worldObj;
		mainThread.addScheduledTask(new Runnable()
		{
			@Override
			public void run()
			{
				NetHandlerPlayServer serverHandler = ctx.getServerHandler();
				EntityPlayer player = serverHandler.playerEntity;

				ItemStack heldStack = player.getHeldItem();
				Item heldItem = heldStack.getItem();
				if(!(heldItem instanceof BaseExtendo)) return;
				BaseExtendo extendo = (BaseExtendo)heldItem;

				IBlockState blockState = player.worldObj.getBlockState(message.target);
				IBlockState setState = extendo.getResourceState(heldStack, blockState);
				Block useBlock = setState.getBlock();
				int meta = useBlock.getMetaFromState(setState);
				Set<BlockPos> positions = extendo.actingBlocks(message.target, message.side, player.worldObj, player, true);

				if(heldStack.isItemStackDamageable() && heldStack.getItemDamage() >= heldStack.getMaxDamage())
				{
					String message = EnumChatFormatting.RED + "Item is too damaged to use!";
					player.addChatComponentMessage(new ChatComponentText(message));
					return;
				}

				int maxBlocks = extendo.getMaxBlocks();
				int placedBlocks = 0;

				for(BlockPos pos : positions)
				{
					if(ItemUtils.useItemWithMeta(Item.getItemFromBlock(useBlock), meta, player.inventory, player))
					{
						placedBlocks += 1;
						if(placedBlocks > maxBlocks)
						{
							String message = EnumChatFormatting.AQUA + "Maximum limit of " + maxBlocks + " blocks created!";
							player.addChatComponentMessage(new ChatComponentText(message));
							break;
						}
						player.worldObj.setBlockState(pos, setState, 2);
						player.openContainer.detectAndSendChanges();
					}
					else
					{
						String message = EnumChatFormatting.AQUA + "Building resource depleted!";
						player.addChatComponentMessage(new ChatComponentText(message));
						break;
					}
				}

				if(heldStack.isItemStackDamageable())
				{
					int newDamage = Math.min(heldStack.getItemDamage() + placedBlocks, heldStack.getMaxDamage());
					heldStack.setItemDamage(newDamage);
					player.openContainer.detectAndSendChanges();
				}
			}
		});
		return null;
	}
}