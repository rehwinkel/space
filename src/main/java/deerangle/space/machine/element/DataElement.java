package deerangle.space.machine.element;

public abstract class DataElement extends Element {

    private final int index;

    public DataElement(int x, int y, int index) {
        super(x, y);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

}
