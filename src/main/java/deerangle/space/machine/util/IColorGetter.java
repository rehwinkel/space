package deerangle.space.machine.util;

public interface IColorGetter {

    int getNextColor(int hash, FlowType flowType, ColorType colorType);

    enum ColorType {
        GUI, OUT, IN
    }

}
