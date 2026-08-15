package hitscan.nostalgic.woodworks.items;

import hitscan.nostalgic.woodworks.blocks.BlockSignpost;
import hitscan.nostalgic.woodworks.registry.WoodworksBlocks;
import hitscan.nostalgic.woodworks.tileentities.TileEntitySignpost;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemGlub extends Item {
    public ItemGlub() {
        this.setRegistryName("test");
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        Block fence = worldIn.getBlockState(pos).getBlock();
        if (fence instanceof BlockFence && !(fence instanceof BlockSignpost)) {
            IBlockState state = fence.getDefaultState();
            BlockSignpost signpostType = state.getMaterial() == Material.IRON ? WoodworksBlocks.SIGNPOST_METAL : (state.getMaterial() == Material.ROCK ? WoodworksBlocks.SIGNPOST_STONE : WoodworksBlocks.SIGNPOST_WOOD);
            worldIn.setBlockState(pos, signpostType.getDefaultState());
            if (worldIn.getTileEntity(pos) instanceof TileEntitySignpost) {
                TileEntitySignpost signpost = (TileEntitySignpost) worldIn.getTileEntity(pos);
                signpost.setFenceDefaultState(state);

                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.PASS;
    }
}
