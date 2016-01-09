package com.arcfalt.extendahand.item;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HandyExtendo extends BaseExtendo
{
    public HandyExtendo()
    {
        setRegistryName("handyextendo");
        setUnlocalizedName("handyextendo");
        GameRegistry.registerItem(this);
    }

    @SideOnly(Side.CLIENT)
    public void initModel()
    {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}