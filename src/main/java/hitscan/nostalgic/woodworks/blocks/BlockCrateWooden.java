package hitscan.nostalgic.woodworks.blocks;

import hitscan.nostalgic.woodworks.Tags;
import hitscan.nostalgic.woodworks.gui.WoodworksGUIs;
import hitscan.nostalgic.woodworks.properties.UnlistedPropertyItemStackArray;
import hitscan.nostalgic.woodworks.tileentities.AbstractStorageBlockTileEntity;
import hitscan.nostalgic.woodworks.tileentities.TileEntityCrateWooden;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.ArrayList;
import java.util.Arrays;

public class BlockCrateWooden extends AbstractStorageBlock {
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final UnlistedPropertyItemStackArray PLANKS = new UnlistedPropertyItemStackArray("planks");

    public BlockCrateWooden() {
        super(Material.WOOD);
        this.setRegistryName("crate");
        this.setTranslationKey("crate");
        this.setHarvestLevel("axe", 0);
        this.setHardness(2.0F);
        this.setSoundType(SoundType.WOOD);
        this.setDefaultState(this.getBlockState().getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            player.openGui(Tags.MOD_ID, WoodworksGUIs.CRATE, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta));
    }

    @Override
    public AbstractStorageBlockTileEntity createStorageBlockTileEntity(World world, IBlockState state) {
        return new TileEntityCrateWooden();
    }

    @Override
    public boolean shouldBeEmptyWhenDropped(AbstractStorageBlockTileEntity te) {
        return false;
    }

    @Override
    public ItemStack serializeToItemStack(AbstractStorageBlockTileEntity storageBlockTileEntity, ItemStack baseStack) {
        if (storageBlockTileEntity instanceof TileEntityCrateWooden) {
            TileEntityCrateWooden crate = (TileEntityCrateWooden) storageBlockTileEntity;

            NBTTagCompound tag = new NBTTagCompound();
            NBTTagCompound planks = new NBTTagCompound();
            for (int i = 0; i < 6; i++) {
                NBTTagCompound plank = new NBTTagCompound();
                crate.getPlank(i).writeToNBT(plank);
                planks.setTag("Plank" + i, plank);
            }
            tag.setTag("Planks", planks);
            baseStack.setTagCompound(tag);
        }
        return baseStack;
    }

    @Override
    public void deserializeFromItemStack(AbstractStorageBlockTileEntity storageBlockTileEntity, NBTTagCompound tag) {
        if (storageBlockTileEntity instanceof TileEntityCrateWooden) {
            TileEntityCrateWooden crate = (TileEntityCrateWooden) storageBlockTileEntity;

            if (tag.hasKey("Planks")) {
                NBTTagCompound planks = tag.getCompoundTag("Planks");

                for (int i = 0; i < 6; i++) {
                    if (planks.hasKey("Plank" + i)) {
                        crate.setPlank(i, new ItemStack(planks.getCompoundTag("Plank" + i)));
                    } else {
                        crate.setPlank(i, new ItemStack(Blocks.PLANKS));
                    }
                }
            } else {
                for (int i = 0; i < 6; i++) {
                    crate.setPlank(i, new ItemStack(Blocks.PLANKS));
                }
            }
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this,
                new IProperty[] {FACING},
                new IUnlistedProperty[] {PLANKS}
        );
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (state instanceof IExtendedBlockState) {
            IExtendedBlockState ebs = (IExtendedBlockState) state;
            TileEntity te = world.getTileEntity(pos);

            if (te instanceof TileEntityCrateWooden) {
                TileEntityCrateWooden crate = (TileEntityCrateWooden) te;

                return ebs.withProperty(PLANKS, new ArrayList<>(Arrays.asList(crate.getPlanks())));
            }
        }
        return state;
    }
}
