package hitscan.nostalgic.woodworks.properties;

import net.minecraftforge.common.property.IUnlistedProperty;

public class UnlistedPropertyString implements IUnlistedProperty<String> {
    public final String name;

    public UnlistedPropertyString(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isValid(String value) {
        //pass in a predicate here if you ever have to use this ig
        return true;
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    public String valueToString(String value) {
        return value;
    }
}
