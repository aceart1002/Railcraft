/* 
 * Copyright (c) CovertJaguar, 2014 http://railcraft.info
 * 
 * This code is the property of CovertJaguar
 * and may only be used with explicit written
 * permission unless otherwise specified on the
 * license page at http://railcraft.info/wiki/info:license.
 */
package mods.railcraft.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.Teleporter;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import mods.railcraft.api.signals.SignalAspect;
import mods.railcraft.client.render.RenderFakeBlock.RenderInfo;
import mods.railcraft.common.blocks.RailcraftBlocks;
import mods.railcraft.common.blocks.signals.BlockSignalRailcraft;
import mods.railcraft.common.blocks.signals.TileBoxBase;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

import static net.minecraftforge.common.util.ForgeDirection.*;

public class RenderSignalBox extends RenderTESRSignals implements ICombinedRenderer {

    private static final RenderInfo info = new RenderInfo();
    private final IIconProvider iconProvider;

    public RenderSignalBox(IIconProvider iconProvider) {
        info.texture = new IIcon[6];
        info.template = RailcraftBlocks.getBlockSignal();
		tesrInfo.texture = new IIcon[6];
		tesrInfo.template = RailcraftBlocks.getBlockSignal();
        this.iconProvider = iconProvider;
    }

    @Override
    public void renderBlock(RenderBlocks renderblocks, IBlockAccess iBlockAccess, int x, int y, int z, Block block) {
        TileBoxBase tile = (TileBoxBase) iBlockAccess.getTileEntity(x, y, z);
        float pix = RenderTools.PIXEL;

        if (renderblocks.hasOverrideBlockTexture())
            info.override = renderblocks.overrideBlockTexture;
        else
            info.override = null;

        info.texture[0] = BlockSignalRailcraft.texturesBox[2];
        info.texture[1] = iconProvider.getIcon();
        info.texture[2] = BlockSignalRailcraft.texturesBox[0];
        info.texture[3] = BlockSignalRailcraft.texturesBox[0];
        info.texture[4] = BlockSignalRailcraft.texturesBox[0];
        info.texture[5] = BlockSignalRailcraft.texturesBox[0];

//        info.setBlockBounds(pix, 13 * pix, pix, 15 * pix, 15 * pix, 15 * pix);
//        RenderFakeBlock.renderBlock(info, iBlockAccess, x, y, z, true, false);

        boolean eastWest = false;
        boolean northSouth = false;
        if (tile.isConnected(EAST) || tile.isConnected(WEST))
            eastWest = true;
        if (tile.isConnected(NORTH) || tile.isConnected(SOUTH))
            northSouth = true;
        boolean side2 = tile.isConnected(NORTH);
        boolean side3 = tile.isConnected(SOUTH);
        boolean side4 = tile.isConnected(WEST);
        boolean side5 = tile.isConnected(EAST);
        if (!eastWest && !northSouth)
            eastWest = true;


        if (side2)
            info.texture[2] = BlockSignalRailcraft.texturesBox[1];
        if (side3)
            info.texture[3] = BlockSignalRailcraft.texturesBox[1];
        if (side4)
            info.texture[4] = BlockSignalRailcraft.texturesBox[1];
        if (side5)
            info.texture[5] = BlockSignalRailcraft.texturesBox[1];
        info.setBlockBounds(2 * pix, 0, 2 * pix, 14 * pix, 15 * pix, 14 * pix);
        RenderFakeBlock.renderBlock(info, iBlockAccess, x, y, z, true, false);

        info.renderSide[0] = false;
        info.renderSide[1] = false;

      /*  // Aspect
        for (int side = 2; side < 6; side++) {
            SignalAspect aspect = tile.getBoxSignalAspect(ForgeDirection.getOrientation(side));
            if (!aspect.isLit())
                aspect = SignalAspect.OFF;
            IIcon lamp = BlockSignalRailcraft.texturesLampBox[aspect.getTextureIndex()];
            info.texture[2] = lamp;
            info.texture[3] = lamp;
            info.texture[4] = lamp;
            info.texture[5] = lamp;
            info.renderSide[2] = side == 2 && !side2;
            info.renderSide[3] = side == 3 && !side3;
            info.renderSide[4] = side == 4 && !side4;
            info.renderSide[5] = side == 5 && !side5;
            if (!renderblocks.hasOverrideBlockTexture())
                info.brightness = aspect.getTextureBrightness();
            RenderFakeBlock.renderBlock(info, iBlockAccess, x, y, z, (info.brightness < 0), false);
        }*/
        info.brightness = -1;
        info.setRenderAllSides();

        // Cap
        if (!iBlockAccess.isAirBlock(x, y + 1, z)) {
            info.texture[1] = BlockSignalRailcraft.texturesBox[3];
            info.texture[2] = BlockSignalRailcraft.texturesBox[0];
            info.texture[3] = BlockSignalRailcraft.texturesBox[0];
            info.texture[4] = BlockSignalRailcraft.texturesBox[0];
            info.texture[5] = BlockSignalRailcraft.texturesBox[0];
            info.setBlockBounds(5 * pix, 15 * pix, 5 * pix, 11 * pix, 16 * pix, 11 * pix);
            RenderFakeBlock.renderBlock(info, iBlockAccess, x, y, z, true, false);
        }

        // Connections
        info.texture[0] = BlockSignalRailcraft.texturesBox[4];
        info.texture[1] = BlockSignalRailcraft.texturesBox[4];
        info.texture[2] = BlockSignalRailcraft.texturesBox[5];
        info.texture[3] = BlockSignalRailcraft.texturesBox[5];
        info.texture[4] = BlockSignalRailcraft.texturesBox[5];
        info.texture[5] = BlockSignalRailcraft.texturesBox[5];
        float min = 7 * pix;
        float max = 9 * pix;
        float minY = 10 * pix;
        float maxY = 12 * pix;
        float minXEW = side4 ? 0.0F : min;
        float maxXEW = side5 ? 1.0F : max;
        float minZNS = side2 ? 0.0F : min;
        float maxZNS = side3 ? 1.0F : max;
        if (eastWest) {
            info.setBlockBounds(minXEW, minY, min, maxXEW, maxY, max);
            RenderFakeBlock.renderBlock(info, iBlockAccess, x, y, z, true, false);
        }
        if (northSouth) {
            info.setBlockBounds(min, minY, minZNS, max, maxY, maxZNS);
            RenderFakeBlock.renderBlock(info, iBlockAccess, x, y, z, true, false);
        }
        minY = 5 * pix;
        maxY = 7 * pix;
        if (eastWest) {
            info.setBlockBounds(minXEW, minY, min, maxXEW, maxY, max);
            RenderFakeBlock.renderBlock(info, iBlockAccess, x, y, z, true, false);
        }
        if (northSouth) {
            info.setBlockBounds(min, minY, minZNS, max, maxY, maxZNS);
            RenderFakeBlock.renderBlock(info, iBlockAccess, x, y, z, true, false);
        }
    }

