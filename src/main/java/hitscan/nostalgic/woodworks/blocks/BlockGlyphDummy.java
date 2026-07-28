package hitscan.nostalgic.woodworks.blocks;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

public class BlockGlyphDummy extends BlockHorizontal {
    public BlockGlyphDummy(String name) {
        super(Material.ROCK);
        this.setRegistryName(name);
        this.setDefaultState(this.getBlockState().getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this,
                FACING
        );
    }
}
