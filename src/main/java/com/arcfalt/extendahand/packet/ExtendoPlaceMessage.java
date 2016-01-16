package com.arcfalt.extendahand.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.HashSet;
import java.util.Set;

public class ExtendoPlaceMessage implements IMessage
{
	Block block;
	int meta;
	Set<BlockPos> positions;

	public ExtendoPlaceMessage()
	{
	}

	public ExtendoPlaceMessage(Block block, int meta, Set<BlockPos> positions)
	{
		this.block = block;
		this.meta = meta;
		this.positions = positions;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		int blockId = buf.readInt();
		block = Block.blockRegistry.getObjectById(blockId);
		this.meta = buf.readInt();
		int count = buf.readInt();
		this.positions = new HashSet<BlockPos>();
		for(int i = 0; i < count; i++)
		{
			long longPos = buf.readLong();
			this.positions.add(BlockPos.fromLong(longPos));
		}
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(Block.blockRegistry.getIDForObject(block));
		buf.writeInt(meta);
		buf.writeInt(positions.size());
		for(BlockPos pos : positions)
		{
			buf.writeLong(pos.toLong());
		}
	}
}