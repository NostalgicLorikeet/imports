package hitscan.nostalgic.woodworks.blocks;

import hitscan.nostalgic.woodworks.items.ItemPodium;
import hitscan.nostalgic.woodworks.properties.UnlistedPropertyString;
import hitscan.nostalgic.woodworks.registry.WoodworksBlocks;
import hitscan.nostalgic.woodworks.tileentities.TileEntityPodium;
import hitscan.nostalgic.woodworks.tileentities.TileEntityShelf;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
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
import net.minecraftforge.common.property.Properties;

public class BlockPodium extends Block {
    public static final PropertyInteger TYPE = PropertyInteger.create("type", 0, 3);
    public static final IUnlistedProperty<String> ID_WOOD = new UnlistedPropertyString("id");
    public static final IUnlistedProperty<Integer> DAMAGE_WOOD = Properties.toUnlisted(PropertyInteger.create("damage", 0, 32766));
    public static final IUnlistedProperty<String> ID_STONE = new UnlistedPropertyString("id");
    public static final IUnlistedProperty<Integer> DAMAGE_STONE = Properties.toUnlisted(PropertyInteger.create("damage", 0, 32766));

    public static final AxisAlignedBB BOUNDING_TOP = new AxisAlignedBB(1/4D, 0, 1/4D, 3/4D, 1/2D, 3/4D);
    public static final AxisAlignedBB BOUNDING_LONE = new AxisAlignedBB(1/4D, 0, 1/4D, 3/4D, 7/8D, 3/4D);
    public static final AxisAlignedBB BOUNDING_MIDDLE = new AxisAlignedBB(5/16D, 0, 5/16D, 11/16D, 1D, 11/16D);
    public static final AxisAlignedBB BOUNDING_BOTTOM = new AxisAlignedBB(1/4D, 0, 1/4D, 3/4D, 1, 3/4D);

    public boolean single = false;

