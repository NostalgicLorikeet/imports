package hitscan.nostalgic.woodworks.gui;

import hitscan.nostalgic.woodworks.gui.containers.client.GuiContainerCrate;
import hitscan.nostalgic.woodworks.gui.containers.server.ContainerCrate;
import hitscan.nostalgic.woodworks.tileentities.TileEntityCrateWooden;
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
        }
        return null;
    }
}
