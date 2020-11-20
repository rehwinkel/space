package deerangle.space.machine.element;

import deerangle.space.machine.util.FlowType;
import deerangle.space.screen.DisplayValueReader;
import net.minecraft.util.text.ITextComponent;

public abstract class OverlayedElement extends DataElement {

    private final FlowType flowType;
    private final int width;
    private final int height;
    private final int overlayColor;

    public OverlayedElement(int x, int y, int index, FlowType flowType, int overlayColor, int width, int height) {
        super(x, y, index);
        this.flowType = flowType;
        this.overlayColor = overlayColor;
        this.width = width;
        this.height = height;
    }

    public FlowType getFlowType() {
        return flowType;
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
