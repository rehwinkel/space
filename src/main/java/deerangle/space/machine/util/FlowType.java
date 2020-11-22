package deerangle.space.machine.util;

public enum FlowType {

    INPUT(true, false), OUTPUT(false, true), INOUT(true, true), NONE(false, false);

    private final boolean input;
    private final boolean output;

    FlowType(boolean doesInput, boolean doesOutput) {
        this.input = doesInput;
        this.output = doesOutput;
    }

    public boolean doesInput() {
        return this.input;
    }

    public boolean doesOutput() {
        return this.output;
    }

}
