package hitscan.nostalgic.woodworks.blocks;

import gregtech.client.utils.BloomEffectUtil;
import hitscan.nostalgic.woodworks.items.ItemNeonGlyph;
import hitscan.nostalgic.woodworks.properties.UnlistedPropertyColorGlyphPairs;
import hitscan.nostalgic.woodworks.tileentities.TileEntityGlyphHolder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;

public class BlockGlyphHolder extends BlockHorizontal {
    public static final PropertyBool ACTIVE = PropertyBool.create("active");
    public static final UnlistedPropertyColorGlyphPairs COLOR_GLYPH_PAIRS = new UnlistedPropertyColorGlyphPairs("color_glyph_pairs");

    private boolean inverted;
    private int dim;

    public static final AxisAlignedBB BOUNDING_NORTH = new AxisAlignedBB(0, 0, 0, 1D, 1D, 1/16D);
    public static final AxisAlignedBB BOUNDING_SOUTH = new AxisAlignedBB(0, 0, 15/16D, 1D, 1D, 1D);
    public static final AxisAlignedBB BOUNDING_EAST = new AxisAlignedBB(15/16D, 0, 0, 1D, 1D, 1D);
    public static final AxisAlignedBB BOUNDING_WEST = new AxisAlignedBB(0, 0, 0, 1/16D, 1D, 1D);