    @Override
    public void renderItem(RenderBlocks renderblocks, ItemStack item, ItemRenderType renderType) {
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        info.override = null;
        float pix = RenderTools.PIXEL;
        info.setBlockBounds(2 * pix, 0, 2 * pix, 14 * pix, 15 * pix, 14 * pix);
        info.texture[0] = BlockSignalRailcraft.texturesBox[2];
        info.texture[1] = iconProvider.getIcon();
        info.texture[2] = BlockSignalRailcraft.texturesBox[0];
        info.texture[3] = BlockSignalRailcraft.texturesBox[0];
        info.texture[4] = BlockSignalRailcraft.texturesBox[0];
        info.texture[5] = BlockSignalRailcraft.texturesBox[0];
        RenderFakeBlock.renderBlockOnInventory(renderblocks, info, 1);
        int texture = SignalAspect.RED.getTextureIndex();
        info.renderSide[0] = false;
        info.renderSide[1] = false;
        info.texture[2] = BlockSignalRailcraft.texturesLampBox[texture];
        info.texture[3] = BlockSignalRailcraft.texturesLampBox[texture];
        info.texture[4] = BlockSignalRailcraft.texturesLampBox[texture];
        info.texture[5] = BlockSignalRailcraft.texturesLampBox[texture];
        RenderFakeBlock.renderBlockOnInventory(renderblocks, info, 1);
        info.setRenderAllSides();

        GL11.glPopAttrib();
    }

