package com.arcfalt.extendahand.packet;

import com.arcfalt.extendahand.item.BaseExtendo;
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
		EntityPlayerMP serverPlayer = ctx.getServerHandler().playerEntity;
		ItemStack held = serverPlayer.getHeldItem();

		if(held.getItem() != message.stack.getItem()) return null;
		Item heldItem = held.getItem();
		if(!(heldItem instanceof BaseExtendo)) return null;

		NBTTagCompound compound = ItemUtils.getOrCreateTagCompound(held);
		compound.merge(message.tags);
		return null;
	}
}
