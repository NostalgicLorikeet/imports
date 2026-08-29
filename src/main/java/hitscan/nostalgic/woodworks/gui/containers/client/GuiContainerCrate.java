package hitscan.nostalgic.woodworks.gui.containers.client;

import hitscan.nostalgic.woodworks.Tags;
import hitscan.nostalgic.woodworks.gui.containers.server.ContainerCrate;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;

public class GuiContainerCrate extends GuiContainer {
    public static final ResourceLocation CHEST_GUI = new ResourceLocation(Tags.MOD_ID, "textures/gui/crate.png");

    public GuiContainerCrate (EntityPlayer player, IItemHandler handler) {
        super(new ContainerCrate(player, handler));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(CHEST_GUI);

        int guiLeft = (this.width - this.xSize) / 2;
        int guiTop = (this.height - this.ySize) / 2;

        drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, this.xSize, this.ySize, 256, 256);
    }
}
