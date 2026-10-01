package hitscan.nostalgic.woodworks.entity;

import hitscan.nostalgic.woodworks.statues.EnumStatueType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;

public class EntityDollStatue extends EntityStrawStatue {
    public boolean doTranslation = true;

    public EntityDollStatue(World worldIn) {
        super(worldIn);
        this.setSize(0.6F * 0.5F, 1.95F * 0.5F);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(2F);
    }

    public EnumStatueType getStatueType() {
        return EnumStatueType.DOLL;
    }

    @Override
    public float getBodyTranslation() {
        return doTranslation ? this.getDataManager().get(BODY_TRANSLATION) : 0;
    }
}