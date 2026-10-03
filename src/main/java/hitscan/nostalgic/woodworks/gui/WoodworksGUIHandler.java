package hitscan.nostalgic.woodworks.gui;

import hitscan.nostalgic.woodworks.entity.EntityStrawStatue;
import hitscan.nostalgic.woodworks.gui.containers.client.GuiContainerCrate;
import hitscan.nostalgic.woodworks.gui.containers.client.GuiContainerStatue;
import hitscan.nostalgic.woodworks.gui.containers.server.ContainerCrate;
import hitscan.nostalgic.woodworks.gui.containers.server.ContainerStatue;
import hitscan.nostalgic.woodworks.tileentities.TileEntityCrateWooden;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class WoodworksGUIHandler implements IGuiHandler {
    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == WoodworksGUIs.CRATE) {
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
        } else if (ID == WoodworksGUIs.STATUE) {
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
        if (ID == WoodworksGUIs.CRATE) {
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
        } else if (ID == WoodworksGUIs.STATUE) {
            Entity entity = world.getEntityByID(x);
            if (entity instanceof EntityStrawStatue) {
                EntityStrawStatue entityStrawStatue = (EntityStrawStatue) entity;
                return new GuiContainerStatue(player.inventory, entityStrawStatue);
            }
        }
        return null;
    }
}
