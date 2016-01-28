package com.arcfalt.extendahand.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class Config
{
	public static Configuration config;

	private static final int blocksMin = 1;
	private static final int blocksMax = 20000;
	private static final int distanceMin = 5;
	private static final int distanceMax = 800;
	private static final int durabilityMin = 0;
	private static final int durabilityMax = 100000;
	private static final float cooldownMin = 0.0f;
	private static final float cooldownMax = 300.0f;
	private static final int axisMin = 5;
	private static final int axisMax = 800;
	private static final int undoMin = 0;
	private static final int undoMax = 8;
	private static final int undoMaxBox = 3;

	private static final String baseCategory = "basic";
	public static int baseMaxDistance = 80;
	public static boolean baseCreativeOnly = false;
	public static int baseDurability = 0;
	public static float baseCooldown = 0.0f;
	public static int baseUndoLevels = 2;

	private static final String lineCategory = "line";
	public static int lineMaxBlocks = 80;
	public static int lineMaxDistance = 80;
	public static boolean lineCreativeOnly = false;
	public static int lineDurability = 0;
	public static float lineCooldown = 0.0f;
	public static int lineUndoLevels = 2;

	private static final String boxCategory = "box";
	public static int boxMaxBlocks = 300;
	public static int boxMaxDistance = 80;
	public static boolean boxCreativeOnly = false;
	public static int boxDurability = 0;
	public static float boxCooldown = 0.0f;
	public static int boxMaxAxis = 100;
	public static int boxUndoLevels = 0;

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
		baseDurability = config.getInt("baseDurability", baseCategory, baseDurability, durabilityMin, durabilityMax, "The durability of the basic tool. Each block placed uses 1 durability. Set to 0 to disable item durability and have infinite usages.");
		baseCooldown = config.getFloat("baseCooldown", baseCategory, baseCooldown, cooldownMin, cooldownMax, "The amount of time in seconds until the basic tool can be used again. Set to 0.0 to disable the cooldown and allow for instant usage.");
		baseUndoLevels = config.getInt("baseUndoLevels", baseCategory, baseUndoLevels, undoMin, undoMax, "The amount of actions the basic tool will remember so that they can be undone.");

		lineMaxBlocks = config.getInt("lineMaxBlocks", lineCategory, lineMaxBlocks, blocksMin, blocksMax, "The maximum amount of blocks the line tool can place in a single operation.");
		lineMaxDistance = config.getInt("lineMaxDistance", lineCategory, lineMaxDistance, distanceMin, distanceMax, "The maximum block distance the line tool can reach away from the player.");
		lineCreativeOnly = config.getBoolean("lineCreativeOnly", lineCategory, lineCreativeOnly, "If the line tool should have no crafting recipe and be for creative mode only.");
		lineDurability = config.getInt("lineDurability", lineCategory, lineDurability, durabilityMin, durabilityMax, "The durability of the line tool. Each block placed uses 1 durability. Set to 0 to disable item durability and have infinite usages.");
		lineCooldown = config.getFloat("lineCooldown", lineCategory, lineCooldown, cooldownMin, cooldownMax, "The amount of time in seconds until the line tool can be used again. Set to 0.0 to disable the cooldown and allow for instant usage.");
		lineUndoLevels = config.getInt("lineUndoLevels", lineCategory, lineUndoLevels, undoMin, undoMax, "The amount of actions the line tool will remember so that they can be undone.");

		boxMaxBlocks = config.getInt("boxMaxBlocks", boxCategory, boxMaxBlocks, blocksMin, blocksMax, "The maximum amount of blocks the box tool can place in a single operation.");
		boxMaxDistance = config.getInt("boxMaxDistance", boxCategory, boxMaxDistance, distanceMin, distanceMax, "The maximum block distance the box tool can reach away from the player.");
		boxCreativeOnly = config.getBoolean("boxCreativeOnly", boxCategory, boxCreativeOnly, "If the box tool should have no crafting recipe and be for creative mode only.");
		boxDurability = config.getInt("boxDurability", boxCategory, boxDurability, durabilityMin, durabilityMax, "The durability of the box tool. Each block placed uses 1 durability. Set to 0 to disable item durability and have infinite usages.");
		boxCooldown = config.getFloat("boxCooldown", boxCategory, boxCooldown, cooldownMin, cooldownMax, "The amount of time in seconds until the box tool can be used again. Set to 0.0 to disable the cooldown and allow for instant usage.");
		boxMaxAxis = config.getInt("boxMaxAxis", boxCategory, boxMaxAxis, axisMin, axisMax, "The maximum length of a single axis in the selection of the box tool.");
		boxUndoLevels = config.getInt("boxUndoLevels", boxCategory, boxUndoLevels, undoMin, undoMaxBox, "The amount of actions the box tool will remember so that they can be undone. WARNING: As the box tool sets a large amount of blocks at once, it is recommended to leave this off or very low unless you are running a small private server.");

		if(config.hasChanged())
		{
			config.save();
		}
	}
}
