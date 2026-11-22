package hitscan.rubyutils.blocks;

import hitscan.rubyutils.init.RubyUtilsBlocks;
import hitscan.rubyutils.tileentities.TileEntityShelf;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class BlockShelfChained extends BlockShelf {
    public static final PropertyBool HANGING = PropertyBool.create("hanging");

    public final AxisAlignedBB HANGING_COLLISION_NORTH_AABB;
    public final AxisAlignedBB HANGING_COLLISION_SOUTH_AABB;
    public final AxisAlignedBB HANGING_COLLISION_EAST_AABB;
    public final AxisAlignedBB HANGING_COLLISION_WEST_AABB;

    public final AxisAlignedBB HANGING_BOUNDING_NORTH_AABB;
    public final AxisAlignedBB HANGING_BOUNDING_SOUTH_AABB;
    public final AxisAlignedBB HANGING_BOUNDING_EAST_AABB;
    public final AxisAlignedBB HANGING_BOUNDING_WEST_AABB;

    public BlockShelfChained(String name, Material material, boolean canPlaceBlocksOnTop, boolean canPlaceItemStacksIn, boolean canPlaceOnSlabs, AxisAlignedBB collisionNorth, AxisAlignedBB boundingNorth, AxisAlignedBB hangingCollisionNorth, AxisAlignedBB hangingBoundingNorth) {
        super(name, material, canPlaceBlocksOnTop, canPlaceItemStacksIn, canPlaceOnSlabs, collisionNorth, boundingNorth);
        this.setDefaultState(this.getBlockState().getBaseState()
                .withProperty(FACING, EnumFacing.NORTH)
                .withProperty(LEFT, true)
                .withProperty(RIGHT, true)
                .withProperty(HANGING, false)
        );

        this.HANGING_COLLISION_NORTH_AABB = hangingCollisionNorth;
        this.HANGING_COLLISION_SOUTH_AABB = new AxisAlignedBB(
                1.0D - hangingCollisionNorth.maxX, hangingCollisionNorth.minY, 1.0D - hangingCollisionNorth.maxZ,
                1.0D - hangingCollisionNorth.minX, hangingCollisionNorth.maxY, 1.0D - hangingCollisionNorth.minZ
        );
        this.HANGING_COLLISION_EAST_AABB = new AxisAlignedBB(
                1.0D - hangingCollisionNorth.maxZ, hangingCollisionNorth.minY, hangingCollisionNorth.minX,
                1.0D - hangingCollisionNorth.minZ, hangingCollisionNorth.maxY, hangingCollisionNorth.maxX
        );
        this.HANGING_COLLISION_WEST_AABB = new AxisAlignedBB(
                hangingCollisionNorth.minZ, hangingCollisionNorth.minY, 1.0D - hangingCollisionNorth.maxX,
                hangingCollisionNorth.maxZ, hangingCollisionNorth.maxY, 1.0D - hangingCollisionNorth.minX
        );

        this.HANGING_BOUNDING_NORTH_AABB = hangingBoundingNorth;
        this.HANGING_BOUNDING_SOUTH_AABB = new AxisAlignedBB(
                1.0D - hangingBoundingNorth.maxX, hangingBoundingNorth.minY, 1.0D - hangingBoundingNorth.maxZ,
                1.0D - hangingBoundingNorth.minX, hangingBoundingNorth.maxY, 1.0D - hangingBoundingNorth.minZ
        );
        this.HANGING_BOUNDING_EAST_AABB = new AxisAlignedBB(
                1.0D - hangingBoundingNorth.maxZ, hangingBoundingNorth.minY, hangingBoundingNorth.minX,
                1.0D - hangingBoundingNorth.minZ, hangingBoundingNorth.maxY, hangingBoundingNorth.maxX
        );
        this.HANGING_BOUNDING_WEST_AABB = new AxisAlignedBB(
                hangingBoundingNorth.minZ, hangingBoundingNorth.minY, 1.0D - hangingBoundingNorth.maxX,
                hangingBoundingNorth.maxZ, hangingBoundingNorth.maxY, 1.0D - hangingBoundingNorth.minX
        );
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        if (!state.getValue(HANGING)) {
            return super.getBoundingBox(state, source, pos);
        }

        EnumFacing facing = state.getValue(FACING);

        if (state.getActualState(source, pos).getValue(LEFT) || state.getActualState(source, pos).getValue(RIGHT)) {
            if (facing == EnumFacing.NORTH) return HANGING_BOUNDING_NORTH_AABB;
            if (facing == EnumFacing.SOUTH) return HANGING_BOUNDING_SOUTH_AABB;
            if (facing == EnumFacing.EAST) return HANGING_BOUNDING_EAST_AABB;
            return HANGING_BOUNDING_WEST_AABB;
        } else {
            if (facing == EnumFacing.NORTH) return HANGING_COLLISION_NORTH_AABB;
            if (facing == EnumFacing.SOUTH) return HANGING_COLLISION_SOUTH_AABB;
            if (facing == EnumFacing.EAST) return HANGING_COLLISION_EAST_AABB;
            return HANGING_COLLISION_WEST_AABB;
        }
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        if (!state.getValue(HANGING)) {
            super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, isActualState);
        } else {
            EnumFacing facing = state.getValue(FACING);

            if (facing == EnumFacing.NORTH) addCollisionBoxToList(pos, entityBox, collidingBoxes, HANGING_COLLISION_NORTH_AABB);
            if (facing == EnumFacing.SOUTH) addCollisionBoxToList(pos, entityBox, collidingBoxes, HANGING_COLLISION_SOUTH_AABB);
            if (facing == EnumFacing.EAST) addCollisionBoxToList(pos, entityBox, collidingBoxes, HANGING_COLLISION_EAST_AABB);
            if (facing == EnumFacing.WEST) addCollisionBoxToList(pos, entityBox, collidingBoxes, HANGING_COLLISION_WEST_AABB);
        }
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        TileEntityShelf shelf = new TileEntityShelf();
        shelf.centerItems = true;
        shelf.centerZOnly = !state.getValue(HANGING);
        return shelf;
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
       if (side == EnumFacing.DOWN) {
           return world.isSideSolid(pos.up(),EnumFacing.DOWN);
       }

       return super.canPlaceBlockOnSide(world, pos, side);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        if (facing == EnumFacing.DOWN) {
            return this.getDefaultState()
                    .withProperty(FACING, placer.getHorizontalFacing().getOpposite())
                    .withProperty(HANGING, true);
        }

        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand)
                .withProperty(HANGING, false);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this,
                new IProperty[] {FACING, LEFT, RIGHT, HANGING},
                new IUnlistedProperty[]{ID, DAMAGE}
        );
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex() + (state.getValue(HANGING) ? 4 : 0);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.byHorizontalIndex(meta > 3 ? meta - 4 : meta);
        boolean hanging = meta > 3;

        return this.getDefaultState()
                .withProperty(FACING, facing)
                .withProperty(HANGING, hanging);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        EnumFacing facing = state.getValue(FACING);
        boolean left = true;
        boolean right = true;
        boolean hanging = state.getValue(HANGING);

        IBlockState stateLeft = worldIn.getBlockState(pos.offset(facing.rotateYCCW()));
        IBlockState stateRight = worldIn.getBlockState(pos.offset(facing.rotateY()));
        if (stateLeft.getBlock() == this && stateLeft.getValue(HANGING) == hanging) {
            left = stateLeft.getValue(FACING) != facing;
        }
        if (stateRight.getBlock() == this && stateRight.getValue(HANGING) == hanging) {
            right = stateRight.getValue(FACING) != facing;
        }

        return state.withProperty(LEFT, left).withProperty(RIGHT, right);
    }
}
