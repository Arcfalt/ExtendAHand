package com.arcfalt.extendahand.packet;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketHandler
{
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("arcfextendahand");

	public static void registerPackets()
	{
		INSTANCE.registerMessage(ExtendoPlaceHandler.class, ExtendoPlaceMessage.class, 0, Side.SERVER);
		INSTANCE.registerMessage(ExtendoNBTHandler.class, ExtendoNBTMessage.class, 1, Side.SERVER);
	}

	@SideOnly(Side.CLIENT)
	public static void sendExtendoPlacement(BlockPos target, EnumFacing side)
	{
		INSTANCE.sendToServer(new ExtendoPlaceMessage(target, side));
	}

	@SideOnly(Side.CLIENT)
	public static void sendExtendoNBT(BlockPos target)
	{
		INSTANCE.sendToServer(new ExtendoNBTMessage(target));
	}
}
