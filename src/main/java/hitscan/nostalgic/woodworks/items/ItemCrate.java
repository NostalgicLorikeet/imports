package hitscan.nostalgic.woodworks.items;

import hitscan.nostalgic.woodworks.registry.WoodworksBlocks;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

public class ItemCrate extends ItemStorageBlock {
    public static final int CREATIVE_TAB_CRATE_COUNT = 16;

    public ItemCrate(Block block) {
        super(block, 9);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        int count = 0;

        if (this.isInCreativeTab(tab)) {
            for (ItemStack stack : OreDictionary.getOres("plankWood")) {
                if (count == CREATIVE_TAB_CRATE_COUNT) break;
                ItemStack itemStack = new ItemStack(WoodworksBlocks.CRATE_WOODEN);
                NBTTagCompound tag = new NBTTagCompound();
                NBTTagCompound planks = new NBTTagCompound();
                for (int i = 0; i < 6; i++) {
                    NBTTagCompound plank = new NBTTagCompound();
                    stack.writeToNBT(plank);
                    planks.setTag("Plank" + i, plank);
                }
                tag.setTag("Planks", planks);
                itemStack.setTagCompound(tag);
                items.add(itemStack);
                count++;
            }
        }
    }
}
