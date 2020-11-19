package deerangle.space.machine.element;

import com.mojang.blaze3d.matrix.MatrixStack;
import deerangle.space.screen.DisplayValueReader;
import deerangle.space.screen.MachineScreen;

public class BurnElement extends DataElement {

    public BurnElement(int x, int y, int index) {
        super(x, y, index);
    }

    @Override
    public void draw(MachineScreen screen, DisplayValueReader reader, MatrixStack matrixStack, int guiLeft, int guiTop) {
        int x = guiLeft + this.getX();
        int y = guiTop + this.getY();
        float amount = reader.getBurnData(this.getIndex());
        screen.resetOverlayColor();
        screen.bindMachinesTexture();
        screen.blit(matrixStack, x, y, 0, 116, 18, 18);
        if (amount > 0f) {
            int height = (int) (13 * (1 - amount));
            screen.blit(matrixStack, x + 2, y + 2 + height, 20, 118 + height, 14, 14);
        }
    }

}
