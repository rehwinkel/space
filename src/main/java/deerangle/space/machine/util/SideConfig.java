package deerangle.space.machine.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;

import java.util.ArrayList;
import java.util.List;

public class SideConfig {

    private Accessor front;
    private Accessor back;
    private Accessor left;
    private Accessor right;
    private Accessor top;
    private Accessor bottom;
    private final boolean[] blocked;
    private final List<Accessor> accessors;

    public SideConfig() {
        this(new boolean[]{false, false, false, false, false, false});
    }

    public SideConfig(boolean[] blocked) {
        this.accessors = new ArrayList<>();
        this.accessors.add(null);
        this.blocked = blocked;
        this.bottom = null;
        this.top = null;
        this.right = null;
        this.front = null;
        this.back = null;
        this.left = null;
    }

    public Accessor getFront() {
        return front;
    }

    public void setFront(Accessor front) {
        this.front = front;
    }

    public boolean isFrontBlocked() {
        return this.blocked[0];
    }

    public Accessor getBack() {
        return back;
    }

    public void setBack(Accessor back) {
        this.back = back;
    }

    public boolean isBackBlocked() {
        return this.blocked[1];
    }

    public Accessor getLeft() {
        return left;
    }

    public void setLeft(Accessor left) {
        this.left = left;
    }

    public boolean isLeftBlocked() {
        return this.blocked[2];
    }

    public Accessor getRight() {
        return right;
    }

    public void setRight(Accessor right) {
        this.right = right;
    }

    public boolean isRightBlocked() {
        return this.blocked[3];
    }

    public Accessor getTop() {
        return top;
    }

    public void setTop(Accessor top) {
        this.top = top;
    }

    public boolean isTopBlocked() {
        return this.blocked[4];
    }

    public Accessor getBottom() {
        return bottom;
    }

    public void setBottom(Accessor bottom) {
        this.bottom = bottom;
    }

    public boolean isBottomBlocked() {
        return this.blocked[5];
    }

    public Accessor getAccessorForSide(Direction facing, Direction side) {
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
        return null;
    }

    public Accessor getNext(Accessor in, boolean forward) {
        int index = this.accessors.indexOf(in);
        int newIndex = index + (forward ? 1 : -1);
        if (newIndex == -1) {
            newIndex = this.accessors.size() - 1;
        } else if (newIndex == this.accessors.size()) {
            newIndex = 0;
        }
        return this.accessors.get(newIndex);
    }

    public void writePacket(PacketBuffer buf) {
        buf.writeByte(this.accessors.indexOf(front));
        buf.writeByte(this.accessors.indexOf(back));
        buf.writeByte(this.accessors.indexOf(left));
        buf.writeByte(this.accessors.indexOf(right));
        buf.writeByte(this.accessors.indexOf(top));
        buf.writeByte(this.accessors.indexOf(bottom));
    }

    public void readPacket(PacketBuffer buf) {
        front = this.accessors.get(buf.readByte());
        back = this.accessors.get(buf.readByte());
        left = this.accessors.get(buf.readByte());
        right = this.accessors.get(buf.readByte());
        top = this.accessors.get(buf.readByte());
        bottom = this.accessors.get(buf.readByte());
    }

    public void addAccessor(Accessor accessor) {
        this.accessors.add(accessor);
    }

    public CompoundNBT write(CompoundNBT nbt) {
        nbt.putByte("U", (byte) this.accessors.indexOf(this.getTop()));
        nbt.putByte("D", (byte) this.accessors.indexOf(this.getBottom()));
        nbt.putByte("L", (byte) this.accessors.indexOf(this.getLeft()));
        nbt.putByte("R", (byte) this.accessors.indexOf(this.getRight()));
        nbt.putByte("F", (byte) this.accessors.indexOf(this.getFront()));
        nbt.putByte("B", (byte) this.accessors.indexOf(this.getBack()));
        return nbt;
    }

    public void read(CompoundNBT nbt) {
        this.setRight(this.accessors.get(nbt.getByte("R")));
        this.setLeft(this.accessors.get(nbt.getByte("L")));
        this.setFront(this.accessors.get(nbt.getByte("F")));
        this.setBack(this.accessors.get(nbt.getByte("B")));
        this.setTop(this.accessors.get(nbt.getByte("U")));
        this.setBottom(this.accessors.get(nbt.getByte("D")));
    }

    public void setAll(Accessor accessor) {
        this.setBottom(accessor);
        this.setTop(accessor);
        this.setBack(accessor);
        this.setFront(accessor);
        this.setLeft(accessor);
        this.setRight(accessor);
    }

}
