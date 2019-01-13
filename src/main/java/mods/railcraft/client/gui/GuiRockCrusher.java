/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2019
 http://railcraft.info

 This code is the property of CovertJaguar
 and may only be used with explicit written
 permission unless otherwise specified on the
 license page at http://railcraft.info/wiki/info:license.
 -----------------------------------------------------------------------------*/
package mods.railcraft.client.gui;

import mods.railcraft.common.blocks.logic.RockCrusherLogic;
import mods.railcraft.common.core.RailcraftConstants;
import mods.railcraft.common.gui.containers.ContainerRockCrusher;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiRockCrusher extends GuiTitled {

    private RockCrusherLogic crusher;

    public GuiRockCrusher(InventoryPlayer inventoryplayer, RockCrusherLogic logic) {
        super(logic, new ContainerRockCrusher(inventoryplayer, logic), RailcraftConstants.GUI_TEXTURE_FOLDER + "gui_crusher.png");
        this.crusher = logic;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
//        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, (ySize - 96) + 2, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        super.drawGuiContainerBackgroundLayer(f, i, j);
        int w = (width - xSize) / 2;
        int h = (height - ySize) / 2;
        if (crusher.getProgress() > 0) {
            int cookProgress = (int) (crusher.getProgressPercent() * 29);
            drawTexturedModalRect(w + 73, h + 20, 176, 0, cookProgress + 1, 38);
        }
    }
}
