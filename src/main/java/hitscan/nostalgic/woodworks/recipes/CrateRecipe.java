package hitscan.nostalgic.woodworks.recipes;

import hitscan.nostalgic.woodworks.registry.WoodworksBlocks;
import hitscan.nostalgic.woodworks.registry.WoodworksItems;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class CrateRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        if (inv.getWidth() < 3 || inv.getHeight() < 3) {
            return false;
        }

        boolean row1 = isOreMatch(inv.getStackInSlot(0), "plankWood") &&
                isOreMatch(inv.getStackInSlot(1), "plankWood") &&
                isOreMatch(inv.getStackInSlot(2), "plankWood");

        boolean centerCobble = inv.getStackInSlot(4).getItem() == WoodworksItems.CRATE_FRAMING;

        boolean row3 = isOreMatch(inv.getStackInSlot(6), "plankWood") &&
                isOreMatch(inv.getStackInSlot(7), "plankWood") &&
                isOreMatch(inv.getStackInSlot(8), "plankWood");

        boolean sidesEmpty = inv.getStackInSlot(3).isEmpty() &&
                inv.getStackInSlot(5).isEmpty();

        return row1 && centerCobble && row3 && sidesEmpty;
    }

    private boolean isOreMatch(ItemStack stack, String oreName) {
        if (stack.isEmpty()) return false;

        int targetId = OreDictionary.getOreID(oreName);
        int[] stackIds = OreDictionary.getOreIDs(stack);

        for (int id : stackIds) {
            if (id == targetId && stack.getItem() instanceof ItemBlock) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack itemStack = new ItemStack(WoodworksBlocks.CRATE_WOODEN);
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagCompound planks = new NBTTagCompound();
        int[] slots = {0, 1, 2, 6, 7, 8};
        for (int i = 0; i < 6; i++) {
            NBTTagCompound plank = new NBTTagCompound();
            inv.getStackInSlot(slots[i]).writeToNBT(plank);
            planks.setTag("Plank" + i, plank);
        }
        tag.setTag("Planks", planks);
        itemStack.setTagCompound(tag);
        return itemStack;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width >= 3 && height >= 3;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }
}