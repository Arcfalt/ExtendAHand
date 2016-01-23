package com.arcfalt.extendahand;

import com.arcfalt.extendahand.config.Config;
import com.arcfalt.extendahand.item.ExtendedExtendo;
import com.arcfalt.extendahand.item.HandyExtendo;
import com.arcfalt.extendahand.item.PlanarExtendo;
import com.arcfalt.extendahand.tabs.ModTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems
{
	public static HandyExtendo handyExtendo;
	public static ExtendedExtendo extendedExtendo;
	public static PlanarExtendo planarExtendo;

	public static void init()
	{
		handyExtendo = new HandyExtendo();
		extendedExtendo = new ExtendedExtendo();
		planarExtendo = new PlanarExtendo();
	}

	public static void initTabs()
	{
		handyExtendo.setCreativeTab(ModTabs.mainTab);
		extendedExtendo.setCreativeTab(ModTabs.mainTab);
		planarExtendo.setCreativeTab(ModTabs.mainTab);
	}

	@SideOnly(Side.CLIENT)
	public static void initModels()
	{
		handyExtendo.initModel();
		extendedExtendo.initModel();
		planarExtendo.initModel();
	}

	public static void initCrafting()
	{
		if(!Config.baseCreativeOnly)
		{
			GameRegistry.addRecipe(new ItemStack(handyExtendo), ".P.", "IDI", ".P.", 'I', Items.ender_pearl, 'D', Items.diamond, 'P', Blocks.piston);
		}
		if(!Config.lineCreativeOnly)
		{
			GameRegistry.addRecipe(new ItemStack(extendedExtendo), "BPB", "IEI", "BSB", 'I', Items.ender_eye, 'E', handyExtendo, 'B', Items.blaze_rod, 'P', Blocks.piston, 'S', Blocks.sticky_piston);
		}
		if(!Config.boxCreativeOnly)
		{
			GameRegistry.addRecipe(new ItemStack(planarExtendo), "BSB", "IEI", "WSW", 'I', Items.ender_eye, 'E', handyExtendo, 'B', Items.ghast_tear, 'S', Blocks.sticky_piston, 'W', new ItemStack(Items.potionitem, 1, 8203));
		}
	}
}