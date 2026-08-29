package hitscan.nostalgic.woodworks.tileentities;

import hitscan.nostalgic.woodworks.items.ItemStorageBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public abstract class AbstractStorageBlockTileEntity extends TileEntity {
    ItemStackHandler stacks;
    boolean brokenByCreativePlayer = false;
    boolean harvested = false;

    public AbstractStorageBlockTileEntity() {
        if (shouldInitializeStacksOnInstantiation()) {
            stacks = createItemStackHandler();
        }
    }

    public boolean shouldInitializeStacksOnInstantiation() {
        return true;
    }

    public abstract int stackCount();

    public int maxCountPerStack() {
        return 64;
    }

    ItemStackHandler createItemStackHandler() {
        return new ItemStackHandler(stackCount()) {
            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }

            @Override
            public int getSlotLimit(int slot) {
                return maxCountPerStack();
            }

            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return !(stack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null) ||
                        stack.getItem() instanceof IInventory ||
                        stack.getItem() instanceof ItemStorageBlock ||
                        stack.getItem() instanceof ItemShulkerBox
                );
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if (!isItemValid(slot, stack)) {
                    return stack;
                }
                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    public void markBrokenByCreativePlayer() {
        this.brokenByCreativePlayer = true;
    }

    public void markHarvested() { this.harvested = true; }

    public boolean isBrokenByCreativePlayer() {
        return brokenByCreativePlayer;
    }

    public boolean isHarvested() { return harvested; }

    public ItemStackHandler getStackHandler() {
        return stacks;
    }

    public boolean containsItems() {
        for (int i = 0; i < this.getStackHandler().getSlots(); i++) {
            if (!this.getStackHandler().getStackInSlot(i).isEmpty()) {
                return true;
            }
        }

        return false;
    }

    public boolean markDirtyOnRead() {
        return true;
    }

    public void deserializeFromNBT(NBTTagCompound compound) {

    }

    public void serializeNBT(NBTTagCompound compound) {

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("Inventory", this.getStackHandler().serializeNBT());
        this.serializeNBT(compound);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("Inventory")) this.getStackHandler().deserializeNBT(compound.getCompoundTag("Inventory"));
        deserializeFromNBT(compound);
        if (markDirtyOnRead()) markDirty();
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound updateTag = new NBTTagCompound();
        this.writeToNBT(updateTag);
        return updateTag;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
        if (this.world != null && this.world.isRemote) {
            this.world.markBlockRangeForRenderUpdate(this.pos, this.pos);
        }
    }

    public void markDirtyAndNotify() {
        this.markDirty();
        if (this.world != null) {
            IBlockState state = this.world.getBlockState(this.pos);
            this.world.notifyBlockUpdate(this.pos, state, state, 3);
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.stacks);
        }
        return super.getCapability(capability, facing);
    }
}
