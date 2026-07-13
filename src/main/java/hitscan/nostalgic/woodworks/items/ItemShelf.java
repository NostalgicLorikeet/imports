package hitscan.nostalgic.woodworks.items;

import hitscan.nostalgic.woodworks.registry.WoodworksItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemShelf extends ItemBlock {
    public static final HashMap<String, String> TOOL_TIP_TYPE_CACHE = new HashMap<>();

    public ItemShelf(Block block) {
        super(block);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            ArrayList<ItemStack> stacks = new ArrayList<>();

            for (BlockPlanks.EnumType wood : BlockPlanks.EnumType.values()) {
                ItemStack plankStack = new ItemStack(Blocks.PLANKS);
                ItemStack logStack = new ItemStack((Blocks.LOG));

                plankStack.setItemDamage(wood.getMetadata());
                logStack.setItemDamage(wood.getMetadata());

                stacks.add(plankStack);
                stacks.add(logStack);
            }

            ArrayList<ItemStack> oreDictStacks = new ArrayList<>();
            oreDictStacks.addAll(OreDictionary.getOres("logWood"));
            oreDictStacks.addAll(OreDictionary.getOres("plankWood"));

            for (ItemStack stack : oreDictStacks) {
                if (!stack.getItem().getRegistryName().getNamespace().equals("minecraft")) stacks.add(stack);
            }

            for (ItemStack woodStack : stacks) {
                ItemStack stack = new ItemStack(this.getBlock());
                NBTTagCompound tag = new NBTTagCompound();
                NBTTagCompound texture = new NBTTagCompound();
                texture.setString("id", woodStack.getItem().getRegistryName().toString());
                texture.setInteger("Damage", woodStack.getItemDamage());
                tag.setTag("Texture", texture);
                stack.setTagCompound(tag);
                items.add(stack);
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (stack.hasTagCompound()) {
            NBTTagCompound tag = stack.getTagCompound();

            if (tag.hasKey("Texture")) {
                NBTTagCompound texture = tag.getCompoundTag("Texture");
                String id = texture.getString("id");
                int damage = texture.getInteger("Damage");
                String combination = id + "-" + damage;
                String type;

                if (TOOL_TIP_TYPE_CACHE.containsKey(combination)) {
                    type = TOOL_TIP_TYPE_CACHE.get(combination);
                } else {
                    ItemStack itemStack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(id)));
                    itemStack.setItemDamage(damage);
                    type = itemStack.getDisplayName();
                    TOOL_TIP_TYPE_CACHE.put(combination, type);
                }

                tooltip.add(type);
            }

            if (stack.getItem() == WoodworksItems.SHELF_DISPLAY || stack.getItem() == WoodworksItems.SHELF_DISPLAY_CHAIN) {
                tooltip.add(new TextComponentTranslation("tile.display_shelf.tooltip.one").getFormattedText());
                tooltip.add(new TextComponentTranslation("tile.display_shelf.tooltip.two").getFormattedText());
                tooltip.add(new TextComponentTranslation("tile.display_shelf.tooltip.three").getFormattedText());
            } else {
                tooltip.add(new TextComponentTranslation("tile.shelf.tooltip").getFormattedText());
            }
        }
    }
}
