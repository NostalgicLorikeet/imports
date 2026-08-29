package hitscan.nostalgic.woodworks.blocks;

import hitscan.nostalgic.woodworks.tileentities.AbstractStorageBlockTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractStorageBlock extends Block  {
    public AbstractStorageBlock(Material material) {
        super(material);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return this.createStorageBlockTileEntity(world, state);
    }

    public abstract AbstractStorageBlockTileEntity createStorageBlockTileEntity(World world, IBlockState state);

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (player.capabilities.isCreativeMode) {
            TileEntity te = world.getTileEntity(pos);
            if (te != null) {
                if (te instanceof AbstractStorageBlockTileEntity) {
                    AbstractStorageBlockTileEntity storageBlockTileEntity = (AbstractStorageBlockTileEntity) te;
                    storageBlockTileEntity.markBrokenByCreativePlayer();
                }
            }
        }
        super.onBlockHarvested(world, pos, state, player);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        return new ArrayList<>();
    }

    public ItemStack serializeToItemStack(AbstractStorageBlockTileEntity storageBlockTileEntity, ItemStack baseStack) {
        return baseStack;
    }

    public boolean shouldBeEmptyWhenDropped(AbstractStorageBlockTileEntity storageBlockTileEntity) {
        return true;
    }

    public void fillWithItems(AbstractStorageBlockTileEntity storageBlockTileEntity, ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound() == null ? new NBTTagCompound() : stack.getTagCompound();
        if (storageBlockTileEntity.containsItems()) tag.setTag("Inventory", storageBlockTileEntity.getStackHandler().serializeNBT());
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof AbstractStorageBlockTileEntity) {
            AbstractStorageBlockTileEntity storageBlockTileEntity = (AbstractStorageBlockTileEntity) te;
            ItemStack blockStack = this.serializeToItemStack(storageBlockTileEntity, new ItemStack(this));

            if (!storageBlockTileEntity.containsItems()) {
                if (!storageBlockTileEntity.isBrokenByCreativePlayer()) spawnAsEntity(world, pos, blockStack);
            } else {
                if (shouldBeEmptyWhenDropped(storageBlockTileEntity)) {
                    if (!storageBlockTileEntity.isBrokenByCreativePlayer()) spawnAsEntity(world, pos, blockStack);

                    for (int i = 0; i < storageBlockTileEntity.getStackHandler().getSlots(); i++) {
                        if (storageBlockTileEntity.getStackHandler().getStackInSlot(i) != ItemStack.EMPTY) {
                            spawnAsEntity(world, pos, storageBlockTileEntity.getStackHandler().getStackInSlot(i));
                        }
                    }
                } else {
                    fillWithItems(storageBlockTileEntity, blockStack);
                    spawnAsEntity(world, pos, blockStack);
                }
            }
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        ItemStack stack = super.getItem(worldIn, pos, state);

        TileEntity te = worldIn.getTileEntity(pos);
        if (te != null) {
            if (te instanceof AbstractStorageBlockTileEntity) {
                return serializeToItemStack((AbstractStorageBlockTileEntity) te, stack);
            }
        }
        return stack;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity te = world.getTileEntity(pos);
        if (te != null) {
            AbstractStorageBlockTileEntity storageBlockTileEntity = (AbstractStorageBlockTileEntity) te;
            NBTTagCompound tag = stack.getTagCompound();

            if (tag != null) {
                deserializeFromItemStack(storageBlockTileEntity, tag);
                if (tag.hasKey("Inventory")) deserializeInventory(storageBlockTileEntity, tag);
            }

            storageBlockTileEntity.markDirtyAndNotify();
        }
    }

    public void deserializeFromItemStack(AbstractStorageBlockTileEntity storageBlockTileEntity, NBTTagCompound tag) {
        //NEVER deserialize inventory from in here, it is done separately
    }

    public void deserializeInventory(AbstractStorageBlockTileEntity storageBlockTileEntity, NBTTagCompound tag) {
        storageBlockTileEntity.getStackHandler().deserializeNBT(tag.getCompoundTag("Inventory"));
    }
}
