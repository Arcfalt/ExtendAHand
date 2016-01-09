package com.arcfalt.extendahand.item;

import com.arcfalt.extendahand.ExtendAHand;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.HashSet;
import java.util.Set;

public class BaseExtendo extends Item
{
    @SideOnly(Side.CLIENT)
    public void drawHighlight(RenderWorldLastEvent event, EntityPlayerSP player, ItemStack stack)
    {
        // Find whatever is under the cursor up to a certain distance away
        Minecraft minecraft = Minecraft.getMinecraft();
        MovingObjectPosition mouseOver = minecraft.getRenderViewEntity().rayTrace(50.0, event.partialTicks);
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
            renderOutlines(event, player, coordinates, 1f, 0f, 1f, .6f);
        }
    }

    private static void renderCubes(Set<BlockPos> coordinates, float offset)
    {
        RenderHelper.enableStandardItemLighting();
        Tessellator t = Tessellator.getInstance();
        WorldRenderer renderer = t.getWorldRenderer();
        renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

        GlStateManager.color(1f, 1f, 1f, 1f);
        for (BlockPos coordinate : coordinates)
        {
            float x = coordinate.getX();
            float y = coordinate.getY();
            float z = coordinate.getZ();

            renderSingleCube(renderer, x, y, z, offset);
        }
        t.draw();
        RenderHelper.disableStandardItemLighting();
    }

    private static void renderSingleCube(WorldRenderer renderer, float x, float y, float z, float o)
    {
        x = x - o;
        y = y - o;
        z = z - o;
        o = 1f + (o * 2f);

        // alpha
        float a = .8f;

        // front and back color
        float r1 = 1f;
        float g1 = .3f;
        float b1 = 1f;

        // left and right color
        float r2 = 1f;
        float g2 = .15f;
        float b2 = 1f;

        // top color
        float r3 = 1f;
        float g3 = .45f;
        float b3 = 1f;

        // bottom color
        float r4 = 1f;
        float g4 = 0f;
        float b4 = 1f;

        // front
        renderer.pos(x + o, y + o, z + o).color(r1, g1, b1, a).endVertex();
        renderer.pos(x, y + o, z + o).color(r1, g1, b1, a).endVertex();
        renderer.pos(x, y, z + o).color(r1, g1, b1, a).endVertex();
        renderer.pos(x + o, y, z + o).color(r1, g1, b1, a).endVertex();

        // back
        renderer.pos(x, y, z).color(r1, g1, b1, a).endVertex();
        renderer.pos(x, y + o, z).color(r1, g1, b1, a).endVertex();
        renderer.pos(x + o, y + o, z).color(r1, g1, b1, a).endVertex();
        renderer.pos(x + o, y, z).color(r1, g1, b1, a).endVertex();

        // left
        renderer.pos(x, y + o, z + o).color(r2, g2, b2, a).endVertex();
        renderer.pos(x, y + o, z).color(r2, g2, b2, a).endVertex();
        renderer.pos(x, y, z).color(r2, g2, b2, a).endVertex();
        renderer.pos(x, y, z + o).color(r2, g2, b2, a).endVertex();

        // right
        renderer.pos(x + o, y, z).color(r2, g2, b2, a).endVertex();
        renderer.pos(x + o, y + o, z).color(r2, g2, b2, a).endVertex();
        renderer.pos(x + o, y + o, z + o).color(r2, g2, b2, a).endVertex();
        renderer.pos(x + o, y, z + o).color(r2, g2, b2, a).endVertex();

        // top
        renderer.pos(x, y + o, z).color(r3, g3, b3, a).endVertex();
        renderer.pos(x, y + o, z + o).color(r3, g3, b3, a).endVertex();
        renderer.pos(x + o, y + o, z + o).color(r3, g3, b3, a).endVertex();
        renderer.pos(x + o, y + o, z).color(r3, g3, b3, a).endVertex();

        // bottom
        renderer.pos(x + o, y, z + o).color(r4, g4, b4, a).endVertex();
        renderer.pos(x, y, z + o).color(r4, g4, b4, a).endVertex();
        renderer.pos(x, y, z).color(r4, g4, b4, a).endVertex();
        renderer.pos(x + o, y, z).color(r4, g4, b4, a).endVertex();
    }

    protected static void renderOutlines(RenderWorldLastEvent evt, EntityPlayerSP p, Set<BlockPos> coordinates, float r, float g, float b, float a)
    {
        double doubleX = p.lastTickPosX + (p.posX - p.lastTickPosX) * evt.partialTicks;
        double doubleY = p.lastTickPosY + (p.posY - p.lastTickPosY) * evt.partialTicks;
        double doubleZ = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * evt.partialTicks;

        GlStateManager.pushAttrib();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();

        GlStateManager.pushMatrix();
        GlStateManager.translate(-doubleX, -doubleY, -doubleZ);

        float offset = .01f;
        renderCubes(coordinates, offset);
        renderOutlines(coordinates, r, g, b, 1.5f, offset);

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }

    private static void renderOutlines(Set<BlockPos> coordinates, float r, float g, float b, float thickness, float offset)
    {
        RenderHelper.enableStandardItemLighting();

        Tessellator tessellator = Tessellator.getInstance();

        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);

        GlStateManager.color(r, g, b, 1f);
        GL11.glLineWidth(thickness);

        for (BlockPos coordinate : coordinates)
        {
            float x = coordinate.getX();
            float y = coordinate.getY();
            float z = coordinate.getZ();

            renderBlockOutline(worldRenderer, x, y, z, offset);
        }
        tessellator.draw();

        RenderHelper.disableStandardItemLighting();
    }

    private static void renderBlockOutline(WorldRenderer renderer, float ox, float oy, float oz, float off)
    {
        ox = ox - off;
        oy = oy - off;
        oz = oz - off;
        off = 1f + (off * 2f);

        renderer.pos(ox, oy, oz).endVertex();
        renderer.pos(ox + off, oy, oz).endVertex();
        renderer.pos(ox, oy, oz).endVertex();
        renderer.pos(ox, oy + off, oz).endVertex();

        renderer.pos(ox, oy, oz).endVertex();
        renderer.pos(ox, oy, oz + off).endVertex();
        renderer.pos(ox + off, oy + off, oz + off).endVertex();
        renderer.pos(ox, oy + off, oz + off).endVertex();

        renderer.pos(ox + off, oy + off, oz + off).endVertex();
        renderer.pos(ox + off, oy, oz + off).endVertex();
        renderer.pos(ox + off, oy + off, oz + off).endVertex();
        renderer.pos(ox + off, oy + off, oz).endVertex();

        renderer.pos(ox, oy + off, oz).endVertex();
        renderer.pos(ox, oy + off, oz + off).endVertex();
        renderer.pos(ox, oy + off, oz).endVertex();
        renderer.pos(ox + off, oy + off, oz).endVertex();

        renderer.pos(ox + off, oy, oz).endVertex();
        renderer.pos(ox + off, oy, oz + off).endVertex();
        renderer.pos(ox + off, oy, oz).endVertex();
        renderer.pos(ox + off, oy + off, oz).endVertex();

        renderer.pos(ox, oy, oz + off).endVertex();
        renderer.pos(ox + off, oy, oz + off).endVertex();
        renderer.pos(ox, oy, oz + off).endVertex();
        renderer.pos(ox, oy + off, oz + off).endVertex();
    }

}