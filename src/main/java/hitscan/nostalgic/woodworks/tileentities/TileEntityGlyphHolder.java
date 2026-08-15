package hitscan.nostalgic.woodworks.tileentities;

import hitscan.nostalgic.woodworks.glyphs.EnumGlyphColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Arrays;

public class TileEntityGlyphHolder extends TileEntity {
    public boolean brokenByCreativePlayer = false;

    public int dim = 1;
    public ItemStack[] glyphs;

    public TileEntityGlyphHolder() {}

    public int[][] serializeGlyphsAndColorsAsIntegerArray() {
        int[][] glyphsAndColors = new int[16][3];

        for (int i = 0; i < glyphs.length; i++) {
            if (!glyphs[i].isEmpty()) {
                glyphsAndColors[i][0] = EnumGlyphColor.values()[glyphs[i].getItemDamage()].colorInt;
                glyphsAndColors[i][1] = EnumGlyphColor.values()[glyphs[i].getItemDamage()].colorIntTransparent;
                glyphsAndColors[i][2] = 0;
                if (glyphs[i].hasTagCompound()) {
                    NBTTagCompound tag = glyphs[i].getTagCompound();
                    if (tag.hasKey("Character")) {
                        glyphsAndColors[i][2] = tag.getInteger("Character");
                    }
                }
            } else {
                glyphsAndColors[i][2] = -1;
            }
        }

        return glyphsAndColors;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("Dimensions", dim);
        NBTTagCompound stacksList = new NBTTagCompound();
        for (int i = 0; i < dim*dim; i++) {
            if (glyphs[i] != null) {
                NBTTagCompound stackNBT = new NBTTagCompound();
                glyphs[i].writeToNBT(stackNBT);
                stacksList.setTag(Integer.toString(i), stackNBT);
            }
        }
        compound.setTag("Stacks", stacksList);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("Dimensions")) {
            dim = compound.getInteger("Dimensions");
            glyphs = new ItemStack[dim * dim];
            Arrays.fill(glyphs, ItemStack.EMPTY);
            if (compound.hasKey("Stacks")) {
                NBTTagCompound stacksList = compound.getCompoundTag("Stacks");
                for (int i = 0; i < dim * dim; i++) {
                    if (stacksList.hasKey(Integer.toString(i))) {
                        glyphs[i] = new ItemStack(stacksList.getCompoundTag(Integer.toString(i)));
                    }
                }
            }
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
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }
}
