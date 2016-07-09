package com.arcfalt.extendahand;

import com.arcfalt.extendahand.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.apache.logging.log4j.Logger;

@Mod(modid = ExtendAHand.MODID, name = ExtendAHand.MODNAME, dependencies = "required-after:Forge@[12.16.0.1826,)", useMetadata = true, version = ExtendAHand.VERSION)
public class ExtendAHand
{

	public static final String MODID = "ExtendAHand";
	public static final String MODNAME = "Extend-A-Hand";
	public static final String VERSION = "1.1";

	@SidedProxy(clientSide = "com.arcfalt.extendahand.proxy.ClientProxy", serverSide = "com.arcfalt.extendahand.proxy.ServerProxy")
	public static CommonProxy proxy;

	@Mod.Instance
	public static ExtendAHand instance;

	public static Logger logger;

	public static SimpleNetworkWrapper networkWrapper;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		logger = event.getModLog();
		proxy.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent e)
	{
		proxy.init(e);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent e)
	{
		proxy.postInit(e);
	}
}