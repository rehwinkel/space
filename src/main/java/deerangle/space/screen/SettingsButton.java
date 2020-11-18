package deerangle.space.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;

public class SettingsButton extends Button {

    public SettingsButton(int x, int y, IPressable pressedAction) {
        super(x, y, 16, 16, null, pressedAction);
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(MachineScreen.MACHINES_GUI);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
        int yOffset = this.isHovered() ? 16 : 0;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.blit(matrixStack, this.x, this.y, 3 * 18, yOffset, 16, 16);
        this.renderBg(matrixStack, minecraft, mouseX, mouseY);
    }
}
