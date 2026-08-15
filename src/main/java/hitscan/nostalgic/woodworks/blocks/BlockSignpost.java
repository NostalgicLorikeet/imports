package hitscan.nostalgic.woodworks.blocks;

import hitscan.nostalgic.woodworks.registry.WoodworksBlocks;
import hitscan.nostalgic.woodworks.tileentities.TileEntitySignpost;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSignpost extends BlockFence {
    public static final IUnlistedProperty<IBlockState> FENCE_ACTUAL_STATE = new IUnlistedProperty<IBlockState>() {
        @Override
        public String getName() {return "fence_actual_state";}
        @Override
        public boolean isValid(IBlockState value) {return value.getBlock() instanceof BlockFence;}
        @Override
        public Class<IBlockState> getType() {return IBlockState.class;}
        @Override
        public String valueToString(IBlockState value) {return value.toString();}
    };

    public BlockSignpost(Material material, String name, String effectiveTool) {
        super(material, MapColor.GRAY);
        this.setRegistryName("signpost_" + name);
        this.setTranslationKey("signpost");
        this.setHarvestLevel(effectiveTool, Blocks.OAK_FENCE.getHarvestLevel(Blocks.OAK_FENCE.getDefaultState()));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        if (source.getTileEntity(pos) instanceof TileEntitySignpost) {
            TileEntitySignpost signpost = (TileEntitySignpost) source.getTileEntity(pos);

            Block block = signpost.getFenceDefaultState().getBlock();

            return block.getBoundingBox(signpost.getFenceDefaultState().getActualState(source, pos), source, pos);
        }

        return super.getBoundingBox(state, source, pos);
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
        if (worldIn.getTileEntity(pos) instanceof TileEntitySignpost) {
            TileEntitySignpost signpost = (TileEntitySignpost) worldIn.getTileEntity(pos);

            Block block = signpost.getFenceDefaultState().getBlock();

            return block.getBlockHardness(signpost.getFenceDefaultState(), worldIn, pos);
        }

        return super.getBlockHardness(blockState, worldIn, pos);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntitySignpost();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this,
                new IProperty[] {NORTH, SOUTH, EAST, WEST},
                new IUnlistedProperty[]{FENCE_ACTUAL_STATE}
        );
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (state instanceof IExtendedBlockState) {
            IExtendedBlockState ebs = (IExtendedBlockState) state;
            TileEntity te = world.getTileEntity(pos);
            IBlockState fenceActualState = null;

            if (te instanceof TileEntitySignpost) {
                TileEntitySignpost signpost = (TileEntitySignpost) te;
                fenceActualState = signpost.getFenceDefaultState().getActualState(world, pos);
            }

            return ebs.withProperty(FENCE_ACTUAL_STATE, fenceActualState);
        }
        return state;
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        if (worldIn.getTileEntity(pos) instanceof TileEntitySignpost) {
            TileEntitySignpost signpost = (TileEntitySignpost) worldIn.getTileEntity(pos);

            Block block = signpost.getFenceDefaultState().getBlock();

            return block.getItem(worldIn, pos, block.getDefaultState());
        }

        return ItemStack.EMPTY;
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (player.capabilities.isCreativeMode) {
            TileEntity te = world.getTileEntity(pos);
            if (te != null) {
                if (te instanceof TileEntitySignpost) {
                    TileEntitySignpost signpost = (TileEntitySignpost) te;
                    signpost.brokenByCreativePlayer = true;
                }
            }
        }
        super.onBlockHarvested(world, pos, state, player);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (worldIn.getTileEntity(pos) instanceof TileEntitySignpost) {
            TileEntitySignpost signpost = (TileEntitySignpost) worldIn.getTileEntity(pos);

            if (!signpost.brokenByCreativePlayer) {
                Block block = signpost.getFenceDefaultState().getBlock();

                Block.spawnAsEntity(worldIn, pos, block.getItem(worldIn, pos, block.getDefaultState()));
            }
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
}
