package deerangle.space.machine.element;

import com.mojang.blaze3d.matrix.MatrixStack;
import deerangle.space.screen.DisplayValueReader;
import deerangle.space.screen.MachineScreen;

public class ProgressElement extends DataElement {

    public ProgressElement(int x, int y, int index) {
        super(x, y, index);
    }

    @Override
    public void draw(MachineScreen screen, DisplayValueReader reader, MatrixStack matrixStack, int guiLeft, int guiTop) {
        int x = guiLeft + this.getX();
        int y = guiTop + this.getY();
        float amount = reader.getProgressData(this.getIndex());
        screen.resetOverlayColor();
        screen.bindMachinesTexture();
        screen.blit(matrixStack, x, y, 90, 18, 24, 18);
        if (amount > 0f) {
            int width = (int) (22 * amount);
            screen.blit(matrixStack, x + 1, y, 91, 0, width, 18);
        }
    }

}
