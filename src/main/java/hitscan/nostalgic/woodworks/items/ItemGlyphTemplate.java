package hitscan.nostalgic.woodworks.items;

import hitscan.nostalgic.woodworks.registry.WoodworksItems;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.List;

public class ItemGlyphTemplate extends Item {
    public ItemGlyphTemplate() {
        this.setRegistryName("glyph_template");
        this.setTranslationKey("glyph_template");
        this.setHasSubtypes(true);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            items.add(new ItemStack(this));

            for (int i = 32; i < 127; i++) {
                if (i != ((int) ' ')) {
                    ItemStack glyph = new ItemStack(this);
                    NBTTagCompound tag = new NBTTagCompound();
                    tag.setInteger("Character", i);
                    //0 = ascii
                    //1 = unicode
                    //2 = sga
                    tag.setByte("Type", (byte) 0);
                    glyph.setTagCompound(tag);
                    items.add(glyph);
                }
            }
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return (!stack.hasTagCompound()) ? I18n.format("item.glyph_template_blank.name") : I18n.format("item.glyph_template.name");
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (stack.hasTagCompound()) {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag.hasKey("Character")) tooltip.add(String.valueOf((char) tag.getInteger("Character")));
            if (tag.hasKey("Type")) {
                int type = tag.getByte("Type");
                tooltip.add(type == 0 ? "ASCII" : (type == 1 ? "Unicode" : "SGA"));
            }
        } else {
            tooltip.add(I18n.format("item.glyph_template.tooltip.blank"));
        }
    }
}
