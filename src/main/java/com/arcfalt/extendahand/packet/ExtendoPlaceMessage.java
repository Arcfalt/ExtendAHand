package com.arcfalt.extendahand.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.HashSet;
import java.util.Set;

public class ExtendoPlaceMessage implements IMessage
{
	BlockPos target;
	EnumFacing side;

	public ExtendoPlaceMessage()
	{
	}

	public ExtendoPlaceMessage(BlockPos target, EnumFacing side)
	{
		this.target = target;
		this.side = side;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		target = BlockPos.fromLong(buf.readLong());
		side = EnumFacing.getFront(buf.readInt());
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeLong(target.toLong());
		buf.writeInt(side.getIndex());
	}
}