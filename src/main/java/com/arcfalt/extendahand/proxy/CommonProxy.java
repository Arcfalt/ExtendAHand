package com.arcfalt.extendahand.proxy;

import com.arcfalt.extendahand.ModItems;
import com.arcfalt.extendahand.packet.PacketHandler;
import com.arcfalt.extendahand.tabs.ModTabs;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy
{
	public void preInit(FMLPreInitializationEvent e)
	{
		ModItems.init();
		ModTabs.init();
		ModItems.initTabs();
		PacketHandler.registerPackets();
	}

	public void init(FMLInitializationEvent e)
	{
		ModItems.initCrafting();
	}

	public void postInit(FMLPostInitializationEvent e)
	{
		//
	}
}