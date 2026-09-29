package hitscan.nostalgic.woodworks.entity;

import com.google.common.base.Optional;
import hitscan.nostalgic.woodworks.registry.WoodworksItems;
import hitscan.nostalgic.woodworks.statues.EnumStatueType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public class EntityStrawStatue extends EntityLivingBase {
    private static final DataParameter<Optional<UUID>> SKIN_UUID = EntityDataManager.createKey(EntityStrawStatue.class, DataSerializers.OPTIONAL_UNIQUE_ID);

    private static final DataParameter<Float> ROTATION_FULL_BODY = EntityDataManager.createKey(EntityStrawStatue.class, DataSerializers.FLOAT);
    private static final Map<Part, Map<Axis, DataParameter<Float>>> ROTATION_DATA_PARAMETER_MAP = new EnumMap<>(Part.class);

    private final NonNullList<ItemStack> armor = NonNullList.withSize(4, ItemStack.EMPTY);
    private final NonNullList<ItemStack> hands = NonNullList.withSize(2, ItemStack.EMPTY);

    public EntityStrawStatue(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.95F);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        if (this.getPlayerUUID() != null) compound.setString("PlayerUUID", this.getPlayerUUID().toString());
        compound.setFloat("RotationFullBody", this.getFullBodyRotation());

        NBTTagList nbttaglist = new NBTTagList();

        for (ItemStack itemstack : this.armor) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();

            if (!itemstack.isEmpty())
            {
                itemstack.writeToNBT(nbttagcompound);
            }

            nbttaglist.appendTag(nbttagcompound);
        }

        compound.setTag("ArmorItems", nbttaglist);

        NBTTagList nbttaglist1 = new NBTTagList();

        for (ItemStack itemstack1 : this.hands) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();

            if (!itemstack1.isEmpty())
            {
                itemstack1.writeToNBT(nbttagcompound1);
            }

            nbttaglist1.appendTag(nbttagcompound1);
        }

        compound.setTag("HandItems", nbttaglist1);

        for (Part part : Part.values()) {
            for (Axis axis : Axis.values()) {
                compound.setFloat(getPartAxisStorageName(part, axis), getLimbRotation(part, axis));
            }
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("PlayerUUID")) {
            this.setPlayerUUID(compound.getString("PlayerUUID"));
        }
        if (compound.hasKey("RotationFullBody")) {
            this.setFullBodyRotation(compound.getFloat("RotationFullBody"));
        }

        if (compound.hasKey("ArmorItems", 9))
        {
            NBTTagList nbttaglist = compound.getTagList("ArmorItems", 10);

            for (int i = 0; i < this.armor.size(); ++i)
            {
                this.armor.set(i, new ItemStack(nbttaglist.getCompoundTagAt(i)));
            }
        }

        if (compound.hasKey("HandItems", 9))
        {
            NBTTagList nbttaglist1 = compound.getTagList("HandItems", 10);

            for (int j = 0; j < this.hands.size(); ++j)
            {
                this.hands.set(j, new ItemStack(nbttaglist1.getCompoundTagAt(j)));
            }
        }

        for (Part part : Part.values()) {
            for (Axis axis : Axis.values()) {
                if (compound.hasKey(getPartAxisStorageName(part, axis))) {
                    setLimbRotation(part, axis, compound.getFloat(getPartAxisStorageName(part, axis)));
                }
            }
        }
    }

    public void setPlayerUUID(String id) {
        this.dataManager.set(SKIN_UUID, Optional.of(UUID.fromString(id)));
    }

    public UUID getPlayerUUID() {
        Optional<UUID> uuidOptional = this.dataManager.get(SKIN_UUID);
        return uuidOptional.isPresent() ? uuidOptional.get() : null;
    }

    public float getFullBodyRotation() {
        return this.getDataManager().get(ROTATION_FULL_BODY);
    }

    public void setFullBodyRotation(float rot) {
        this.getDataManager().set(ROTATION_FULL_BODY, rot);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(SKIN_UUID, Optional.absent());
        this.dataManager.register(ROTATION_FULL_BODY, 0.0F);
        for (Part part : Part.values()) {
            for (Axis axis : Axis.values()) {
                this.dataManager.register(ROTATION_DATA_PARAMETER_MAP.get(part).get(axis), 0.0F);
            }
        }
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        if (!world.isRemote) {
            if (hand == EnumHand.OFF_HAND && !player.getHeldItemMainhand().isEmpty())
                return super.processInitialInteract(player, hand);
            ItemStack itemStack = player.getHeldItem(hand);
            EntityEquipmentSlot slot;
            if (itemStack.isEmpty()) {
                slot = EntityEquipmentSlot.MAINHAND;
                if (hand == EnumHand.OFF_HAND) return super.processInitialInteract(player, hand);
            } else {
                slot = hand == EnumHand.MAIN_HAND ? EntityEquipmentSlot.MAINHAND : EntityEquipmentSlot.OFFHAND;
            }
            if (itemStack.getItem() instanceof ItemArmor) {
                ItemArmor itemArmor = (ItemArmor) itemStack.getItem();

                slot = itemArmor.armorType;
            }

            ItemStack itemStackOld = this.getItemStackFromSlot(slot);
            ItemStack itemStackCopy = itemStack.copy();
            itemStackCopy.setCount(1);

            this.setItemStackToSlot(slot, itemStackCopy);
            if (!itemStack.isEmpty()) player.getHeldItem(hand).shrink(1);

            if (!itemStackOld.isEmpty()) {
                player.addItemStackToInventory(itemStackOld);
            }
        }

        return super.processInitialInteract(player, hand);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.motionX = 0.0D;
        this.motionZ = 0.0D;
    }

    public boolean canBeHurtByMeleeItem(ItemStack item) {
        return (item.getItem() instanceof ItemSword || item.getItem() instanceof ItemAxe);
    }

    public boolean canBeHurtByProjectile() {
        return true;
    }

    public boolean canBeHurtByExplosion() {
        return true;
    }

    public boolean canBeHurt(DamageSource source) {
        if (source.isProjectile()) {
            return canBeHurtByProjectile();
        }

        if (source.getTrueSource() instanceof EntityLivingBase) {
            EntityLivingBase attacker = (EntityLivingBase) source.getTrueSource();
            return this.canBeHurtByMeleeItem(attacker.getHeldItemMainhand());
        }

        if (source.isExplosion()) {
            return canBeHurtByExplosion();
        }

        return false;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.canHarmInCreative()) {
            return super.attackEntityFrom(source, amount);
        }

        if (this.canBeHurt(source)) {
            return super.attackEntityFrom(source, amount);
        }

        return false;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5F);
    }

    @Override
    public boolean canBeHitWithPotion()
    {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox() {
        return null;
    }

    @Override
    public AxisAlignedBB getCollisionBox(Entity entityIn) {
        return null;
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        super.fall(0, 0);
    }

    @Override
    public void knockBack(Entity entityIn, float strength, double xRatio, double zRatio) {}

    @Override
    public void handleStatusUpdate(byte id) {
        if (id == 2) {

        } else {
            super.handleStatusUpdate(id);
        }
    }

    @Override
    public void applyEntityCollision(Entity entityIn) {}

    @Override
    public boolean canBePushed() {
        return false;
    }

    public IBlockState getDeathParticles() {
        return Blocks.HAY_BLOCK.getDefaultState();
    }

    public EnumStatueType getStatueType() {
        return EnumStatueType.STRAW;
    }

    public ItemStack getSelfAsItem(boolean saveData) {
        ItemStack itemStack = new ItemStack(WoodworksItems.STATUE, 1, getStatueType().getStatueMeta());

        if (saveData) {
            
        }

        return itemStack;
    }

    @Override
    protected void onDeathUpdate() {
        if (!world.isRemote) {
            ((WorldServer)this.world).spawnParticle(
                    EnumParticleTypes.BLOCK_DUST,
                    this.posX,
                    this.posY + (double)this.height / 1.5D,
                    this.posZ,
                    10,
                    (double)(this.width / 4.0F),
                    (double)(this.height / 4.0F),
                    (double)(this.width / 4.0F),
                    0.05D,
                    Block.getStateId(getDeathParticles())
            );
        }
        this.setDead();
        Block.spawnAsEntity(world, getPosition(), this.getSelfAsItem(true));

        for (ItemStack itemstack : this.armor) {
            if (!itemstack.isEmpty())
            {
                Block.spawnAsEntity(world, getPosition(), itemstack);
            }
        }

        for (ItemStack itemstack : this.hands) {
            if (!itemstack.isEmpty())
            {
                Block.spawnAsEntity(world, getPosition(), itemstack);
            }
        }
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return this.getSelfAsItem(false);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_ARMORSTAND_HIT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BLOCK_CLOTH_BREAK;
    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return armor;
    }

    @Override
    public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
        return slotIn.getSlotType() == EntityEquipmentSlot.Type.HAND ? this.hands.get(slotIn.getIndex()) : this.armor.get(slotIn.getIndex());
    }

    @Override
    public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
        if (slotIn.getSlotType() == EntityEquipmentSlot.Type.HAND) this.hands.set(slotIn.getIndex(), stack);
        else this.armor.set(slotIn.getIndex(), stack);
    }

    @Override
    public EnumHandSide getPrimaryHand() {
        return EnumHandSide.RIGHT;
    }

    static {
        for (Part part : Part.values()) {
            Map<Axis, DataParameter<Float>> axisMap = new EnumMap<>(Axis.class);
            for (Axis axis : Axis.values()) {
                axisMap.put(axis, EntityDataManager.createKey(EntityStrawStatue.class, DataSerializers.FLOAT));
            }
            ROTATION_DATA_PARAMETER_MAP.put(part, axisMap);
        }
    }

    public static String getPartAxisStorageName(Part part, Axis axis) {
        return part.getName() + axis.getName();
    }

    public void setLimbRotation(Part part, Axis axis, float rotation) {
        this.getDataManager().set(ROTATION_DATA_PARAMETER_MAP.get(part).get(axis), rotation);
    }

    public float getLimbRotation(Part part, Axis axis) {
        return this.getDataManager().get(ROTATION_DATA_PARAMETER_MAP.get(part).get(axis));
    }

    public enum Part {
        HEAD("Head"),
        BODY("Body"),
        ARM_LEFT("ArmLeft"),
        ARM_RIGHT("ArmRight"),
        LEG_LEFT("LegLeft"),
        LEG_RIGHT("LegRight");

        final String name;

        Part(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum Axis {
        X("X"),
        Y("Y"),
        Z("Z");

        final String name;

        Axis(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }
    }
}
