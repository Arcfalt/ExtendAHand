package com.arcfalt.extendahand.packet;

import com.arcfalt.extendahand.utils.ItemUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Set;

public class ExtendoNBTHandler implements IMessageHandler<ExtendoNBTMessage, IMessage>
{
	@Override
	public IMessage onMessage(final ExtendoNBTMessage message, final MessageContext ctx)
	{
		/*
		IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.worldObj;
		mainThread.addScheduledTask(new Runnable()
		{
			@Override
			public void run()
			{
				NetHandlerPlayServer serverHandler = ctx.getServerHandler();
				EntityPlayer player = serverHandler.playerEntity;
				Set<BlockPos> positions = message.positions;
				int meta = message.meta;
				Block useBlock = message.block;
				IBlockState setState = useBlock.getStateFromMeta(meta);

				for(BlockPos pos : positions)
				{
					if(ItemUtils.useItemWithMeta(Item.getItemFromBlock(useBlock), meta, player.inventory, player))
					{
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
			}
		});
		*/
		EntityPlayerMP serverPlayer = ctx.getServerHandler().playerEntity;
		ItemStack held = serverPlayer.getHeldItem();

		if(held.getItem() != message.stack.getItem()) return null;

		NBTTagCompound compound = ItemUtils.getOrCreateTagCompound(held);
		compound.merge(message.tags);
		return null;
	}
}
