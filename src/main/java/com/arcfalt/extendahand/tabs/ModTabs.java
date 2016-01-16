package com.arcfalt.extendahand.tabs;

import com.arcfalt.extendahand.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModTabs
{
	public static CreativeTabs mainTab;

	public static void init()
	{
		mainTab = new CreativeTabs("arcf_extendahand_creativetab")
		{
			@Override
			@SideOnly(Side.CLIENT)
			public Item getTabIconItem()
			{
				return ModItems.extendedExtendo;
			}
		};
	}
}
