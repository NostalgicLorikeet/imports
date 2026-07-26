package hitscan.nostalgic.woodworks.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockLetterBoard extends Block {
    public static final PropertyBool ACTIVE = PropertyBool.create("active");
    boolean inverted;

    public BlockLetterBoard(boolean inverted) {
        super(Material.ROCK);
        this.inverted = inverted;
        this.setRegistryName("letter_board" + (inverted ? "_inverted" : ""));
        this.setTranslationKey("letter_board" + (inverted ? "_inverted" : ""));
        this.setHarvestLevel("pickaxe", 0);
        this.setHardness(1.5F);
        this.setDefaultState(this.getBlockState().getBaseState()
                .withProperty(ACTIVE, inverted)
        );
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
    public int getMetaFromState(IBlockState state) {
        return state.getValue(ACTIVE) ? 1 : 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState()
                .withProperty(ACTIVE, meta == 1);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this,
                ACTIVE
        );
    }
}
