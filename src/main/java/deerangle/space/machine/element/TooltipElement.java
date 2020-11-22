package deerangle.space.machine.element;

import deerangle.space.screen.DisplayValueReader;
import net.minecraft.util.text.ITextComponent;

public abstract class TooltipElement extends DataElement {

    private final int width;
    private final int height;
    private final ITextComponent name;

    public TooltipElement(int x, int y, int index, ITextComponent name, int width, int height) {
        super(x, y, index);
        this.width = width;
        this.height = height;
        this.name = name;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public abstract ITextComponent getTooltipText(DisplayValueReader valueReader);

    public ITextComponent getName() {
        return this.name;
    }

}