    public BlockGlyphHolder(boolean inverted, int dim) {
        super(Material.GLASS);
        final String name = "glyph_holder_" + (inverted ? "inverted_" : "") + (dim + "x" + dim);
        this.setRegistryName(name);
        this.setTranslationKey(name);
        this.setHarvestLevel("pickaxe", 0);
        this.setHardness(1.5F);
        this.inverted = inverted;
        this.dim = dim;
        this.setSoundType(SoundType.GLASS);
        this.setDefaultState(this.getBlockState().getBaseState()
                .withProperty(FACING, EnumFacing.NORTH)
                .withProperty(ACTIVE, inverted)
        );
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!player.getHeldItemMainhand().isEmpty()) {
            if (!(player.getHeldItemMainhand().getItem() instanceof ItemNeonGlyph)) return false;
        }
        if (facing == state.getValue(FACING).getOpposite()) {
            float targetYnormalized = 1-hitY;
            float targetXnormalized;

            if (facing == EnumFacing.SOUTH) {
                targetXnormalized = hitX;
            } else if (facing == EnumFacing.NORTH) {
                targetXnormalized = 1-hitX;
            } else if (facing == EnumFacing.WEST) {
                targetXnormalized = hitZ;
            } else {
                targetXnormalized = 1-hitZ;
            }

            int targetX1x = 0;
            int targetY1x = 0;
            int targetX2x = targetXnormalized >= 0.5 ? 1 : 0;
            int targetY2x = targetYnormalized >= 0.5 ? 1 : 0;
            int targetX4x = Math.min((int) (targetXnormalized/0.25), 3);
            int targetY4x = Math.min((int) (targetYnormalized/0.25), 3);

            int targetX, targetY;

            if (dim == 1) {
                targetX = targetX1x;
                targetY = targetY1x;
            } else if (dim == 2) {
                targetX = targetX2x;
                targetY = targetY2x;
            } else {
                targetX = targetX4x;
                targetY = targetY4x;
            }

            int arrayPosition = targetY * dim + targetX;

            if (world.getTileEntity(pos) instanceof TileEntityGlyphHolder) {
                TileEntityGlyphHolder glyphHolderTE = (TileEntityGlyphHolder) world.getTileEntity(pos);

                if ((player.getHeldItemMainhand().getItem() instanceof ItemNeonGlyph ^ glyphHolderTE.glyphs[arrayPosition].getItem() instanceof ItemNeonGlyph) ||
                        (player.getHeldItemMainhand().isItemEqual(glyphHolderTE.glyphs[arrayPosition]) && ItemStack.areItemStackTagsEqual(player.getHeldItemMainhand(), glyphHolderTE.glyphs[arrayPosition]))) {
                    if (!world.isRemote) {
                        if (player.getHeldItemMainhand().getItem() instanceof ItemNeonGlyph && glyphHolderTE.glyphs[arrayPosition].isEmpty()) {
                            ItemStack stack = player.getHeldItemMainhand().copy();
                            stack.setCount(1);
                            glyphHolderTE.glyphs[arrayPosition] = stack;
                            player.getHeldItemMainhand().shrink(1);
                            world.playSound(
                                    null,
                                    pos.getX(), pos.getY(), pos.getZ(),
                                    SoundEvents.ENTITY_ITEMFRAME_PLACE,
                                    SoundCategory.PLAYERS,
                                    1.0F,
                                    0.5F
                            );
                        } else {
                            if (player.getHeldItemMainhand().isEmpty() ||
                                    (player.getHeldItemMainhand().isItemEqual(glyphHolderTE.glyphs[arrayPosition]) && ItemStack.areItemStackTagsEqual(player.getHeldItemMainhand(), glyphHolderTE.glyphs[arrayPosition]))
                            ) {
                                if (player.getHeldItemMainhand().isEmpty()) {
                                    player.setHeldItem(EnumHand.MAIN_HAND, glyphHolderTE.glyphs[arrayPosition]);
                                } else {
                                    player.getHeldItemMainhand().grow(1);
                                }

                                glyphHolderTE.glyphs[arrayPosition] = ItemStack.EMPTY;
                                world.playSound(
                                        null,
                                        pos.getX(), pos.getY(), pos.getZ(),
                                        SoundEvents.ENTITY_ITEM_PICKUP,
                                        SoundCategory.PLAYERS,
                                        1.0F,
                                        0.75F
                                );
                            }
                        }
                        glyphHolderTE.markDirtyAndNotify();
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public int getDim() {
        return dim;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        TileEntityGlyphHolder glyphHolder = new TileEntityGlyphHolder();
        glyphHolder.glyphs = new ItemStack[dim*dim];
        Arrays.fill(glyphHolder.glyphs, ItemStack.EMPTY);
        glyphHolder.dim = dim;
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
                worldIn.setBlockState(pos, state.withProperty(ACTIVE, shouldBeActive), 2);
            }
            worldIn.markBlockRangeForRenderUpdate(pos, pos);
        }
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state;
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
        EnumFacing facing = EnumFacing.byHorizontalIndex(meta & 3);
        boolean active = (meta & 4) != 0;
        return this.getDefaultState()
                .withProperty(FACING, facing)
                .withProperty(ACTIVE, active);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int facingMeta = state.getValue(FACING).getHorizontalIndex();
        int activeMeta = state.getValue(ACTIVE) ? 4 : 0;
        return facingMeta | activeMeta;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this,
                new IProperty[] {FACING, ACTIVE},
                new IUnlistedProperty[] {COLOR_GLYPH_PAIRS}
        );
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (state instanceof IExtendedBlockState) {
            IExtendedBlockState ebs = (IExtendedBlockState) state;
            TileEntity te = world.getTileEntity(pos);

            if (te instanceof TileEntityGlyphHolder) {
                TileEntityGlyphHolder glyphHolder = (TileEntityGlyphHolder) te;

                return ebs
                    .withProperty(COLOR_GLYPH_PAIRS, glyphHolder.serializeGlyphsAndColorsAsIntegerArray());
            }
        }
        return state;
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        if (layer == BloomEffectUtil.getBloomLayer()) {
            return state.getValue(ACTIVE);
        }
        return layer == BlockRenderLayer.TRANSLUCENT || layer == BlockRenderLayer.CUTOUT;
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

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (player.capabilities.isCreativeMode) {
            TileEntity te = world.getTileEntity(pos);
            if (te != null) {
                if (te instanceof TileEntityGlyphHolder) {
                    TileEntityGlyphHolder glyphHolder = (TileEntityGlyphHolder) te;
                    glyphHolder.brokenByCreativePlayer = true;
                }
            }
        }
        super.onBlockHarvested(world, pos, state, player);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityGlyphHolder) {
            TileEntityGlyphHolder glyphHolder = (TileEntityGlyphHolder) te;
            //if (!glyphHolder.brokenByCreativePlayer) spawnAsEntity(world, pos, new ItemStack(this));

            for (ItemStack glyph : glyphHolder.glyphs) {
                if (!glyph.isEmpty()) {
                    spawnAsEntity(world, pos, glyph);
                }
            }
        }
        super.breakBlock(world, pos, state);
    }
}
