package hitscan.nostalgic.woodworks.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemStorageBlock extends ItemBlock {
    final int slotCount;

    public ItemStorageBlock(Block block, int slotCount) {
        super(block);
        this.slotCount = slotCount;
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Inventory")) {
            return 1;
        }
        return super.getItemStackLimit(stack);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new StorageBlockInventoryProvider(stack, slotCount);
    }

    public class StorageBlockInventoryProvider implements ICapabilityProvider {
        private final ItemStack stack;
        private final int slotCount;
        private final ItemStackHandler inventory;

        public StorageBlockInventoryProvider(ItemStack stack, int slotCount) {
            this.stack = stack;
            this.slotCount = slotCount;
            this.inventory = new ItemStackHandler(slotCount) {
                @Override
                protected void onContentsChanged(int slot) {
                    super.onContentsChanged(slot);
                    NBTTagCompound tag = stack.getTagCompound();
                    if (tag == null) {
                        tag = new NBTTagCompound();
                        stack.setTagCompound(tag);
                    }
                    tag.setTag("Inventory", serializeNBT());
                }
            };

            if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Inventory")) {
                inventory.deserializeNBT(stack.getTagCompound().getCompoundTag("Inventory"));
            }
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
            }
            return null;
        }
    }
}
