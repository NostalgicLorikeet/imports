package hitscan.nostalgic.woodworks.tileentities;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class TileEntityPodium extends TileEntity {
    public String id_wood = "minecraft:planks";
    public int damage_wood = 0;
    public String id_stone = "minecraft:stone";
    public int damage_stone = 0;

    public ItemStack stack = ItemStack.EMPTY;

    public boolean single = false;
    public boolean brokenByCreativePlayer = false;
    public EnumFacing facing = EnumFacing.NORTH;

    public TileEntityPodium() {}

    private final IItemHandler itemHandler = new IItemHandler() {
        @Override
        public int getSlots() {
            return 1;
        }

        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot) {
            return stack;
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            return stack;
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 0;
        }
    };

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
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemHandler);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagCompound podium = new NBTTagCompound();
        NBTTagCompound wood = new NBTTagCompound();
        wood.setString("ID", id_wood);
        wood.setInteger("Damage", damage_wood);
        NBTTagCompound stone = new NBTTagCompound();
        stone.setString("ID", id_stone);
        stone.setInteger("Damage", damage_stone);
        podium.setTag("Wood", wood);
        podium.setTag("Stone", stone);
        podium.setBoolean("Single", single);
        NBTTagCompound stackTag = new NBTTagCompound();
        stack.writeToNBT(stackTag);
        podium.setTag("Stack", stackTag);
        podium.setInteger("Facing", facing.getIndex());
        compound.setTag("Podium", podium);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("Podium")) {
            NBTTagCompound podium = compound.getCompoundTag("Podium");

            single = podium.getBoolean("Single");
            stack = new ItemStack(podium.getCompoundTag("Stack"));

            NBTTagCompound wood = podium.getCompoundTag("Wood");
            NBTTagCompound stone = podium.getCompoundTag("Stone");

            id_wood = wood.getString("ID");
            damage_wood = wood.getInteger("Damage");
            id_stone = stone.getString("ID");
            damage_stone = stone.getInteger("Damage");

            facing = EnumFacing.byIndex(podium.getInteger("Facing"));
        }
        markDirty();
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
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        double maxBlocks = Minecraft.getMinecraft().gameSettings.renderDistanceChunks * 2.0;
        return maxBlocks * maxBlocks;
    }
}
