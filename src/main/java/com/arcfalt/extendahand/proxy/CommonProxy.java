package com.arcfalt.extendahand.proxy;

import com.arcfalt.extendahand.ModItems;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy
{
    public void preInit(FMLPreInitializationEvent e)
    {
        ModItems.init();
        ModItems.initModels();
    }

    public void init(FMLInitializationEvent e)
    {
        //
    }

    public void postInit(FMLPostInitializationEvent e)
    {
        //
    }
}