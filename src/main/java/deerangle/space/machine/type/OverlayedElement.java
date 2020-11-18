package deerangle.space.machine.type;

import deerangle.space.screen.DisplayValueReader;
import net.minecraft.util.text.ITextComponent;

public abstract class OverlayedElement extends DataElement {

    private final boolean input;
    private final int width;
    private final int height;
    private final int overlayColor;

    public OverlayedElement(int x, int y, int index, boolean input, int overlayColor, int width, int height) {
        super(x, y, index);
        this.input = input;
        this.overlayColor = overlayColor;
        this.width = width;
        this.height = height;
    }

    public boolean isInput() {
        return input;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getOverlayColor() {
        return overlayColor;
    }

    public abstract ITextComponent getTooltipText(DisplayValueReader valueReader);

}
