package deerangle.space.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

public class SideColorButton extends Button {

    private int color;

    public SideColorButton(int x, int y, int color, ITextComponent title, IPressable pressedAction) {
        super(x, y, 20, 20, title, pressedAction);
        this.color = color;
    }

    private void setOverlayColor() {
        int overlay = this.color;
        RenderSystem
                .color4f(((overlay >> 16) & 0xFF) / 255.0F, ((overlay >> 8) & 0xFF) / 255.0F, (overlay & 0xFF) / 255.0F,
                        this.alpha);
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        FontRenderer fontrenderer = minecraft.fontRenderer;
        minecraft.getTextureManager().bindTexture(MachineScreen.MACHINES_GUI);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
        int i = this.getYImage(this.isHovered());
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.blit(matrixStack, this.x, this.y, 70, (i - 1) * 20, 20, 20);
        this.setOverlayColor();
        this.blit(matrixStack, this.x + 1, this.y + 1, 71, (i - 1) * 20 + 1, 18, 18);
        this.renderBg(matrixStack, minecraft, mouseX, mouseY);
        int j = getFGColor();
        drawCenteredString(matrixStack, fontrenderer, this.getMessage(), this.x + this.width / 2,
                this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
    }

    public void setColor(int color) {
        this.color = color;
    }

}