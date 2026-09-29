package hitscan.nostalgic.woodworks;

import hitscan.nostalgic.woodworks.registry.WoodworksBlocks;
import hitscan.nostalgic.woodworks.registry.WoodworksItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WoodworksCreativeTabs extends CreativeTabs {
    public static final CreativeTabs WOODWORKS_TAB = new WoodworksCreativeTabs("woodworks",new ItemStack(WoodworksItems.PODIUM));

    private final ItemStack icon;

    public WoodworksCreativeTabs(String name, ItemStack icon) {
        super(Tags.MOD_ID + "." + name);
        this.setBackgroundImageName("item_search.png");
        this.icon = icon;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ItemStack createIcon() {
        return icon;
    }

    @Override
    public boolean hasSearchBar() {
        return true;
    }
}
