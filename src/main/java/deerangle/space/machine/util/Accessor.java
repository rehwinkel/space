package deerangle.space.machine.util;

import deerangle.space.machine.data.IMachineData;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class Accessor {

    private final IMachineData data;
    private final boolean input;
    private final ITextComponent name;
    private final int color;

    public Accessor(IMachineData associatedData, boolean isInput, int color, ITextComponent name) {
        this.data = associatedData;
        this.input = isInput;
        this.color = color;
        this.name = isInput ? new TranslationTextComponent("info.space.in", name) : new TranslationTextComponent("info.space.out", name);
    }

    public int getColor() {
        return this.color;
    }

    public ITextComponent getName() {
        return this.name;
    }

    public boolean isInput() {
        return input;
    }

    public IMachineData getAssociatedData() {
        return data;
    }

}
