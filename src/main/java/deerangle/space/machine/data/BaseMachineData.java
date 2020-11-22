package deerangle.space.machine.data;

import deerangle.space.machine.util.Accessor;
import deerangle.space.machine.util.FlowType;
import deerangle.space.machine.util.IColorGetter;
import net.minecraft.util.text.ITextComponent;

public abstract class BaseMachineData implements IMachineData {

    protected final Accessor inputAccessor;
    protected final Accessor outputAccessor;
    private final String name;
    private final int color;

    public BaseMachineData(String name, FlowType flowType, IColorGetter colorGetter, ITextComponent dataName) {
        this.name = name;
        this.color = colorGetter.getNextColor(this.hashCode(), flowType, IColorGetter.ColorType.GUI);
        this.inputAccessor = flowType.doesInput() ? new Accessor(this, true,
                colorGetter.getNextColor(this.hashCode(), flowType, IColorGetter.ColorType.IN), dataName) : null;
        this.outputAccessor = flowType.doesOutput() ? new Accessor(this, false,
                colorGetter.getNextColor(this.hashCode(), flowType, IColorGetter.ColorType.OUT), dataName) : null;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public Accessor getInputAccessor() {
        return this.inputAccessor;
    }

    @Override
    public Accessor getOutputAccessor() {
        return this.outputAccessor;
    }

    @Override
    public String getName() {
        return name;
    }
}
