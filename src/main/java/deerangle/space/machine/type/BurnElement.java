package deerangle.space.machine.type;

import com.mojang.blaze3d.matrix.MatrixStack;
import deerangle.space.screen.CoalGeneratorScreen;
import deerangle.space.screen.DisplayValueReader;

public class BurnElement extends DataElement {

    public BurnElement(int x, int y, int index) {
        super(x, y, index);
    }

    @Override
    public void draw(CoalGeneratorScreen screen, DisplayValueReader reader, MatrixStack matrixStack, int guiLeft, int guiTop) {
        int x = guiLeft + this.getX();
        int y = guiTop + this.getY();
        float amount = reader.getBurnData(this.getIndex());
        screen.resetOverlayColor();
        screen.bindMachinesTexture();
        screen.blit(matrixStack, x, y, 0, 96 + 18, 18, 18);
        if (amount > 0f) {
            int height = (int) (13 * (1 - amount));
            screen.blit(matrixStack, x + 2, y + 2 + height, 20, 116 + height, 14, 14);
        }
    }

}
