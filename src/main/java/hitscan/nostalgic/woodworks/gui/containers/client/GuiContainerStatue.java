package hitscan.nostalgic.woodworks.gui.containers.client;

import hitscan.nostalgic.woodworks.Woodworks;
import hitscan.nostalgic.woodworks.entity.EntityDollStatue;
import hitscan.nostalgic.woodworks.entity.EntityStrawStatue;
import hitscan.nostalgic.woodworks.gui.containers.server.ContainerStatue;
import hitscan.nostalgic.woodworks.message.MessageStatueUpdateSkin;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.client.config.GuiSlider;

import java.io.IOException;

public class GuiContainerStatue extends GuiContainer {
    private final EntityStrawStatue statue;
    private final EntityStrawStatue statueForGUIRendering;
    String lastSentName;
    private GuiTextField textField;
    private GuiSlider viewAngleSlider;

    public GuiContainerStatue(InventoryPlayer inventoryPlayer, EntityStrawStatue statue) {
        super(new ContainerStatue(inventoryPlayer, statue));
        this.statue = statue;
        this.statueForGUIRendering = this.statue instanceof EntityDollStatue ? new EntityDollStatue(statue.world) : new EntityStrawStatue(statue.world);
        this.lastSentName = statue.getPlayerName();
    }

    @Override
    public void initGui() {
        super.initGui();
        this.mc.player.openContainer = this.inventorySlots;
        this.textField = new GuiTextField(0, this.fontRenderer, this.guiLeft + 20, this.guiTop - 40, 135, 20);
        this.textField.setMaxStringLength(16);
        this.textField.setFocused(true);
        this.textField.setText(statue.getPlayerName());
        this.viewAngleSlider = new GuiSlider(0, this.guiLeft - 88, this.guiTop + 170, 100, 10, "Angle: ", "", 0, 360, 0, true, false, null);
        this.buttonList.add(this.viewAngleSlider);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawDefaultBackground();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.textField.drawTextBox();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableLighting();
        GlStateManager.enableBlend();
        if (this.statue != null) {
            int posX = this.guiLeft - 88;
            int posY = this.guiTop + 120;
            int scale = 50;
            statueForGUIRendering.setFullBodyRotation((float) viewAngleSlider.sliderValue * 360F);
            GuiInventory.drawEntityOnScreen(posX, posY, scale, 0F, 0F, this.statueForGUIRendering);
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        this.textField.updateCursorCounter();
        statueForGUIRendering.setPlayerName(statue.getPlayerName());
        statueForGUIRendering.setPlayerUUID(statue.getPlayerUUID());
        statueForGUIRendering.setArmor(statue.getArmor());
        statueForGUIRendering.setHands(statue.getHands());
        statueForGUIRendering.setLegsLockedToBody(statue.areLegsLockedToBody());
        for (EntityStrawStatue.Part part : EntityStrawStatue.Part.values()) {
            for (EntityStrawStatue.Axis axis : EntityStrawStatue.Axis.values()) {
                statueForGUIRendering.setLimbRotation(part, axis, statue.getLimbRotation(part, axis));
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (this.textField.textboxKeyTyped(typedChar, keyCode)) {
            return;
        }

        if (keyCode == 28 || keyCode == 156) {
            submitNameToServer();
            this.textField.setFocused(false);
            return;
        }

        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        boolean wasFocused = this.textField.isFocused();
        boolean clickedInside = mouseX >= this.textField.x
                && mouseX < this.textField.x + this.textField.width
                && mouseY >= this.textField.y
                && mouseY < this.textField.y + this.textField.height;
        this.textField.setFocused(clickedInside);
        if (wasFocused && !this.textField.isFocused()) {
            submitNameToServer();
        }
    }

    private void submitNameToServer() {
        if (!this.lastSentName.equals(this.textField.getText())) {
            Woodworks.NETWORK.sendToServer(new MessageStatueUpdateSkin(statue.getEntityId(), this.textField.getText()));
            lastSentName = this.textField.getText();
        }
    }
}
