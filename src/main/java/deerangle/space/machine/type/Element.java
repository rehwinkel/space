package deerangle.space.machine.type;

import com.mojang.blaze3d.matrix.MatrixStack;
import deerangle.space.screen.CoalGeneratorScreen;
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

    public abstract void draw(CoalGeneratorScreen screen, DisplayValueReader reader, MatrixStack matrixStack, int guiLeft, int guiTop);

}
