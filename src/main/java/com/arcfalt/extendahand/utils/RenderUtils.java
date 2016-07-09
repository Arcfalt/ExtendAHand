package com.arcfalt.extendahand.utils;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

import java.util.Set;

public class RenderUtils
{
	public static void renderBlockOverlays(RenderWorldLastEvent event, EntityPlayerSP player, Set<BlockPos> positions, float r, float g, float b, float offset)
	{
		if(positions == null || positions.size() == 0) return;

		double doubleX = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
		double doubleY = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
		double doubleZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();

		GlStateManager.pushAttrib();
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();

		GlStateManager.pushMatrix();
		GlStateManager.translate(-doubleX, -doubleY, -doubleZ);

		renderCubes(positions, offset, r, g, b, .8f);
		GlStateManager.disableBlend();
		renderOutlines(positions, 1.5f, offset + 0.001f, 0f, 0f, 0f, 1f);

		// get around vanilla minecraft opengl bug by resetting attribs pre-pop
		GlStateManager.enableTexture2D();
		GlStateManager.enableLighting();
		GlStateManager.color(1f, 1f, 1f, 1f);

		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
	}

	private static void renderCubes(Set<BlockPos> positions, float offset, float r, float g, float b, float a)
	{
		RenderHelper.enableStandardItemLighting();
		Tessellator t = Tessellator.getInstance();
		VertexBuffer renderer = t.getBuffer();
		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		GlStateManager.color(r, g, b, a);
		for(BlockPos coordinate : positions)
		{
			float x = coordinate.getX();
			float y = coordinate.getY();
			float z = coordinate.getZ();

			renderSingleCube(renderer, x, y, z, offset);
		}
		t.draw();
		RenderHelper.disableStandardItemLighting();
	}

	private static void renderSingleCube(VertexBuffer renderer, float x, float y, float z, float o)
	{
		// offsetting
		x = x - o;
		y = y - o;
		z = z - o;
		o = 1f + (o * 2f);

		// front
		renderer.pos(x + o, y + o, z + o).endVertex();
		renderer.pos(x, y + o, z + o).endVertex();
		renderer.pos(x, y, z + o).endVertex();
		renderer.pos(x + o, y, z + o).endVertex();

		// back
		renderer.pos(x, y, z).endVertex();
		renderer.pos(x, y + o, z).endVertex();
		renderer.pos(x + o, y + o, z).endVertex();
		renderer.pos(x + o, y, z).endVertex();

		// left
		renderer.pos(x, y + o, z + o).endVertex();
		renderer.pos(x, y + o, z).endVertex();
		renderer.pos(x, y, z).endVertex();
		renderer.pos(x, y, z + o).endVertex();

		// right
		renderer.pos(x + o, y, z).endVertex();
		renderer.pos(x + o, y + o, z).endVertex();
		renderer.pos(x + o, y + o, z + o).endVertex();
		renderer.pos(x + o, y, z + o).endVertex();

		// top
		renderer.pos(x, y + o, z).endVertex();
		renderer.pos(x, y + o, z + o).endVertex();
		renderer.pos(x + o, y + o, z + o).endVertex();
		renderer.pos(x + o, y + o, z).endVertex();

		// bottom
		renderer.pos(x + o, y, z + o).endVertex();
		renderer.pos(x, y, z + o).endVertex();
		renderer.pos(x, y, z).endVertex();
		renderer.pos(x + o, y, z).endVertex();
	}

	private static void renderOutlines(Set<BlockPos> positions, float thickness, float offset, float r, float g, float b, float a)
	{
		RenderHelper.enableStandardItemLighting();
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer worldRenderer = tessellator.getBuffer();
		worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
		GlStateManager.color(r, g, b, a);
		GL11.glLineWidth(thickness);

		for(BlockPos coordinate : positions)
		{
			float x = coordinate.getX();
			float y = coordinate.getY();
			float z = coordinate.getZ();

			renderSingleOutline(worldRenderer, x, y, z, offset);
		}

		tessellator.draw();
		RenderHelper.disableStandardItemLighting();
	}

	private static void renderSingleOutline(VertexBuffer renderer, float ox, float oy, float oz, float off)
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
