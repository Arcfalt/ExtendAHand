package com.arcfalt.extendahand.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ExtendoNBTMessage implements IMessage
{
	ItemStack stack;
	NBTTagCompound tags;

	public ExtendoNBTMessage()
	{
	}

	public ExtendoNBTMessage(ItemStack stack, NBTTagCompound tags)
	{
		this.stack = stack;
		this.tags = tags;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.stack = ByteBufUtils.readItemStack(buf);
		this.tags = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeItemStack(buf, this.stack);
		ByteBufUtils.writeTag(buf, this.tags);
	}
}
