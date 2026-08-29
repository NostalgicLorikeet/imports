package hitscan.nostalgic.woodworks.tileentities;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityCrateWooden extends AbstractStorageBlockTileEntity {
    ItemStack[] planks = new ItemStack[] {
        new ItemStack(Blocks.PLANKS),
                new ItemStack(Blocks.PLANKS),
                new ItemStack(Blocks.PLANKS),
                new ItemStack(Blocks.PLANKS),
                new ItemStack(Blocks.PLANKS),
                new ItemStack(Blocks.PLANKS)
    };

    @Override
    public int stackCount() {
        return 9;
    }

    public void setPlank(int i, ItemStack stack) {
        planks[i] = stack;
    }

    public ItemStack getPlank(int i) {
        return planks[i];
    }

    public ItemStack[] getPlanks() {
        return planks;
    }

    @Override
    public void deserializeFromNBT(NBTTagCompound compound) {
        for (int i = 0; i < 6; i++) {
            if (compound.hasKey("Planks")) {
                NBTTagCompound planks = compound.getCompoundTag("Planks");
                if (planks.hasKey("Plank" + i)) {
                    setPlank(i, new ItemStack(planks.getCompoundTag("Plank" + i)));
                }
            } else {
                setPlank(i, new ItemStack(Blocks.PLANKS));
            }
        }
    }

    @Override
    public void serializeNBT(NBTTagCompound compound) {
        NBTTagCompound planks = new NBTTagCompound();

        for (int i = 0; i < 6; i++) {
            NBTTagCompound plank = new NBTTagCompound();
            if (getPlank(i) != null) {
                if (!getPlank(i).isEmpty()) {
                    getPlank(i).writeToNBT(plank);
                } else {
                    new ItemStack(Blocks.PLANKS).writeToNBT(plank);
                }
            } else {
                new ItemStack(Blocks.PLANKS).writeToNBT(plank);
            }
            planks.setTag("Plank" + i, plank);
        }

        compound.setTag("Planks", planks);
    }
}
