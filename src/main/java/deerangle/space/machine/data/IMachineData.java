package deerangle.space.machine.data;

import deerangle.space.machine.util.Accessor;
import net.minecraft.nbt.INBT;
import net.minecraft.network.PacketBuffer;

public interface IMachineData {

    INBT write();

    void read(INBT nbt);

    String getName();

    void writePacket(PacketBuffer buf);

    void readPacket(PacketBuffer buf);

    Accessor getInputAccessor();

    Accessor getOutputAccessor();

    int getColor();

}
