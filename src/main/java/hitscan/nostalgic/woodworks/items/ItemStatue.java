package hitscan.nostalgic.woodworks.items;

import hitscan.nostalgic.woodworks.entity.EntityStrawStatue;
import hitscan.nostalgic.woodworks.statues.EnumStatueType;
import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class ItemStatue extends Item {
    public ItemStatue() {
        this.setRegistryName("statue");
        this.setTranslationKey("statue");
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);

        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, (source, stack) -> {
            EnumFacing facing = source.getBlockState().getValue(BlockDispenser.FACING);
            spawnStatue(stack, source.getWorld(), source.getBlockPos().offset(facing), facing.getHorizontalAngle());
            stack.shrink(1);
            return stack;
        });
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            if (world.isAirBlock(pos.offset(facing))) {
                if (player.getHeldItem(hand).getItem() instanceof ItemStatue) {
                    List<EntityStrawStatue> items = world.getEntitiesWithinAABB(EntityStrawStatue.class, new AxisAlignedBB(pos.offset(facing)));
                    if (!items.isEmpty()) return EnumActionResult.FAIL;
                    spawnStatue(player.getHeldItem(hand), world, pos.offset(facing), player.rotationYaw + 180.0F);
                    if (!player.isCreative()) {
                        player.getHeldItem(hand).shrink(1);
                    }
                }
            }
        }
        return EnumActionResult.SUCCESS;
    }

    public void spawnStatue(ItemStack stack, World world, BlockPos pos, float rotationYaw) {
        int meta = stack.getItemDamage();
        EnumStatueType type = EnumStatueType.getStatueFromMeta(meta);
        EntityStrawStatue statue = type.get(world);
        statue.setPosition(pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F);
        if (stack.hasTagCompound()) {
            EntityStrawStatue.readStatueDataFromNBT(stack.getTagCompound(), statue);
            if (stack.hasDisplayName()) {
                statue.setCustomNameTag(stack.getDisplayName());
            }
        }
        world.spawnEntity(statue);
        statue.setFullBodyRotation(rotationYaw);
        world.playSound(
                null,
                pos.getX() + 0.5F,
                pos.getY() + 1,
                pos.getZ() + 0.5F,
                type.getPlacementSound(),
                SoundCategory.BLOCKS,
                0.5F,
                1.0F
        );
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            for (EnumStatueType type : EnumStatueType.values()) {
                items.add(new ItemStack(this, 1, type.getStatueMeta()));
            }
        }
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return "item." + EnumStatueType.getStatueFromMeta(stack.getMetadata()).getStatueName() + "_statue";
    }

    @Override
    public boolean hasEffect (ItemStack stack) {
        if (stack.hasTagCompound()) {
            NBTTagCompound tagCompound = stack.getTagCompound();
            return tagCompound.hasKey("PlayerUUID");
        }
        return false;
    }
}
