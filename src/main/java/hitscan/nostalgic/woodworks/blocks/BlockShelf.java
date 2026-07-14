package hitscan.nostalgic.woodworks.blocks;

import hitscan.nostalgic.woodworks.properties.UnlistedPropertyString;
import hitscan.nostalgic.woodworks.registry.WoodworksBlocks;
import hitscan.nostalgic.woodworks.tileentities.TileEntityShelf;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockShelf extends BlockHorizontal {
    public static final PropertyBool LEFT = PropertyBool.create("has_left");
    public static final PropertyBool RIGHT = PropertyBool.create("has_right");
    public static final IUnlistedProperty<String> ID = new UnlistedPropertyString("id");
    public static final IUnlistedProperty<Integer> DAMAGE = Properties.toUnlisted(PropertyInteger.create("damage", 0, 32766));

    public final boolean canPlaceBlocksOnTop;
    public final boolean canPlaceItemStacksIn;
    public final boolean canPlaceOnSlabs;
    public final boolean hanging;

    public final AxisAlignedBB COLLISION_NORTH_AABB;
    public final AxisAlignedBB COLLISION_SOUTH_AABB;
    public final AxisAlignedBB COLLISION_EAST_AABB;
    public final AxisAlignedBB COLLISION_WEST_AABB;

    public final AxisAlignedBB BOUNDING_NORTH_AABB;
    public final AxisAlignedBB BOUNDING_SOUTH_AABB;
    public final AxisAlignedBB BOUNDING_EAST_AABB;
    public final AxisAlignedBB BOUNDING_WEST_AABB;

    public BlockShelf(String name, Material material, boolean canPlaceBlocksOnTop, boolean canPlaceItemStacksIn, boolean canPlaceOnSlabs, boolean hanging, AxisAlignedBB collisionNorth, AxisAlignedBB boundingNorth) {
        super(material);
        this.canPlaceBlocksOnTop = canPlaceBlocksOnTop;
        this.canPlaceItemStacksIn = canPlaceItemStacksIn;
        this.canPlaceOnSlabs = canPlaceOnSlabs;
        this.hanging = hanging;
        this.setRegistryName(name);
        this.setTranslationKey(name);
        this.setHarvestLevel("axe", 0);
        this.setHardness(2.0F);
        this.setSoundType(SoundType.WOOD);
        this.setDefaultState(this.getBlockState().getBaseState()
                .withProperty(FACING, EnumFacing.NORTH)
                .withProperty(LEFT, true)
                .withProperty(RIGHT, true)
        );

        this.COLLISION_NORTH_AABB = collisionNorth;
        this.COLLISION_SOUTH_AABB = new AxisAlignedBB(
                1.0D - collisionNorth.maxX, collisionNorth.minY, 1.0D - collisionNorth.maxZ,
                1.0D - collisionNorth.minX, collisionNorth.maxY, 1.0D - collisionNorth.minZ
        );
        this.COLLISION_EAST_AABB = new AxisAlignedBB(
                1.0D - collisionNorth.maxZ, collisionNorth.minY, collisionNorth.minX,
                1.0D - collisionNorth.minZ, collisionNorth.maxY, collisionNorth.maxX
        );
        this.COLLISION_WEST_AABB = new AxisAlignedBB(
                collisionNorth.minZ, collisionNorth.minY, 1.0D - collisionNorth.maxX,
                collisionNorth.maxZ, collisionNorth.maxY, 1.0D - collisionNorth.minX
        );

        this.BOUNDING_NORTH_AABB = boundingNorth;
        this.BOUNDING_SOUTH_AABB = new AxisAlignedBB(
                1.0D - boundingNorth.maxX, boundingNorth.minY, 1.0D - boundingNorth.maxZ,
                1.0D - boundingNorth.minX, boundingNorth.maxY, 1.0D - boundingNorth.minZ
        );
        this.BOUNDING_EAST_AABB = new AxisAlignedBB(
                1.0D - boundingNorth.maxZ, boundingNorth.minY, boundingNorth.minX,
                1.0D - boundingNorth.minZ, boundingNorth.maxY, boundingNorth.maxX
        );
        this.BOUNDING_WEST_AABB = new AxisAlignedBB(
                boundingNorth.minZ, boundingNorth.minY, 1.0D - boundingNorth.maxX,
                boundingNorth.maxZ, boundingNorth.maxY, 1.0D - boundingNorth.minX
        );
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (this.canPlaceItemStacksIn) {
            if (facing != EnumFacing.DOWN) {
                if (hand == EnumHand.MAIN_HAND) {
                    TileEntity te = world.getTileEntity(pos);
                    if (te instanceof TileEntityShelf) {
                        TileEntityShelf shelf = (TileEntityShelf) te;
                        int targetStackSlot;

                        if (state.getValue(FACING) == EnumFacing.NORTH || state.getValue(FACING) == EnumFacing.SOUTH) {
                            targetStackSlot = (int) (hitX / ((float) 1 / 3));
                        } else {
                            targetStackSlot = (int) (hitZ / ((float) 1 / 3));
                        }

                        targetStackSlot = MathHelper.clamp(targetStackSlot, 0, 2);

                        if (!world.isRemote) {
                            if (!player.isSneaking()) {
                                if (!player.getHeldItemMainhand().isEmpty() && shelf.stacks[targetStackSlot].isEmpty()) {
                                    shelf.stacks[targetStackSlot] = player.getHeldItemMainhand();
                                    player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
                                    world.playSound(
                                            null,
                                            pos.getX(), pos.getY(), pos.getZ(),
                                            SoundEvents.ENTITY_ITEMFRAME_PLACE,
                                            SoundCategory.PLAYERS,
                                            1.0F,
                                            0.5F
                                    );
                                } else if (!shelf.stacks[targetStackSlot].isEmpty()) {
                                    if (player.getHeldItemMainhand().isEmpty()) {
                                        ItemStack shelfStackCopy = shelf.stacks[targetStackSlot].copy();
                                        shelfStackCopy.setCount(1);
                                        player.setHeldItem(EnumHand.MAIN_HAND, shelfStackCopy);
                                        shelf.stacks[targetStackSlot].shrink(1);
                                        world.playSound(
                                                null,
                                                pos.getX(), pos.getY(), pos.getZ(),
                                                SoundEvents.ENTITY_ITEM_PICKUP,
                                                SoundCategory.PLAYERS,
                                                1.0F,
                                                0.75F
                                        );
                                    } else if (player.getHeldItemMainhand().isItemEqual(shelf.stacks[targetStackSlot]) && ItemStack.areItemStackTagsEqual(shelf.stacks[targetStackSlot], player.getHeldItemMainhand())) {
                                        if (player.getHeldItemMainhand().getCount() < player.getHeldItemMainhand().getMaxStackSize()) {
                                            if (!shelf.stacks[targetStackSlot].isEmpty()) {
                                                player.getHeldItemMainhand().grow(1);
                                                shelf.stacks[targetStackSlot].shrink(1);
                                            }
                                        }
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
                                shelf.markDirtyAndNotify();
                            } else if (player.getHeldItemMainhand().isEmpty()) {
                                if (!shelf.stacks[targetStackSlot].isEmpty()) {
                                    player.setHeldItem(EnumHand.MAIN_HAND, shelf.stacks[targetStackSlot]);
                                    shelf.stacks[targetStackSlot] = ItemStack.EMPTY;
                                    shelf.markDirtyAndNotify();
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
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        EnumFacing facing = state.getValue(FACING);

        if (state.getActualState(source, pos).getValue(LEFT) || state.getActualState(source, pos).getValue(RIGHT)) {
            if (facing == EnumFacing.NORTH) return BOUNDING_NORTH_AABB;
            if (facing == EnumFacing.SOUTH) return BOUNDING_SOUTH_AABB;
            if (facing == EnumFacing.EAST) return BOUNDING_EAST_AABB;
            return BOUNDING_WEST_AABB;
        } else {
            if (facing == EnumFacing.NORTH) return COLLISION_NORTH_AABB;
            if (facing == EnumFacing.SOUTH) return COLLISION_SOUTH_AABB;
            if (facing == EnumFacing.EAST) return COLLISION_EAST_AABB;
            return COLLISION_WEST_AABB;
        }
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        EnumFacing facing = state.getValue(FACING);

        if (facing == EnumFacing.NORTH) addCollisionBoxToList(pos, entityBox, collidingBoxes, COLLISION_NORTH_AABB);
        if (facing == EnumFacing.SOUTH) addCollisionBoxToList(pos, entityBox, collidingBoxes, COLLISION_SOUTH_AABB);
        if (facing == EnumFacing.EAST) addCollisionBoxToList(pos, entityBox, collidingBoxes, COLLISION_EAST_AABB);
        if (facing == EnumFacing.WEST) addCollisionBoxToList(pos, entityBox, collidingBoxes, COLLISION_WEST_AABB);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        TileEntityShelf shelf = new TileEntityShelf();
        shelf.centerItems = this == WoodworksBlocks.SHELF_DISPLAY_HANGING || this == WoodworksBlocks.SHELF_DISPLAY_CHAINED;
        shelf.centerZOnly = this == WoodworksBlocks.SHELF_DISPLAY_CHAINED;
        return shelf;
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
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.UP;
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
        if (hanging) {
            if (side == EnumFacing.UP) {
                return world.isSideSolid(pos,EnumFacing.DOWN);
            } else {
                if (world.isSideSolid(pos.up(), EnumFacing.DOWN)) {
                    return true;
                }
            }
        } else {
            BlockPos attachedPos = pos.offset(side.getOpposite());

            if (world.getBlockState(attachedPos).getBlock() instanceof BlockShelf) {
                for (EnumFacing horizontal : EnumFacing.HORIZONTALS) {
                    if (horizontal == side.getOpposite()) {
                        continue;
                    }
                    if (world.isSideSolid(pos.offset(horizontal.getOpposite()), horizontal)) {
                        return true;
                    }
                }
                return false;
            }

            if (canPlaceOnSlabs) {
                if (world.getBlockState(attachedPos).getBlock() instanceof BlockSlab) {
                    BlockSlab slab = (BlockSlab) world.getBlockState(attachedPos).getBlock();

                    if (!slab.isDouble()) {
                        return world.getBlockState(attachedPos).getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP;
                    }
                }
            }

            if (side.getHorizontalIndex() != -1) {
                return world.isSideSolid(attachedPos, side);
            }

            for (EnumFacing horizontal : EnumFacing.HORIZONTALS) {
                if (world.isSideSolid(pos.offset(horizontal.getOpposite()), horizontal)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        if (world.getBlockState(pos.offset(facing.getOpposite())).getBlock() instanceof BlockShelf) {
            for (EnumFacing horizontal : EnumFacing.HORIZONTALS) {
                if (horizontal == facing.getOpposite()) {
                    continue;
                }
                if (world.isSideSolid(pos.offset(horizontal.getOpposite()), horizontal)) {
                    return this.getDefaultState().withProperty(FACING, horizontal.getOpposite());
                }
            }
        }

        if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
            for (EnumFacing horizontal : EnumFacing.HORIZONTALS) {
                if (world.isSideSolid(pos.offset(horizontal.getOpposite()), horizontal)) {
                    return this.getDefaultState().withProperty(FACING, horizontal.getOpposite());
                }
            }
        }

        return this.getDefaultState().withProperty(FACING, hanging ? placer.getHorizontalFacing() : facing.getOpposite());
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((state.getValue(LEFT) ? 1 : 0) + (state.getValue(RIGHT) ? 2 : 0)) * 4 + state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean left = meta > 11 || (meta > 3 && meta < 8);
        boolean right = meta > 7;
        EnumFacing facing = EnumFacing.byHorizontalIndex(meta % 4);

        return this.getDefaultState()
                .withProperty(FACING, facing)
                .withProperty(LEFT, left)
                .withProperty(RIGHT, right);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this,
                new IProperty[] {FACING, LEFT, RIGHT},
                new IUnlistedProperty[]{ID, DAMAGE}
        );
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (state instanceof IExtendedBlockState) {
            IExtendedBlockState ebs = (IExtendedBlockState) state;
            TileEntity te = world.getTileEntity(pos);
            String id = "minecraft:planks";
            int damage = 0;

            if (te instanceof TileEntityShelf) {
                TileEntityShelf shelf = (TileEntityShelf) te;
                id = shelf.getId();
                damage = shelf.getDamage();
            }

            return ebs
                    .withProperty(ID, id)
                    .withProperty(DAMAGE, damage < 32767 ? damage : 32766);
        }
        return state;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing side) {
        return (side == EnumFacing.UP && canPlaceBlocksOnTop) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        EnumFacing facing = state.getValue(FACING);
        boolean left = true;
        boolean right = true;

        IBlockState stateLeft = worldIn.getBlockState(pos.offset(facing.rotateYCCW()));
        IBlockState stateRight = worldIn.getBlockState(pos.offset(facing.rotateY()));
        if (stateLeft.getBlock() == this) {
            left = stateLeft.getValue(FACING) != facing;
        }
        if (stateRight.getBlock() == this) {
            right = stateRight.getValue(FACING) != facing;
        }

        return state.withProperty(LEFT, left).withProperty(RIGHT, right);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity te = world.getTileEntity(pos);
        if (te != null) {
            TileEntityShelf shelf = (TileEntityShelf) te;
            NBTTagCompound tag = stack.getTagCompound();

            if (canPlaceItemStacksIn) {
                shelf.setCanPlaceItemStacksIn();
                shelf.rotation = state.getValue(FACING);
            }

            if (tag != null) {
                if (tag.hasKey("Texture")) {
                    NBTTagCompound texture = tag.getCompoundTag("Texture");
                    if (texture.hasKey("id")) {
                        shelf.setId(texture.getString("id"));
                    }

                    if (texture.hasKey("Damage")) {
                        shelf.setDamage(texture.getInteger("Damage"));
                    }
                }
            }

            shelf.markDirty();
        }
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (player.capabilities.isCreativeMode) {
            TileEntity te = world.getTileEntity(pos);
            if (te != null) {
                if (te instanceof TileEntityShelf) {
                    TileEntityShelf shelf = (TileEntityShelf) te;
                    shelf.brokenByCreativePlayer = true;
                }
            }
        }
        super.onBlockHarvested(world, pos, state, player);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityShelf) {
            TileEntityShelf shelf = (TileEntityShelf) te;
            ItemStack stack = new ItemStack(this);
            NBTTagCompound tag = new NBTTagCompound();
            NBTTagCompound texture = new NBTTagCompound();
            texture.setString("id", shelf.getId());
            texture.setInteger("Damage", shelf.getDamage());
            tag.setTag("Texture", texture);
            stack.setTagCompound(tag);

            if (!shelf.brokenByCreativePlayer) spawnAsEntity(world, pos, stack);

            if (shelf.canPlaceItemStacksIn()) {
                for (int i = 0; i < 3; i++) {
                    if (shelf.stacks[i] != ItemStack.EMPTY) spawnAsEntity(world, pos, shelf.stacks[i]);
                }
            }
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        return new ArrayList<>();
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        ItemStack stack = super.getItem(worldIn, pos, state);
        NBTTagCompound tag = new NBTTagCompound();

        TileEntity te = worldIn.getTileEntity(pos);
        if (te != null) {
            if (te instanceof TileEntityShelf) {
                TileEntityShelf shelf = (TileEntityShelf) te;
                NBTTagCompound texture = new NBTTagCompound();
                texture.setString("id", shelf.getId());
                texture.setInteger("Damage", shelf.getDamage());
                tag.setTag("Texture", texture);
                stack.setTagCompound(tag);
            }
        }
        return stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return this == WoodworksBlocks.SHELF_DISPLAY_CHAINED || this == WoodworksBlocks.SHELF_DISPLAY_HANGING ? BlockRenderLayer.CUTOUT : super.getRenderLayer();
    }
}