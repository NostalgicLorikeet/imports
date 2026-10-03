package hitscan.nostalgic.woodworks.gui.containers.server;

import hitscan.nostalgic.woodworks.entity.EntityStrawStatue;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ContainerStatue extends Container {
    private final EntityStrawStatue statue;
    private final InventoryPlayer inventoryPlayer;
    int slotCount = 6;

    public ContainerStatue(InventoryPlayer inventoryPlayer, EntityStrawStatue statue) {
        this.inventoryPlayer = inventoryPlayer;
        this.statue = statue;

        for (int k = 0; k < 4; ++k) {
            this.addSlotToContainer(new Slot(null, k, 8 + k * 18, 18) {
                final EntityEquipmentSlot equipmentSlot = EntityEquipmentSlot.values()[this.getSlotIndex()+2];

                @Override
                public boolean isItemValid(ItemStack stack) {
                    return stack.getItem() instanceof ItemArmor &&
                            ((ItemArmor) stack.getItem()).armorType == equipmentSlot;
                }

                @Override
                public int getSlotStackLimit() {
                    return 1;
                }

                @Override
                public ItemStack getStack() {
                    return statue.getItemStackFromSlot(equipmentSlot);
                }

                @Override
                public void putStack(ItemStack stack) {
                    statue.setItemStackToSlot(equipmentSlot, stack);
                    this.onSlotChanged();
                }

                @Override
                public ItemStack decrStackSize(int amount) {
                    ItemStack itemstack = this.getStack();
                    return itemstack.isEmpty() ? ItemStack.EMPTY : itemstack.splitStack(amount);
                }

                //this has to be overridden with an empty method or else it crashes cuz this.inventory is null
                @Override
                public void onSlotChanged() {}
            });
        }

        for (int s = 0; s < 2; ++s) {
            this.addSlotToContainer(new Slot(null, s, 8 + s * 18, 0) {
                final EntityEquipmentSlot handSlot = EntityEquipmentSlot.values()[this.getSlotIndex()];

                @Override
                public int getSlotStackLimit() {
                    return 1;
                }

                @Override
                public ItemStack getStack() {
                    return statue.getItemStackFromSlot(handSlot);
                }

                @Override
                public void putStack(ItemStack stack) {
                    statue.setItemStackToSlot(handSlot, stack);
                    this.onSlotChanged();
                }

                @Override
                public ItemStack decrStackSize(int amount) {
                    ItemStack itemstack = this.getStack();
                    return itemstack.isEmpty() ? ItemStack.EMPTY : itemstack.splitStack(amount);
                }

                @Override
                public void onSlotChanged() {}
            });
        }

        for (int l = 0; l < 3; ++l)
        {
            for (int j1 = 0; j1 < 9; ++j1)
            {
                this.addSlotToContainer(new Slot(inventoryPlayer, j1 + l * 9 + 9, 8 + j1 * 18, 49 + l * 18));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1)
        {
            this.addSlotToContainer(new Slot(inventoryPlayer, i1, 8 + i1 * 18, 107));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            int totalSlots = this.inventorySlots.size();

            if (index < slotCount) {
                if (!this.mergeItemStack(itemstack1, slotCount, totalSlots, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.mergeItemStack(itemstack1, 0, slotCount, false)) {
                    return ItemStack.EMPTY;
                }
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

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        statue.isGUIOpen = false;
    }
}
