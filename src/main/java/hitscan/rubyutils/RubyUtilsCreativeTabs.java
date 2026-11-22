package hitscan.rubyutils;

import hitscan.rubyutils.init.RubyUtilsItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RubyUtilsCreativeTabs extends CreativeTabs {
    public static final CreativeTabs RUBYUTILS_TAB = new RubyUtilsCreativeTabs("rubyutils",new ItemStack(RubyUtilsItems.PODIUM));

    private final ItemStack icon;

    public RubyUtilsCreativeTabs(String name, ItemStack icon) {
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
