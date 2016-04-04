package com.arcfalt.extendahand.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ExtendoNBTMessage implements IMessage
{
	BlockPos target;

	public ExtendoNBTMessage()
	{
	}

	public ExtendoNBTMessage(BlockPos target)
	{
		this.target = target;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		target = BlockPos.fromLong(buf.readLong());
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeLong(target.toLong());
	}
}
