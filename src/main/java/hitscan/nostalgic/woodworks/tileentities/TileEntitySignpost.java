package hitscan.nostalgic.woodworks.tileentities;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntitySignpost extends TileEntity {
    public boolean brokenByCreativePlayer = false;
    IBlockState fenceDefaultState = Blocks.OAK_FENCE.getDefaultState();

    public TileEntitySignpost() {

    }

    public void setFenceDefaultState(IBlockState state) {
        fenceDefaultState = state;
    }

    public IBlockState getFenceDefaultState() {
        return fenceDefaultState;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagCompound state = new NBTTagCompound();
        NBTUtil.writeBlockState(state, fenceDefaultState);
        compound.setTag("state", state);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("state")) {
            fenceDefaultState = NBTUtil.readBlockState(compound.getCompoundTag("state"));
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
}
