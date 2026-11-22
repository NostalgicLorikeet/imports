package hitscan.rubyutils.gui;

import hitscan.rubyutils.entity.EntityStrawStatue;
import hitscan.rubyutils.gui.containers.client.GuiContainerCrate;
import hitscan.rubyutils.gui.containers.client.GuiContainerStatue;
import hitscan.rubyutils.gui.containers.server.ContainerCrate;
import hitscan.rubyutils.gui.containers.server.ContainerStatue;
import hitscan.rubyutils.tileentities.TileEntityCrateWooden;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class RubyUtilsGUIHandler implements IGuiHandler {
    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == RubyUtilsGUIs.CRATE) {
            TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
            if (te instanceof TileEntityCrateWooden) {
                TileEntityCrateWooden crate = (TileEntityCrateWooden) te;
                IItemHandler handler = crate.getStackHandler();

                if (handler != null) {
                    return new ContainerCrate(player, handler);
                } else {
                    return null;
                }
            }
        } else if (ID == RubyUtilsGUIs.STATUE) {
            Entity entity = world.getEntityByID(x);
            if (entity instanceof EntityStrawStatue) {
                EntityStrawStatue entityStrawStatue = (EntityStrawStatue) entity;
                return new ContainerStatue(player.inventory, entityStrawStatue);
            }
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == RubyUtilsGUIs.CRATE) {
            TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
            if (te instanceof TileEntityCrateWooden) {
                TileEntityCrateWooden crate = (TileEntityCrateWooden) te;
                IItemHandler handler = crate.getStackHandler();

                if (handler != null) {
                    return new GuiContainerCrate(player, handler);
                } else {
                    return null;
                }
            }
        } else if (ID == RubyUtilsGUIs.STATUE) {
            Entity entity = world.getEntityByID(x);
            if (entity instanceof EntityStrawStatue) {
                EntityStrawStatue entityStrawStatue = (EntityStrawStatue) entity;
                return new GuiContainerStatue(player.inventory, entityStrawStatue);
            }
        }
        return null;
    }
}
