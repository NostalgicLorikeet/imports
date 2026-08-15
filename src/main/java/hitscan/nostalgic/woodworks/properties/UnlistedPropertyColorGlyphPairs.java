package hitscan.nostalgic.woodworks.properties;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.List;

public class UnlistedPropertyColorGlyphPairs implements IUnlistedProperty<int[][]> {
    public final String name;

    public UnlistedPropertyColorGlyphPairs(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isValid(int[][] value) {
        return value.length == 16;
    }

    @Override
    public Class<int[][]> getType() {
        return int[][].class;
    }

    @Override
    public String valueToString(int[][] value) {
        return value.toString();
    }
}
