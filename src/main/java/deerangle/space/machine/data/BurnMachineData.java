package deerangle.space.machine.data;

import net.minecraft.nbt.FloatNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.PacketBuffer;

public class BurnMachineData implements IMachineData {

    private final String name;
    private float progress;

    public BurnMachineData(String name) {
        this.name = name;
    }

    @Override
    public INBT write() {
        return FloatNBT.valueOf(this.progress);
    }

    @Override
    public void read(INBT nbt) {
        this.progress = ((FloatNBT) nbt).getFloat();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void writePacket(PacketBuffer buf) {
        buf.writeFloat(this.progress);
    }

    @Override
    public void readPacket(PacketBuffer buf) {
        this.progress = buf.readFloat();
    }

    @Override
    public boolean storeInItem() {
        return false;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public float getProgress() {
        return progress;
    }

}
