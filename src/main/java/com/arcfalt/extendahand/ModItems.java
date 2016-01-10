package com.arcfalt.extendahand;

import com.arcfalt.extendahand.item.ExtendedExtendo;
import com.arcfalt.extendahand.item.HandyExtendo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems
{
	public static HandyExtendo handyExtendo;
	public static ExtendedExtendo extendedExtendo;

	public static void init()
	{
		handyExtendo = new HandyExtendo();
		extendedExtendo = new ExtendedExtendo();
	}

	@SideOnly(Side.CLIENT)
	public static void initModels()
	{
		handyExtendo.initModel();
		extendedExtendo.initModel();
	}
}