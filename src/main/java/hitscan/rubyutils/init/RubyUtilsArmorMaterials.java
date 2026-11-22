package hitscan.rubyutils.init;

import hitscan.rubyutils.Tags;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;

public class RubyUtilsArmorMaterials {
    public static final ItemArmor.ArmorMaterial TRACKLAYER_BOOTS = EnumHelper.addArmorMaterial(
            "TRACKLAYER_BOOTS",
            Tags.MOD_ID + ":tracklayer_boots",
            11,
            new int[]{1, 3, 4, 2},
            8,
    SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND,
            1
    );
}
