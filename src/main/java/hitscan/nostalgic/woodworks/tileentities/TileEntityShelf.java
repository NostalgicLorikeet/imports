package hitscan.nostalgic.woodworks.tileentities;

import hitscan.nostalgic.woodworks.blocks.BlockShelf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class TileEntityShelf extends TileEntity {
    private String id = "minecraft:planks";
    private int damage = 0;
    public boolean brokenByCreativePlayer = false;
    private boolean canPlaceItemStacksIn = false;
    public ItemStack[] stacks;
    //setting it here too because its prolly cheaper than to constantly check the blockstate in the tesr
    public EnumFacing rotation;

    public TileEntityShelf() {}

    private final IItemHandler itemHandler = new IItemHandler() {
        @Override
        public int getSlots() {
            return 3;
        }

        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot) {
            return slot < 3 && slot > -1 ? stacks[slot] : ItemStack.EMPTY;
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
            return canPlaceItemStacksIn;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && canPlaceItemStacksIn) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemHandler);
        }
        return super.getCapability(capability, facing);
    }

    public void setId(String id) {
        this.id = id;
        markDirtyAndNotify();
    }

    public void setDamage(int damage) {
        this.damage = damage;
        markDirtyAndNotify();
    }

    public String getId() {
        return id;
    }

    public int getDamage() {
        return damage;
    }

    public void setCanPlaceItemStacksIn() {
        stacks = new ItemStack[3];
        Arrays.fill(stacks, ItemStack.EMPTY);
        canPlaceItemStacksIn = true;
    }

    public boolean canPlaceItemStacksIn() {
        return canPlaceItemStacksIn;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagCompound texture = new NBTTagCompound();
        texture.setString("id", id);
        texture.setInteger("Damage", damage);
        compound.setTag("Texture", texture);

        if (this.canPlaceItemStacksIn) {
            NBTTagCompound stacksList = new NBTTagCompound();
            for (int i = 0; i < 3; i++) {
                if (stacks[i] != null) {
                    NBTTagCompound stackNBT = new NBTTagCompound();
                    stacks[i].writeToNBT(stackNBT);
                    stacksList.setTag(Integer.toString(i), stackNBT);
                }
            }
            compound.setTag("Stacks", stacksList);
        }

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("Texture")) {
            NBTTagCompound texture = compound.getCompoundTag("Texture");
            if (texture.hasKey("id")) id = texture.getString("id");
            if (texture.hasKey("Damage")) damage = texture.getInteger("Damage");
        }
        if (compound.hasKey("Stacks")) {
            setCanPlaceItemStacksIn();
            NBTTagCompound stacksList = compound.getCompoundTag("Stacks");
            for (int i = 0; i < 3; i++) {
                if (stacksList.hasKey(Integer.toString(i))) {
                    stacks[i] = new ItemStack(stacksList.getCompoundTag(Integer.toString(i)));
                }
            }
        }
        if (compound.hasKey("Facing")) {
            try {
                rotation = EnumFacing.byIndex(compound.getInteger("Facing"));
            } catch (Exception ignored) {}
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
        updateTag.setInteger("Facing", world.getBlockState(pos).getValue(BlockShelf.FACING).getIndex());
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
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(pos.add(-0.5, -0.5, -0.5), pos.add(1, 1, 1));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        double maxBlocks = Minecraft.getMinecraft().gameSettings.renderDistanceChunks * 3.0;
        return maxBlocks * maxBlocks;
    }
}
