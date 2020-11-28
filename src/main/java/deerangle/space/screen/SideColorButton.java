package deerangle.space.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SideColorButton extends Button {

    private final IPressableRL pressedAction;
    private final String tooltip;
    private int color;
    private ITextComponent selectedName;

    public SideColorButton(int x, int y, String title, IPressableRL pressedAction) {
        super(x, y, 20, 20, new TranslationTextComponent(title + "_letter"), button -> {
        });
        this.pressedAction = pressedAction;
        this.tooltip = title;
    }

    private void setOverlayColor() {
        int overlay = this.color;
        RenderSystem.color4f(((overlay >> 16) & 0xFF) / 255.0F, ((overlay >> 8) & 0xFF) / 255.0F, (overlay & 0xFF) / 255.0F, this.alpha);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.active && this.visible) {
            if (this.isValidClickButton(button)) {
                boolean flag = this.clicked(mouseX, mouseY);
                if (flag) {
                    this.playDownSound(Minecraft.getInstance().getSoundHandler());
                    this.pressedAction.onPress(button);
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean isValidClickButton(int button) {
        return button == 0 || button == 1;
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
        drawCenteredString(matrixStack, fontrenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
    }

    public void drawTooltip(Screen screen, MatrixStack matrixStack, int mouseX, int mouseY) {
        if (mouseX >= this.x && mouseX < this.x + 20) {
            if (mouseY >= this.y && mouseY < this.y + 20) {
                screen.renderTooltip(matrixStack, new TranslationTextComponent(tooltip, selectedName), mouseX, mouseY);
            }
        }
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setSelectedName(ITextComponent selectedName) {
        this.selectedName = selectedName;
    }

    @OnlyIn(Dist.CLIENT)
    public interface IPressableRL {
        void onPress(int mouseButton);
    }

}
