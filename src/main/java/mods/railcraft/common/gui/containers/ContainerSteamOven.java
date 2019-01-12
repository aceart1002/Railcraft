/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2019
 http://railcraft.info

 This code is the property of CovertJaguar
 and may only be used with explicit written
 permission unless otherwise specified on the
 license page at http://railcraft.info/wiki/info:license.
 -----------------------------------------------------------------------------*/
package mods.railcraft.common.gui.containers;

import mods.railcraft.common.blocks.logic.SteamOvenLogic;
import mods.railcraft.common.fluids.TankManager;
import mods.railcraft.common.gui.slots.SlotSmelting;
import mods.railcraft.common.gui.widgets.FluidGaugeWidget;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.SlotFurnaceOutput;

public class ContainerSteamOven extends ContainerCrafter {

    public ContainerSteamOven(InventoryPlayer invPlayer, SteamOvenLogic logic) {
        super(logic);

        TankManager tMan = logic.getTankManager();
        if (!tMan.isEmpty()) {
            addWidget(new FluidGaugeWidget(tMan.get(0), 94, 20, 176, 0, 16, 47));
        }

        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 3; k++) {
                addSlot(new SlotSmelting(logic, i * 3 + k, 8 + k * 18, 17 + i * 18));
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 3; k++) {
                addSlot(new SlotFurnaceOutput(invPlayer.player, logic, 9 + i * 3 + k, 116 + k * 18, 17 + i * 18));
            }
        }

        addPlayerSlots(invPlayer);
    }

}
