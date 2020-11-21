package deerangle.space.machine.util;

public enum FlowType {
    //TODO: adjust colors
    INPUT(0x0000ff, 0x007fff, 0x00ffff), OUTPUT(0xff0000, 0xff7f00, 0xffff00), INOUT(0xff00ff);

    private final int[] colors;

    FlowType(int... colors) {
        this.colors = colors;
    }

    public int getColor(int index) {
        return this.colors[index];
    }
}
