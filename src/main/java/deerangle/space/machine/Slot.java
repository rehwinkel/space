package deerangle.space.machine;

import net.minecraft.nbt.INBT;

public abstract class Slot extends Display {

    protected final boolean input;

    public Slot(int x, int y, boolean input) {
        super(x, y);
        this.input = input;
    }

    public abstract INBT write();

    public boolean isInput() {
        return input;
    }

}
