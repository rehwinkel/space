package deerangle.space.machine.util;

public enum FlowType {
    INPUT(0xff0000, 0xff7f00, 0xffff00), OUTPUT(0x0000ff, 0x007fff, 0x00ffff), INOUT(0xff00ff);

    private final int[] colors;

    FlowType(int... colors) {
        this.colors = colors;
    }

    public int getColor(int index) {
        return this.colors[index];
    }
}
