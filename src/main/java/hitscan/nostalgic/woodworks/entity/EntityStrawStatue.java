package hitscan.nostalgic.woodworks.entity;

import com.google.common.base.Optional;
import com.mojang.authlib.GameProfile;
import hitscan.nostalgic.woodworks.Tags;
import hitscan.nostalgic.woodworks.gui.WoodworksGUIs;
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
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EntityStrawStatue extends EntityLivingBase {
    protected static final DataParameter<Optional<UUID>> SKIN_UUID = EntityDataManager.createKey(EntityStrawStatue.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    protected static final DataParameter<String> SKIN_NAME = EntityDataManager.createKey(EntityStrawStatue.class, DataSerializers.STRING);
    protected static final DataParameter<Float> BODY_TRANSLATION = EntityDataManager.createKey(EntityStrawStatue.class, DataSerializers.FLOAT);
    protected static final DataParameter<Float> BODY_ROTATION = EntityDataManager.createKey(EntityStrawStatue.class, DataSerializers.FLOAT);
    protected static final DataParameter<Boolean> LOCK_LEGS_TO_BODY = EntityDataManager.createKey(EntityStrawStatue.class, DataSerializers.BOOLEAN);
    protected static final Map<Part, Map<Axis, DataParameter<Float>>> ROTATION_DATA_PARAMETER_MAP = new EnumMap<>(Part.class);

    String lastKnownUsername = "";
    public boolean isGUIOpen = false;
    public boolean doRotation = true;
    private static final HashMap<String, GameProfile> USERNAME_TO_GAME_PROFILE = new HashMap<>();

    protected NonNullList<ItemStack> armor = NonNullList.withSize(4, ItemStack.EMPTY);
    protected NonNullList<ItemStack> hands = NonNullList.withSize(2, ItemStack.EMPTY);

    public EntityStrawStatue(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.95F);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);

        compound.setFloat("BodyRotation", this.getFullBodyRotation());

        NBTTagList nbttaglist = new NBTTagList();

        for (ItemStack itemstack : this.armor) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();

            if (!itemstack.isEmpty()) {
                itemstack.writeToNBT(nbttagcompound);
            }

            nbttaglist.appendTag(nbttagcompound);
        }

        compound.setTag("ArmorItems", nbttaglist);

        NBTTagList nbttaglist1 = new NBTTagList();

        for (ItemStack itemstack1 : this.hands) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();

            if (!itemstack1.isEmpty()) {
                itemstack1.writeToNBT(nbttagcompound1);
            }

            nbttaglist1.appendTag(nbttagcompound1);
        }

        compound.setTag("HandItems", nbttaglist1);
        writeStatueDataToNBT(compound, this);
    }

    public static void writeStatueDataToNBT(NBTTagCompound compound, EntityStrawStatue statue) {
        if (!statue.getPlayerName().isEmpty()) compound.setString("PlayerName", statue.getPlayerName());
        if (statue.getPlayerUUID() != null) compound.setString("PlayerUUID", statue.getPlayerUUID().toString());
        compound.setFloat("BodyTranslation", statue.getBodyTranslation());
        compound.setBoolean("LegsLockedToBody", statue.areLegsLockedToBody());

        for (Part part : Part.values()) {
            for (Axis axis : Axis.values()) {
                compound.setFloat(getPartAxisStorageName(part, axis), statue.getLimbRotation(part, axis));
            }
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);

        if (compound.hasKey("BodyRotation")) {
            this.setFullBodyRotation(compound.getFloat("BodyRotation"));
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

        readStatueDataFromNBT(compound, this);
    }

    public static void readStatueDataFromNBT (NBTTagCompound compound, EntityStrawStatue statue) {
        if (compound.hasKey("PlayerName")) {
            statue.setPlayerName(compound.getString("PlayerName"));
            statue.lastKnownUsername = compound.getString("PlayerName");
        }

        if (compound.hasKey("PlayerUUID")) {
            statue.setPlayerUUID(UUID.fromString(compound.getString("PlayerUUID")));
        }

        if (compound.hasKey("BodyTranslation")) {
            statue.setBodyTranslation(compound.getFloat("BodyTranslation"));
        }

        if (compound.hasKey("LegsLockedToBody")) {
            statue.setLegsLockedToBody(compound.getBoolean("LegsLockedToBody"));
        }

        for (Part part : Part.values()) {
            for (Axis axis : Axis.values()) {
                if (compound.hasKey(getPartAxisStorageName(part, axis))) {
                    statue.setLimbRotation(part, axis, compound.getFloat(getPartAxisStorageName(part, axis)));
                }
            }
        }
    }

    public String getPlayerName() {
        return this.getDataManager().get(SKIN_NAME);
    }

    public void setPlayerName(String name) {
        this.getDataManager().set(SKIN_NAME, name);
        if (name.isEmpty()) this.setPlayerUUID(null);
    }

    public UUID getPlayerUUID() {
        return this.getDataManager().get(SKIN_UUID).isPresent() ? this.getDataManager().get(SKIN_UUID).get() : null;
    }

    public void setPlayerUUID(@Nullable UUID uuid) {
        if (uuid != null) {
            this.getDataManager().set(SKIN_UUID, Optional.of(uuid));
        } else {
            this.getDataManager().set(SKIN_UUID, Optional.absent());
        }
    }

    public void updatePlayerInformation() {
        if (!world.isRemote) {
            if (!this.getPlayerName().isEmpty()) {
                GameProfile profile;
                if (this.getPlayerName().isEmpty() || !USERNAME_TO_GAME_PROFILE.containsKey(this.getPlayerName())) {
                    profile = null;
                    Thread thread = new Thread(() -> {
                        GameProfile profile1 = new GameProfile(this.getPlayerUUID(), this.getPlayerName());
                        profile1 = TileEntitySkull.updateGameProfile(profile1);
                        USERNAME_TO_GAME_PROFILE.put(profile1.getName(), profile1);
                        if (this.world.getMinecraftServer() != null) {
                            this.world.getMinecraftServer().addScheduledTask(this::updatePlayerInformation);
                        }
                    });
                    thread.start();
                } else {
                    profile = USERNAME_TO_GAME_PROFILE.getOrDefault(this.getPlayerName(), null);
                }
                if (profile != null) {
                    this.setPlayerUUID(profile.getId());
                }
            }
            lastKnownUsername = this.getPlayerName();
        }
    }

    public void resetPlayerInfo() {
        this.setPlayerUUID(null);
        this.setPlayerName("");
    }

    public float getFullBodyRotation() {
        return this.getDataManager().get(BODY_ROTATION);
    }

    public void setFullBodyRotation(float rot) {
        this.rotationYaw = rot;
        this.getDataManager().set(BODY_ROTATION, rot);
    }

    public float getBodyTranslation() {
        return this.getDataManager().get(BODY_TRANSLATION);
    }

    public void setBodyTranslation(float bodyTranslation) {
        this.getDataManager().set(BODY_TRANSLATION, bodyTranslation);
    }

    public void setLegsLockedToBody(boolean legsLockedToBody) {
        this.dataManager.set(LOCK_LEGS_TO_BODY, legsLockedToBody);
    }

    public boolean areLegsLockedToBody() {
        return this.dataManager.get(LOCK_LEGS_TO_BODY);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(SKIN_UUID, Optional.absent());
        this.dataManager.register(SKIN_NAME, "");
        this.dataManager.register(BODY_TRANSLATION, 0.0F);
        this.dataManager.register(BODY_ROTATION, 0.0F);
        this.dataManager.register(LOCK_LEGS_TO_BODY, false);
        for (Part part : Part.values()) {
            for (Axis axis : Axis.values()) {
                this.dataManager.register(ROTATION_DATA_PARAMETER_MAP.get(part).get(axis), 0.0F);
            }
        }
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        if (!world.isRemote) {
            if (!isGUIOpen) {
                if (!player.isSneaking()) {
                    if (hand == EnumHand.OFF_HAND) return super.processInitialInteract(player, hand);
                    ItemStack itemStack = player.getHeldItem(hand);
                    EntityEquipmentSlot slot = EntityEquipmentSlot.MAINHAND;
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
                        if (!(itemStackOld.getItem() instanceof ItemArmor)) {
                            player.addItemStackToInventory(itemStackOld);
                        } else {
                            if (itemStack.isEmpty()) {
                                player.setHeldItem(EnumHand.MAIN_HAND, itemStackOld);
                            } else {
                                Block.spawnAsEntity(this.world, player.getPosition(), itemStackOld);
                            }
                        }
                    }
                } else {
                    player.openGui(Tags.MOD_ID, WoodworksGUIs.STATUE, this.world, this.getEntityId(), 0, 0);
                    isGUIOpen = true;
                }
            }
        }

        return super.processInitialInteract(player, hand);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.motionX = 0.0D;
        this.motionZ = 0.0D;
        if (!this.world.isRemote) {
            if (!this.getPlayerName().equals(lastKnownUsername)) {
                this.updatePlayerInformation();
            }
        }
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

        return true;
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
    public boolean canBeHitWithPotion() {
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
            NBTTagCompound statueData = new NBTTagCompound();
            writeStatueDataToNBT(statueData, this);
            if (this.hasCustomName()) {
                NBTTagCompound displayData = new NBTTagCompound();
                displayData.setString("Name", this.getCustomNameTag());
                statueData.setTag("display", displayData);
            }
            itemStack.setTagCompound(statueData);
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
        HEAD("Head", false),
        BODY("Body", false),
        ARM_LEFT("ArmLeft", false),
        ARM_RIGHT("ArmRight", false),
        LEG_LEFT("LegLeft", true),
        LEG_RIGHT("LegRight", true);

        final String name;
        final boolean leg;

        Part(String name, boolean leg) {
            this.name = name;
            this.leg = leg;
        }

        public String getName() {
            return name;
        }

        public boolean isLeg() {
            return leg;
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

    //do not actually use these it prolly wont actually do anything bad just dont
    public void setHands(NonNullList<ItemStack> hands) {
        this.hands = hands;
    }

    public void setArmor(NonNullList<ItemStack> armor) {
        this.armor = armor;
    }

    public NonNullList<ItemStack> getArmor() {
        return armor;
    }

    public NonNullList<ItemStack> getHands() {
        return hands;
    }
}