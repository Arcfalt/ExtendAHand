package com.arcfalt.extendahand.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;

public class ItemUtils
{
	public static int findItemWithMeta(Item item, int meta, InventoryPlayer inventory)
	{
		for(int i = 0; i < inventory.mainInventory.length; ++i)
		{
			if(inventory.mainInventory[i] != null && inventory.mainInventory[i].getItem() == item && meta == inventory.mainInventory[i].getItemDamage())
			{
				return i;
			}
		}
		return -1;
	}

	public static boolean useItemWithMeta(Item item, int meta, InventoryPlayer inventory, EntityPlayer player)
	{
		if(player.capabilities.isCreativeMode) return true;
		int i = findItemWithMeta(item, meta, inventory);

		if(i < 0) return false;
		if(--inventory.mainInventory[i].stackSize <= 0)
		{
			inventory.mainInventory[i] = null;
		}
		return true;
	}
}
