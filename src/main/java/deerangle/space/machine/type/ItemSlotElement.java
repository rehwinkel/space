package deerangle.space.machine.type;

import com.mojang.blaze3d.matrix.MatrixStack;
import deerangle.space.screen.CoalGeneratorScreen;
import deerangle.space.screen.DisplayValueReader;
import net.minecraft.util.text.ITextComponent;

public class ItemSlotElement extends SlotElement {

    ItemSlotElement(int x, int y, int index, boolean input, int overlayColor) {
        super(x, y, index, input,overlayColor, 18, 18);
    }

    @Override
    public ITextComponent getTooltipText(DisplayValueReader valueReader) {
        return null;
    }

    @Override
    public void draw(CoalGeneratorScreen screen, DisplayValueReader reader, MatrixStack matrixStack, int guiLeft, int guiTop) {
        int x = guiLeft + this.getX();
        int y = guiTop + this.getY();
        screen.resetOverlayColor();
        screen.bindMachinesTexture();
        screen.blit(matrixStack, x, y, 0, 96, 18, 18);
        screen.setOverlayColor(this.getOverlayColor());
        screen.blit(matrixStack, x, y, 18, 96, 18, 18);
    }

}
