package com.arcfalt.extendahand.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class Config
{
	public static Configuration config;

	public static void load(File configFile)
	{
		if(config == null)
		{
			config = new Configuration(configFile);
		}
		read();
	}

	public static void read()
	{
		try
		{
			//testValue = config.getBoolean(Configuration.CATEGORY_GENERAL, "testValue", true, "Test Config Value");
		}
		catch (Exception e)
		{
		}
		finally
		{
			if(config.hasChanged())
			{
				config.save();
			}
		}
	}
}
