package deerangle.space.machine.util;

public interface IColorGetter {

    enum ColorType {
        GUI, OUT, IN;
    }

    int getNextColor(int hash, FlowType flowType, ColorType colorType);

}
