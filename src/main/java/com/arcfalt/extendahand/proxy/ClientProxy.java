package com.arcfalt.extendahand.proxy;

import com.arcfalt.extendahand.ModItems;
import com.arcfalt.extendahand.item.BaseExtendo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientProxy extends CommonProxy
{
	@Override
	public void preInit(FMLPreInitializationEvent e)
	{
		super.preInit(e);
		ModItems.initModels();
	}

	@Override
	public void init(FMLInitializationEvent e)
	{
		super.init(e);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void renderWorldLastEvent(RenderWorldLastEvent evt)
	{
		// Get the player and their held item
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
		ItemStack heldItem = player.getHeldItem();

		// Check if the held item is an extendo
		if(heldItem == null) return;
		if(heldItem.getItem() instanceof BaseExtendo)
		{
			// Call the highlight on the held extendo
			BaseExtendo extendo = (BaseExtendo) heldItem.getItem();
			extendo.drawHighlight(evt, player, heldItem);
		}
	}
}