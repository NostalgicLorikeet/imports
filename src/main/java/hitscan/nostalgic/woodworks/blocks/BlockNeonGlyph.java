package hitscan.nostalgic.woodworks.blocks;

import gregtech.client.utils.BloomEffectUtil;
import hitscan.nostalgic.woodworks.registry.WoodworksBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockNeonGlyph extends BlockDirectional {
    public static final PropertyBool ACTIVE = PropertyBool.create("active");
    boolean inverted;

    public BlockNeonGlyph(boolean inverted) {
        super(Material.IRON);
        this.setRegistryName("neon_glyph_block" + (inverted ? "_inverted" : ""));
        this.setTranslationKey("neon_glyph_block" + (inverted ? "_inverted" : ""));
        this.setHarvestLevel("pickaxe", 0);
        this.setHardness(1.5F);
        this.setDefaultState(this.getBlockState().getBaseState()
                .withProperty(FACING, EnumFacing.NORTH)
                .withProperty(ACTIVE, inverted)
        );
        this.inverted = inverted;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!worldIn.isRemote) {
            boolean isPowered = worldIn.isBlockPowered(pos);
            boolean shouldBeActive = this.inverted != isPowered;

            if (state.getValue(ACTIVE) != shouldBeActive) {
                worldIn.setBlockState(pos, state.withProperty(ACTIVE, shouldBeActive), 3);
            }
        }
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            boolean isPowered = worldIn.isBlockPowered(pos);
            boolean shouldBeActive = this.inverted != isPowered;

            if (state.getValue(ACTIVE) != shouldBeActive) {
                worldIn.setBlockState(pos, state.withProperty(ACTIVE, shouldBeActive), 2);
            }
        }
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        boolean isPowered = world.isBlockPowered(pos);
        boolean shouldBeActive = this.inverted != isPowered;

        return this.getDefaultState()
                .withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos,placer))
                .withProperty(ACTIVE, shouldBeActive);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        int facingIndex = (meta & 7) % 6;
        boolean isActive = (meta & 8) != 0;

        return this.getDefaultState()
                .withProperty(ACTIVE, isActive)
                .withProperty(FACING, EnumFacing.byIndex(facingIndex));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = state.getValue(FACING).getIndex();
        if (state.getValue(ACTIVE)) {
            i |= 8;
        }
        return i;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this,
                FACING, ACTIVE
        );
    }

    @Override
    public int getLightValue(IBlockState state) {
        return state.getValue(ACTIVE) ? 10 : 0;
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        if (layer == BloomEffectUtil.getBloomLayer()) {
            return state.getValue(ACTIVE);
        }
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
}