    public BlockPodium(String name) {
        super(Material.WOOD);
        this.setRegistryName(name);
        this.setTranslationKey(name);
        this.setSoundType(SoundType.STONE);
        this.setHarvestLevel("axe", 0);
        this.single = name.equals("podium_trophy");
        this.setDefaultState(this.getBlockState().getBaseState()
                .withProperty(TYPE, 0)
        );
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityPodium) {
            IBlockState actualState = state.getActualState(worldIn, pos);
            TileEntityPodium podium = (TileEntityPodium) worldIn.getTileEntity(pos);

            if (actualState.getValue(TYPE) != 0 && actualState.getValue(TYPE) != 3) {
                spawnAsEntity(worldIn, pos, podium.stack);
                podium.stack = ItemStack.EMPTY;
                podium.markDirtyAndNotify();
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if ((facing == EnumFacing.UP || hitY > (11/16F)) && (state.getActualState(world, pos).getValue(TYPE) == 0 || state.getActualState(world, pos).getValue(TYPE) == 3)) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileEntityPodium) {
                TileEntityPodium podium = (TileEntityPodium) te;

                if (hand == EnumHand.MAIN_HAND) {
                    if (player.getHeldItemMainhand().isEmpty() && !podium.stack.isEmpty()) {
                        if (!world.isRemote) {
                            if (!player.isSneaking()) {
                                ItemStack exportStack = podium.stack.copy();
                                exportStack.setCount(1);
                                podium.stack.shrink(1);
                                player.setHeldItem(EnumHand.MAIN_HAND, exportStack);
                            } else {
                                player.setHeldItem(EnumHand.MAIN_HAND, podium.stack);
                                podium.stack = ItemStack.EMPTY;
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
                        podium.markDirtyAndNotify();
                        return true;
                    } else if (!player.getHeldItemMainhand().isEmpty() && podium.stack.isEmpty()) {
                        if (!world.isRemote) {
                            if (single) {
                                ItemStack insertStack = player.getHeldItemMainhand().copy();
                                insertStack.setCount(1);
                                podium.stack = insertStack;
                                player.getHeldItemMainhand().shrink(1);
                            } else {
                                podium.stack = player.getHeldItemMainhand();
                                player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
                            }
                            world.playSound(
                                    null,
                                    pos.getX(), pos.getY(), pos.getZ(),
                                    SoundEvents.ENTITY_ITEMFRAME_PLACE,
                                    SoundCategory.PLAYERS,
                                    1.0F,
                                    0.8F
                            );
                        }
                        podium.markDirtyAndNotify();
                        return true;
                    } else if (!player.getHeldItemMainhand().isEmpty() && !podium.stack.isEmpty()) {
                        if (player.getHeldItemMainhand().isItemEqual(podium.stack) && ItemStack.areItemStackTagsEqual(player.getHeldItemMainhand(), podium.stack)) {
                            if (!world.isRemote) {
                                if (player.getHeldItemMainhand().getCount() < player.getHeldItemMainhand().getMaxStackSize()) {
                                    player.getHeldItemMainhand().grow(1);
                                    podium.stack.shrink(1);
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
                            podium.markDirtyAndNotify();
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        TileEntityPodium podium = new TileEntityPodium();
        podium.single = single;
        return podium;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        if (state.getActualState(source, pos).getValue(TYPE) == 0 || state.getActualState(source, pos).getValue(TYPE) == 3) return BOUNDING_LONE;
        if (state.getActualState(source, pos).getValue(TYPE) == 1) return BOUNDING_BOTTOM;
        if (state.getActualState(source, pos).getValue(TYPE) == 2) return BOUNDING_MIDDLE;
        return BOUNDING_TOP;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        boolean isAbovePodium = worldIn.getBlockState(pos.up()).getBlock() instanceof BlockPodium;
        boolean isBelowPodium = worldIn.getBlockState(pos.down()).getBlock() instanceof BlockPodium;

        if (!isAbovePodium && !isBelowPodium) return this.getDefaultState().withProperty(TYPE, 0);
        if (isAbovePodium && !isBelowPodium) return this.getDefaultState().withProperty(TYPE, 1);
        if (isAbovePodium && isBelowPodium) return this.getDefaultState().withProperty(TYPE, 2);
        if (!isAbovePodium && isBelowPodium) return this.getDefaultState().withProperty(TYPE, 3);
        return this.getDefaultState();
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
    public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
        return world.isSideSolid(pos.down(), EnumFacing.UP) || world.getBlockState(pos.down()).getBlock() instanceof BlockPodium;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        if (!(world.getBlockState(pos.down()).getBlock() instanceof BlockPodium) && !(world.getBlockState(pos.down()).getBlock() instanceof BlockPodium)) {
            return this.getDefaultState().withProperty(TYPE, 0);
        } else if (world.getBlockState(pos.up()).getBlock() instanceof BlockPodium && !(world.getBlockState(pos.down()).getBlock() instanceof BlockPodium)) {
            return this.getDefaultState().withProperty(TYPE, 1);
        } else if (world.getBlockState(pos.down()).getBlock() instanceof BlockPodium && world.getBlockState(pos.up()).getBlock() instanceof BlockPodium) {
            return this.getDefaultState().withProperty(TYPE, 2);
        } else {
            return this.getDefaultState().withProperty(TYPE, 3);
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this,
                new IProperty[] {TYPE},
                new IUnlistedProperty[]{ID_WOOD, DAMAGE_WOOD, ID_STONE, DAMAGE_STONE}
        );
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (state instanceof IExtendedBlockState) {
            IExtendedBlockState ebs = (IExtendedBlockState) state;
            TileEntity te = world.getTileEntity(pos);
            String idWood = "minecraft:planks";
            String idStone = "minecraft:stone";
            int damageWood = 0;
            int damageStone = 0;

            if (te instanceof TileEntityPodium) {
                TileEntityPodium podium = (TileEntityPodium) te;
                idWood = podium.id_wood;
                idStone = podium.id_stone;
                damageWood = podium.damage_wood;
                damageStone = podium.damage_stone;
            }

            return ebs
                    .withProperty(ID_WOOD, idWood)
                    .withProperty(ID_STONE, idStone)
                    .withProperty(DAMAGE_WOOD, damageWood < 32767 ? damageWood : 32766)
                    .withProperty(DAMAGE_STONE, damageStone < 32767 ? damageStone : 32766);
        }
        return state;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(TYPE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TYPE, meta);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing side) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (player.capabilities.isCreativeMode) {
            TileEntity te = world.getTileEntity(pos);
            if (te != null) {
                if (te instanceof TileEntityPodium) {
                    TileEntityPodium podium = (TileEntityPodium) te;
                    podium.brokenByCreativePlayer = true;
                }
            }
        }
        super.onBlockHarvested(world, pos, state, player);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityPodium) {
            TileEntityPodium podium = (TileEntityPodium) te;
            ItemStack stack = new ItemStack(this);
            NBTTagCompound tag = new NBTTagCompound();
            NBTTagCompound texture = new NBTTagCompound();
            NBTTagCompound wood = new NBTTagCompound();
            NBTTagCompound stone = new NBTTagCompound();
            wood.setString("ID", podium.id_wood);
            wood.setInteger("Damage", podium.damage_wood);
            stone.setString("ID", podium.id_stone);
            stone.setInteger("Damage", podium.damage_stone);
            texture.setTag("Wood", wood);
            texture.setTag("Stone", stone);
            tag.setTag("Texture", texture);
            stack.setTagCompound(tag);

            if (!podium.brokenByCreativePlayer) spawnAsEntity(world, pos, stack);

            if (!podium.stack.isEmpty()) {
                spawnAsEntity(world, pos, podium.stack);
            }
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity te = world.getTileEntity(pos);
        if (te != null) {
            TileEntityPodium podium = (TileEntityPodium) te;
            NBTTagCompound tag = stack.getTagCompound();

            if (tag != null) {
                if (tag.hasKey("Texture")) {
                    NBTTagCompound texture = tag.getCompoundTag("Texture");
                    NBTTagCompound wood = texture.getCompoundTag("Wood");
                    NBTTagCompound stone = texture.getCompoundTag("Stone");
                    podium.id_wood = wood.getString("ID");
                    podium.damage_wood = wood.getInteger("Damage");
                    podium.id_stone = stone.getString("ID");
                    podium.damage_stone = stone.getInteger("Damage");
                    podium.facing = placer.getHorizontalFacing();
                }
            }

            podium.markDirty();
        }
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        ItemStack stack = super.getItem(worldIn, pos, state);
        NBTTagCompound tag = new NBTTagCompound();

        TileEntity te = worldIn.getTileEntity(pos);
        if (te != null) {
            if (te instanceof TileEntityPodium) {
                TileEntityPodium podium = (TileEntityPodium) te;
                NBTTagCompound texture = new NBTTagCompound();
                NBTTagCompound wood = new NBTTagCompound();
                NBTTagCompound stone = new NBTTagCompound();
                wood.setString("ID", podium.id_wood);
                wood.setInteger("Damage", podium.damage_wood);
                stone.setString("ID", podium.id_stone);
                stone.setInteger("Damage", podium.damage_stone);
                texture.setTag("Wood", wood);
                texture.setTag("Stone", stone);
                tag.setTag("Texture", texture);
                stack.setTagCompound(tag);
            }
        }
        return stack;
    }
}
