package hitscan.nostalgic.woodworks;

import hitscan.nostalgic.woodworks.registry.WoodworksBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WoodworksCreativeTab extends CreativeTabs {
    public WoodworksCreativeTab() {
        super(Tags.MOD_ID);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ItemStack createIcon() {
        return new ItemStack(WoodworksBlocks.SHELF_LONG);
    }
}
