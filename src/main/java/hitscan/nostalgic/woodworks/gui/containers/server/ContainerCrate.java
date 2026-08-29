package hitscan.nostalgic.woodworks.gui.containers.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerCrate extends Container {
    IItemHandler handler;
    int slotCount = 9;

    public ContainerCrate (EntityPlayer player, IItemHandler handler) {
        this.handler = handler;

        for (int k = 0; k < slotCount; ++k) {
            this.addSlotToContainer(new SlotItemHandler(handler, k, 8 + k * 18, 18));
        }

        for (int l = 0; l < 3; ++l)
        {
            for (int j1 = 0; j1 < 9; ++j1)
            {
                this.addSlotToContainer(new Slot(player.inventory, j1 + l * 9 + 9, 8 + j1 * 18, 48 + l * 18));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1)
        {
            this.addSlotToContainer(new Slot(player.inventory, i1, 8 + i1 * 18, 106));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < slotCount) {
                if (!this.mergeItemStack(itemstack1, slotCount, slotCount+36, true)) return ItemStack.EMPTY;
            }else {
                if (!handler.isItemValid(slot.getSlotIndex(), itemstack1)) {
                    return ItemStack.EMPTY;
                }

                if (!this.mergeItemStack(itemstack1, 0, slotCount, false)) return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }
}
