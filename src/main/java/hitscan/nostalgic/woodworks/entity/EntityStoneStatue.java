package hitscan.nostalgic.woodworks.entity;

import hitscan.nostalgic.woodworks.statues.EnumStatueType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityStoneStatue extends EntityStrawStatue {
    public EntityStoneStatue(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20F);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.BLOCK_STONE_PLACE;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BLOCK_STONE_BREAK;
    }

    @Override
    public IBlockState getDeathParticles() {
        return Blocks.STONE.getDefaultState();
    }

    @Override
    public boolean canBeHurtByMeleeItem(ItemStack item) {
        return (item.getItem() instanceof ItemPickaxe);
    }

    @Override
    public boolean canBeHurtByProjectile() {
        return false;
    }

    public EnumStatueType getStatueType() {
        return EnumStatueType.STONE;
    }
}
