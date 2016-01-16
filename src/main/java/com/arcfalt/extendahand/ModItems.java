package com.arcfalt.extendahand;

import com.arcfalt.extendahand.item.ExtendedExtendo;
import com.arcfalt.extendahand.item.HandyExtendo;
import com.arcfalt.extendahand.tabs.ModTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
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

	public static void initTabs()
	{
		handyExtendo.setCreativeTab(ModTabs.mainTab);
		extendedExtendo.setCreativeTab(ModTabs.mainTab);
	}

	@SideOnly(Side.CLIENT)
	public static void initModels()
	{
		handyExtendo.initModel();
		extendedExtendo.initModel();
	}

	public static void initCrafting()
	{
		GameRegistry.addRecipe(new ItemStack(handyExtendo), ".P.", "IDI", ".S.", 'I', Items.ender_pearl, 'D', Items.diamond, 'P', Blocks.piston, 'S', Blocks.sticky_piston);
		GameRegistry.addRecipe(new ItemStack(extendedExtendo), "BPB", "IEI", "BSB", 'I', Items.ender_eye, 'E', handyExtendo, 'B', Items.blaze_rod, 'P', Blocks.piston, 'S', Blocks.sticky_piston);
	}
}