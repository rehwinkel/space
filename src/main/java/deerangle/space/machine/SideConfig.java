package deerangle.space.machine;

import net.minecraft.util.Direction;

public class SideConfig {

    private byte front;
    private byte back;
    private byte left;
    private byte right;
    private byte top;
    private byte bottom;

    public SideConfig(int front, int back, int left, int right, int top, int bottom) {
        this.front = (byte) front;
        this.back = (byte) back;
        this.left = (byte) left;
        this.right = (byte) right;
        this.top = (byte) top;
        this.bottom = (byte) bottom;
    }

    public int getFront() {
        return front;
    }

    public void setFront(int front) {
        this.front = (byte) front;
    }

    public int getBack() {
        return back;
    }

    public void setBack(int back) {
        this.back = (byte) back;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = (byte) left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = (byte) right;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = (byte) top;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = (byte) bottom;
    }

    public int getIndexForSide(Direction facing, Direction side) {
        switch (facing) {
            case NORTH:
                break;
            case SOUTH:
                side = side.rotateY().rotateY();
            case EAST:
                side = side.rotateY();
            case WEST:
                side = side.rotateYCCW();
        }
        switch (side) {
            case NORTH:
                return this.getFront();
            case SOUTH:
                return this.getBack();
            case UP:
                return this.getTop();
            case DOWN:
                return this.getBottom();
            case EAST:
                return this.getLeft();
            case WEST:
                return this.getRight();
        }
        return -1;
    }

}
