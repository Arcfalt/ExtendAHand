package com.arcfalt.extendahand.packet;

import com.arcfalt.extendahand.item.BasePointExtendo;
import com.arcfalt.extendahand.utils.ItemUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ExtendoNBTHandler implements IMessageHandler<ExtendoNBTMessage, IMessage>
{
	static final String LOC = "extendoLoc";
	static final String LOC_NEXT = "extendoLocNext";

	@Override
	public IMessage onMessage(final ExtendoNBTMessage message, final MessageContext ctx)
	{
		EntityPlayerMP player = ctx.getServerHandler().playerEntity;
		ItemStack itemStackIn = player.getHeldItem(EnumHand.MAIN_HAND);
		Item heldItem = itemStackIn.getItem();
		if(!(heldItem instanceof BasePointExtendo)) return null;

		NBTTagCompound tags = ItemUtils.getOrCreateTagCompound(itemStackIn);
		int placeIn = 0;
		if(tags.hasKey(LOC_NEXT))
		{
			placeIn = tags.getInteger(LOC_NEXT);
			placeIn = MathHelper.clamp_int(placeIn, 0, 1);
		}
		tags.setLong(LOC + placeIn, message.target.toLong());
		tags.setInteger(LOC_NEXT, 1 - placeIn);
		player.inventoryContainer.detectAndSendChanges();
		return null;
	}
}
