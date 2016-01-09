package com.arcfalt.extendahand.item;

import com.arcfalt.extendahand.utils.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;
import java.util.Set;

public class BaseExtendo extends Item
{
    @SideOnly(Side.CLIENT)
    public void drawHighlight(RenderWorldLastEvent event, EntityPlayerSP player, ItemStack stack)
    {
        // Find whatever is under the cursor up to a certain distance away
        Minecraft minecraft = Minecraft.getMinecraft();
        MovingObjectPosition mouseOver = minecraft.getRenderViewEntity().rayTrace(90.0, event.partialTicks);
        if (mouseOver == null) return;

        // Get the block position and make sure it is a block
        World world = player.worldObj;
        BlockPos blockPos = mouseOver.getBlockPos();
        if (blockPos == null) return;

        IBlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();
        if (block != null && block.getMaterial() != Material.air)
        {
            Set<BlockPos> coordinates;
            //int meta = block.getMetaFromState(blockState);

            //coordinates = findSuitableBlocks(wand, world, mouseOver.sideHit, blockPos, block, meta);
            coordinates = new HashSet<BlockPos>();
            coordinates.add(blockPos);
            RenderUtils.renderBlockOverlays(event, player, coordinates);
        }
    }



}