package com.arcfalt.extendahand.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class Config
{
	public static Configuration config;

	private static final int blocksMin = 1;
	private static final int blocksMax = 80000;
	private static final int distanceMin = 5;
	private static final int distanceMax = 800;

	private static final String baseCategory = "basic";
	public static int baseMaxDistance = 80;
	public static boolean baseCreativeOnly = false;

	private static final String lineCategory = "line";
	public static int lineMaxBlocks = 80;
	public static int lineMaxDistance = 80;
	public static boolean lineCreativeOnly = false;

	private static final String boxCategory = "box";
	public static int boxMaxBlocks = 300;
	public static int boxMaxDistance = 80;
	public static boolean boxCreativeOnly = false;

	public static void load(File configFile)
	{
		if(config == null)
		{
			config = new Configuration(configFile);
		}
		read();
	}

	private static void read()
	{
		baseMaxDistance = config.getInt("baseMaxDistance", baseCategory, baseMaxDistance, distanceMin, distanceMax, "The maximum block distance the basic tool can reach away from the player.");
		baseCreativeOnly = config.getBoolean("baseCreativeOnly", baseCategory, baseCreativeOnly, "If the basic tool should have no crafting recipe and be for creative mode only.");

		lineMaxBlocks = config.getInt("lineMaxBlocks", lineCategory, lineMaxBlocks, blocksMin, blocksMax, "The maximum amount of blocks the line tool can place in a single operation.");
		lineMaxDistance = config.getInt("lineMaxDistance", lineCategory, lineMaxDistance, distanceMin, distanceMax, "The maximum block distance the line tool can reach away from the player.");
		lineCreativeOnly = config.getBoolean("lineCreativeOnly", lineCategory, lineCreativeOnly, "If the line tool should have no crafting recipe and be for creative mode only.");

		boxMaxBlocks = config.getInt("boxMaxBlocks", boxCategory, boxMaxBlocks, blocksMin, blocksMax, "The maximum amount of blocks the box tool can place in a single operation.");
		boxMaxDistance = config.getInt("boxMaxDistance", boxCategory, boxMaxDistance, distanceMin, distanceMax, "The maximum block distance the box tool can reach away from the player.");
		boxCreativeOnly = config.getBoolean("boxCreativeOnly", boxCategory, boxCreativeOnly, "If the box tool should have no crafting recipe and be for creative mode only.");

		if(config.hasChanged())
		{
			config.save();
		}
	}
}
