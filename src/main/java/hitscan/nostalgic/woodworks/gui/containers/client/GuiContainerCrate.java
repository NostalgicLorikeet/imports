package hitscan.nostalgic.woodworks.gui.containers.client;

import hitscan.nostalgic.woodworks.Tags;
import hitscan.nostalgic.woodworks.gui.containers.server.ContainerCrate;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.items.IItemHandler;

public class GuiContainerCrate extends GuiContainer {
    public static final ResourceLocation CHEST_GUI = new ResourceLocation(Tags.MOD_ID, "textures/gui/crate.png");
    public static final TextComponentTranslation CHEST_NAME = new TextComponentTranslation("tile.crate.name");

    public GuiContainerCrate (EntityPlayer player, IItemHandler handler) {
        super(new ContainerCrate(player, handler));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.fontRenderer.drawString(CHEST_NAME.getFormattedText(),
                (this.width - this.xSize) / 2 + 7, (this.height - this.ySize) / 2 + 6,
                0x404040);
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();

        this.renderHoveredToolTip(mouseX, mouseY);
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
