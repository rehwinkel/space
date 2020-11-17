package deerangle.space.machine.data;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.network.PacketBuffer;

public class BurnMachineData implements IMachineData {

    private final String name;

    public BurnMachineData(String name) {
        this.name = name;
    }

    @Override
    public INBT write() {
        return IntNBT.valueOf(1);
    }

    @Override
    public void read(INBT nbt) {

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void writePacket(PacketBuffer buf) {
        buf.writeInt(1);
    }

    @Override
    public void readPacket(PacketBuffer buf) {
        buf.readInt();
    }

}
