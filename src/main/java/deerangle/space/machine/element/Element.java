package deerangle.space.machine.element;

import com.mojang.blaze3d.matrix.MatrixStack;
import deerangle.space.screen.MachineScreen;
import deerangle.space.screen.DisplayValueReader;

public abstract class Element {

    private final int x;
    private final int y;

    public Element(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public abstract void draw(MachineScreen screen, DisplayValueReader reader, MatrixStack matrixStack, int guiLeft, int guiTop);

}
