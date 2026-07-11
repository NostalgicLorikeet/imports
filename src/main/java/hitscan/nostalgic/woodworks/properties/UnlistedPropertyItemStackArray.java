package hitscan.nostalgic.woodworks.properties;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.lang.reflect.Array;
import java.util.List;

public class UnlistedPropertyItemStackArray implements IUnlistedProperty<List<ItemStack>> {
    public final String name;

    public UnlistedPropertyItemStackArray(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isValid(List<ItemStack> value) {
        return true;
    }

    @Override
    public Class<List<ItemStack>> getType() {
        return (Class<List<ItemStack>>) (Class<?>) List.class;
    }

    @Override
    public String valueToString(List<ItemStack> value) {
        return value.toString();
    }
}
