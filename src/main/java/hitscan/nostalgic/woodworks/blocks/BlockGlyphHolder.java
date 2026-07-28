package hitscan.nostalgic.woodworks.blocks;

import gregtech.client.utils.BloomEffectUtil;
import hitscan.nostalgic.woodworks.tileentities.TileEntityGlyphHolder;
import hitscan.nostalgic.woodworks.tileentities.TileEntityPodium;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGlyphHolder extends BlockHorizontal {
    public static final PropertyBool ACTIVE = PropertyBool.create("active");
    boolean inverted;
    boolean doubled;

    public static final AxisAlignedBB BOUNDING_NORTH = new AxisAlignedBB(0, 0, 0, 1D, 1D, 1/16D);
    public static final AxisAlignedBB BOUNDING_SOUTH = new AxisAlignedBB(0, 0, 15/16D, 1D, 1D, 1D);
    public static final AxisAlignedBB BOUNDING_EAST = new AxisAlignedBB(15/16D, 0, 0, 1D, 1D, 1D);
    public static final AxisAlignedBB BOUNDING_WEST = new AxisAlignedBB(0, 0, 0, 1/16D, 1D, 1D);

    public BlockGlyphHolder(boolean inverted, boolean doubled) {
        super(Material.GLASS);
        this.setRegistryName("glyph_holder_" + (inverted ? "inverted_" : "") + (doubled ? "4x4" : "2x2"));
        this.setTranslationKey("glyph_holder_" + (inverted ? "inverted_" : "") + (doubled ? "4x4" : "2x2"));
        this.setHarvestLevel("pickaxe", 0);
        this.setHardness(1.5F);
        this.inverted = inverted;
        this.doubled = doubled;
        this.setSoundType(SoundType.GLASS);
        this.setDefaultState(this.getBlockState().getBaseState()
                .withProperty(FACING, EnumFacing.NORTH)
                .withProperty(ACTIVE, inverted)
        );
    }

    public boolean isDouble() {
        return doubled;
    }

    @Override
    public boolean hasTileEntity() {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        TileEntityGlyphHolder glyphHolder = new TileEntityGlyphHolder();
        glyphHolder.facing = state.getValue(FACING);
        return glyphHolder;
    }


    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        if (state.getValue(FACING) == EnumFacing.NORTH) return BOUNDING_NORTH;
        if (state.getValue(FACING) == EnumFacing.SOUTH) return BOUNDING_SOUTH;
        if (state.getValue(FACING) == EnumFacing.EAST) return BOUNDING_EAST;
        return BOUNDING_WEST;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!worldIn.isRemote) {
            boolean isPowered = this.isReceivingValidPower(worldIn, pos, state.getValue(FACING));
            boolean shouldBeActive = this.inverted != isPowered;

            if (state.getValue(ACTIVE) != shouldBeActive) {
                worldIn.setBlockState(pos, state.withProperty(ACTIVE, shouldBeActive), 3);
            }
        }
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            boolean isPowered = this.isReceivingValidPower(worldIn, pos, state.getValue(FACING));
            boolean shouldBeActive = this.inverted != isPowered;

            if (state.getValue(ACTIVE) != shouldBeActive) {
                worldIn.setBlockState(pos, state.withProperty(ACTIVE, shouldBeActive), 2);
            }
        }
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        EnumFacing blockFacing = facing.getHorizontalIndex() != -1 ? facing.getOpposite() : placer.getHorizontalFacing();

        boolean isPowered = this.isReceivingValidPower(world, pos, blockFacing);
        boolean shouldBeActive = this.inverted != isPowered;

        return this.getDefaultState()
                .withProperty(FACING, blockFacing)
                .withProperty(ACTIVE, shouldBeActive);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean active = (meta & 4) != 0;
        int horizontalIndex = meta & 3;
        return this.getDefaultState()
                .withProperty(ACTIVE, active)
                .withProperty(FACING, EnumFacing.byHorizontalIndex(horizontalIndex));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int horizontalIndex = state.getValue(FACING).getHorizontalIndex();
        int activeBit = state.getValue(ACTIVE) ? 4 : 0;
        return horizontalIndex | activeBit;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this,
                FACING, ACTIVE
        );
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        if (layer == BloomEffectUtil.getBloomLayer()) {
            return state.getValue(ACTIVE);
        }
        return layer == BlockRenderLayer.CUTOUT_MIPPED || layer == BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public int getLightValue(IBlockState state) {
        return state.getValue(ACTIVE) ? 10 : 0;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public int getLightOpacity(IBlockState state) {
        return 0;
    }

    @Override
    public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return side == state.getValue(FACING);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing side) {
        return side == state.getValue(FACING) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    private boolean isReceivingValidPower(World world, BlockPos pos, EnumFacing facing) {
        EnumFacing frontFace = facing.getOpposite();

        for (EnumFacing side : EnumFacing.values()) {
            if (side != frontFace) {
                if (world.getRedstonePower(pos.offset(side), side) > 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
