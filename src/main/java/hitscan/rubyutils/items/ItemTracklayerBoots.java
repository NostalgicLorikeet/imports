package hitscan.rubyutils.items;

import hitscan.rubyutils.config.RubyUtilsConfig;
import hitscan.rubyutils.init.RubyUtilsArmorMaterials;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemTracklayerBoots extends ItemArmor {
    public ItemTracklayerBoots(String name) {
        super(RubyUtilsArmorMaterials.TRACKLAYER_BOOTS, 1, EntityEquipmentSlot.FEET);
        this.setRegistryName(name);
        this.setTranslationKey(name);
    }

    @Override
    public void onArmorTick(World worldIn, EntityPlayer playerIn, ItemStack itemStackIn) {
        if (!worldIn.isRemote) {
            if (playerIn.isSneaking()) return;
            Item heldItem = playerIn.getHeldItemMainhand().getItem();
            BlockPos playerPosition = playerIn.getPosition();
            if (worldIn.getBlockState(playerPosition).getBlock() instanceof BlockRailBase) return;
            if (heldItem instanceof ItemBlock) {
                Block block = ((ItemBlock) heldItem).getBlock();
                if (block instanceof BlockRailBase) {
                    BlockPos blockPosPlaceOnTop = playerPosition.down();
                    if (block.canPlaceBlockOnSide(worldIn, blockPosPlaceOnTop,EnumFacing.UP)) {
                        heldItem.onItemUse(playerIn, worldIn, blockPosPlaceOnTop, EnumHand.MAIN_HAND, EnumFacing.UP, 0.5F, 0.5F, 0.5F);
                    }
                }
            }
        }
    }
}