	private static final RenderInfo tesrInfo = new RenderInfo();

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float f) {
        super.renderTileEntityAt(te, x, y, z, f);
        if(!(te instanceof TileBoxBase)){
            return;
        }
		RenderInfo info = tesrInfo;
        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        TileBoxBase tile = (TileBoxBase) te;
        Tessellator tessellator = Tessellator.instance;

        float pix = RenderTools.PIXEL;

        final float zOffset = 1/16f;
		info.setBlockBounds(2 * pix - zOffset, 0 - zOffset, 2 * pix - zOffset, 14 * pix - zOffset, 15 * pix - zOffset, 14 * pix - zOffset);

		info.renderSide[0] = false;
		info.renderSide[1] = false;
		info.renderSide[2] = !tile.isConnected(NORTH);
		info.renderSide[3] = !tile.isConnected(SOUTH);
		info.renderSide[4] = !tile.isConnected(WEST);
		info.renderSide[5] = !tile.isConnected(EAST);

		GL11.glTranslated(x,y,z);
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
		tessellator.startDrawingQuads();

        // Aspect
        for (int side = 2; side < 6; side++) {
            SignalAspect aspect = tile.getBoxSignalAspect(ForgeDirection.getOrientation(side));
            if (!aspect.isLit())
                aspect = SignalAspect.OFF;
            info.texture[side] = BlockSignalRailcraft.texturesLampBox[aspect.getTextureIndex()];
            info.brightness = aspect.getTextureBrightness();
            //RenderFakeBlock.renderBlock(info, tile.getWorld(), x,y,z, (info.brightness < 0), true);
        }
		if (info.brightness < 0){
			float light = 0;
			float lightBottom = 0.5F;
			if (info.light < 0) {
				//                light = info.template.getBlockBrightness(blockAccess, (int) lightX, (int) lightY, (int) lightZ);
				//                light = light + ((1.0f - light) * 0.4f);
				light = 1;
			} else
				light = info.light;
			int br = 0;
			if (info.brightness < 0)
				br = info.template.getMixedBrightnessForBlock(tile.getWorld(), tile.xCoord, tile.yCoord, tile.zCoord);
			else
				br = info.brightness;
			tessellator.setBrightness(br);
			tessellator.setColorOpaque_F(lightBottom * light, lightBottom * light, lightBottom * light);
		} else {
			tessellator.setBrightness(info.brightness);
		}
		double xS = info.minX, xE = info.maxX,
			yS = info.minY, yE = info.maxY,
			zS = info.minZ, zE = info.maxZ;
		if(info.renderSide[2]) {

			/*drawQuad(info.minX, info.minY, info.minZ, info.maxX, info.maxY, info.maxZ,
				info.texture[2].getInterpolatedU(0), info.texture[2].getInterpolatedV(0),info.texture[2].getInterpolatedU(16f), info.texture[2].getInterpolatedV(16f));*/
			tessellator.addVertex(xS, yS, zS);
			tessellator.addVertex(xS, yE, zS);
			tessellator.addVertex(xE, yE, zS);
			tessellator.addVertex(xE, yS, zS);
			/*tessellator.addVertexWithUV(xS, yS, zS, info.texture[2].getInterpolatedU(0),info.texture[2].getInterpolatedV(0));
			tessellator.addVertexWithUV(xS, yE, zS,info.texture[2].getInterpolatedU(0),info.texture[2].getInterpolatedV(0));
			tessellator.addVertexWithUV(xE, yE, zS,info.texture[2].getInterpolatedU(0),info.texture[2].getInterpolatedV(0));
			tessellator.addVertexWithUV(xE, yS, zS,info.texture[2].getInterpolatedU(0),info.texture[2].getInterpolatedV(0));*/
		}
		if(info.renderSide[3]) {
			tessellator.addVertex(xS, yS, zE);
			tessellator.addVertex(xE, yS, zE);
			tessellator.addVertex(xE, yE, zE);
			tessellator.addVertex(xS, yE, zE);
		}
		if(info.renderSide[4]) {
			tessellator.addVertex(xS, yS, zS);
			tessellator.addVertex(xS, yS, zE);
			tessellator.addVertex(xS, yE, zE);
			tessellator.addVertex(xS, yE, zS);
		}
		if(info.renderSide[5]){
			tessellator.addVertex(xE, yS, zS);
			tessellator.addVertex(xE, yE, zS);
			tessellator.addVertex(xE, yE, zE);
			tessellator.addVertex(xE, yS, zE);
		}
		tessellator.draw();
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }

    /*private void drawQuad(){
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertex(xS, yS, zS);
        tessellator.addVertex(xS, yE, zS);
        tessellator.addVertex(xE, yE, zS);
        tessellator.addVertex(xE, yS, zS);

        tessellator.addVertex(xS, yS, zE);
        tessellator.addVertex(xE, yS, zE);
        tessellator.addVertex(xE, yE, zE);
        tessellator.addVertex(xS, yE, zE);

        tessellator.addVertex(xS, yS, zS);
        tessellator.addVertex(xS, yS, zE);
        tessellator.addVertex(xS, yE, zE);
        tessellator.addVertex(xS, yE, zS);

        tessellator.addVertex(xE, yS, zS);
        tessellator.addVertex(xE, yE, zS);
        tessellator.addVertex(xE, yE, zE);
        tessellator.addVertex(xE, yS, zE);
        tessellator.draw();
    }*/
}
