package deerangle.space.machine.util;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;

public class SideConfig {

    private byte front;
    private byte back;
    private byte left;
    private byte right;
    private byte top;
    private byte bottom;
    private final boolean[] blocked;
    private final byte sideValueCount;

    public SideConfig(int front, int back, int left, int right, int top, int bottom, int sideValueCount) {
        this(front, back, left, right, top, bottom, false, false, false, false, false, false, sideValueCount);
    }

    public SideConfig(int front, int back, int left, int right, int top, int bottom, boolean frontBlock, boolean backBlock, boolean leftBlock, boolean rightBlock, boolean topBlock, boolean bottomBlock, int sideValueCount) {
        this.front = (byte) front;
        this.back = (byte) back;
        this.left = (byte) left;
        this.right = (byte) right;
        this.top = (byte) top;
        this.bottom = (byte) bottom;
        this.blocked = new boolean[]{frontBlock, backBlock, leftBlock, rightBlock, topBlock, bottomBlock};
        this.sideValueCount = (byte) sideValueCount;
    }

    public int getFront() {
        return this.isFrontBlocked() ? -1 : front;
    }

    public void setFront(int front) {
        this.front = (byte) front;
    }

    public boolean isFrontBlocked() {
        return this.blocked[0];
    }

    public int getBack() {
        return this.isBackBlocked() ? -1 : back;
    }

    public void setBack(int back) {
        this.back = (byte) back;
    }

    public boolean isBackBlocked() {
        return this.blocked[1];
    }

    public int getLeft() {
        return this.isLeftBlocked() ? -1 : left;
    }

    public void setLeft(int left) {
        this.left = (byte) left;
    }

    public boolean isLeftBlocked() {
        return this.blocked[2];
    }

    public int getRight() {
        return this.isRightBlocked() ? -1 : right;
    }

    public void setRight(int right) {
        this.right = (byte) right;
    }

    public boolean isRightBlocked() {
        return this.blocked[3];
    }

    public int getTop() {
        return this.isTopBlocked() ? -1 : top;
    }

    public void setTop(int top) {
        this.top = (byte) top;
    }

    public boolean isTopBlocked() {
        return this.blocked[4];
    }

    public int getBottom() {
        return this.isBottomBlocked() ? -1 : bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = (byte) bottom;
    }

    public boolean isBottomBlocked() {
        return this.blocked[5];
    }

    public int getIndexForSide(Direction facing, Direction side) {
        if (side.getAxis() != Direction.Axis.Y) {
            switch (facing) {
                case NORTH:
                    break;
                case SOUTH:
                    side = side.rotateY().rotateY();
                    break;
                case EAST:
                    side = side.rotateYCCW();
                    break;
                case WEST:
                    side = side.rotateY();
                    break;
            }
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

    public int getNext(int index, boolean forward) {
        int newIndex;
        if (forward) {
            newIndex = index + 1;
            if (newIndex == sideValueCount) {
                newIndex = -1;
            }
        } else {
            newIndex = index - 1;
            if (newIndex == -2) {
                newIndex = sideValueCount - 1;
            }
        }
        return newIndex;
    }

    public void writePacket(PacketBuffer buf) {
        buf.writeByte(front);
        buf.writeByte(back);
        buf.writeByte(left);
        buf.writeByte(right);
        buf.writeByte(top);
        buf.writeByte(bottom);
    }

    public void readPacket(PacketBuffer buf) {
        front = buf.readByte();
        back = buf.readByte();
        left = buf.readByte();
        right = buf.readByte();
        top = buf.readByte();
        bottom = buf.readByte();
    }

}
