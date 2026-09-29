package hitscan.nostalgic.woodworks.statues;

import hitscan.nostalgic.woodworks.entity.EntityDollStatue;
import hitscan.nostalgic.woodworks.entity.EntityStoneStatue;
import hitscan.nostalgic.woodworks.entity.EntityStrawStatue;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public enum EnumStatueType {
    STRAW("straw", 0, SoundEvents.BLOCK_CLOTH_PLACE, true),
    STONE("stone", 1, SoundEvents.BLOCK_STONE_PLACE),
    DOLL("doll", 2, SoundEvents.BLOCK_CLOTH_PLACE, true);

    final String name;
    final int meta;
    final SoundEvent placementSound;
    final boolean isStraw;

    EnumStatueType(String name, int meta, SoundEvent placementSound) {
        this(name, meta, placementSound, false);
    }

    EnumStatueType(String name, int meta, SoundEvent placementSound, boolean isStraw) {
        this.name = name;
        this.meta = meta;
        this.placementSound = placementSound;
        this.isStraw = isStraw;
    }

    public String getStatueName() {
        return name;
    }

    public int getStatueMeta() {
        return meta;
    }

    public SoundEvent getPlacementSound() {
        return placementSound;
    }

    public boolean isStraw() {
        return isStraw;
    }

    //hardcoded here cuz pain
    public EntityStrawStatue get(World worldIn) {
        switch(meta) {
            case 0:
                return new EntityStrawStatue(worldIn);
            case 1:
                return new EntityStoneStatue(worldIn);
            case 2:
                return new EntityDollStatue(worldIn);
            default:
                return new EntityStrawStatue(worldIn);
        }
    }

    public static EnumStatueType getStatueFromMeta(int metaIn) {
        for (EnumStatueType type : EnumStatueType.values()) {
            if (type.getStatueMeta() == metaIn) {
                return type;
            }
        }
        return STRAW;
    }
}
