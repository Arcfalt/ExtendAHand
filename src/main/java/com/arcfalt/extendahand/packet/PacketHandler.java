package com.arcfalt.extendahand.packet;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

public class PacketHandler
{
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("arcfextendahand");

	public static void registerPackets()
	{
		INSTANCE.registerMessage(ExtendoPlaceHandler.class, ExtendoPlaceMessage.class, 0, Side.SERVER);
	}

	@SideOnly(Side.CLIENT)
	public static void sendExtendoPlacement(Block block, int meta, Set<BlockPos> positions)
	{
		INSTANCE.sendToServer(new ExtendoPlaceMessage(block, meta, positions));
	}
}
