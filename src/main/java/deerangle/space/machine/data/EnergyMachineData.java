package deerangle.space.machine.data;

import deerangle.space.machine.util.FlowType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyMachineData implements IMachineData {

    private final String name;
    private EnergyMachineGate storage;

    //TODO: inout behavior
    public EnergyMachineData(String name, int capacity, int transfer, FlowType flowType) {
        this.storage = new EnergyMachineGate(capacity, transfer, transfer, 0, flowType);
        this.name = name;
    }

    public LazyOptional<IEnergyStorage> getEnergyStorage(boolean fromCapability) {
        return storage.getEnergyStorage(fromCapability);
    }

    public IEnergyStorage getEnergyStorageForce(boolean fromCapability) {
        return this.getEnergyStorage(fromCapability)
                .orElseThrow(() -> new RuntimeException("failed to get fluid handler"));
    }

    public IEnergyStorage getEnergyStorageForce() {
        return this.getEnergyStorageForce(false);
    }

    @Override
    public INBT write() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("Cap", this.storage.getCapacity());
        nbt.putInt("Rec", this.storage.getMaxReceive());
        nbt.putInt("Ext", this.storage.getMaxExtract());
        nbt.putInt("Eng", this.storage.getEnergy());
        return nbt;
    }

    @Override
    public void read(INBT nbt) {
        CompoundNBT compound = (CompoundNBT) nbt;
        this.storage.setCapacity(compound.getInt("Cap"));
        this.storage.setMaxReceive(compound.getInt("Rec"));
        this.storage.setMaxExtract(compound.getInt("Ext"));
        this.storage.setEnergy(compound.getInt("Eng"));
    }

    @Override
    public void writePacket(PacketBuffer buf) {
        buf.writeInt(this.storage.getEnergy());
        buf.writeInt(this.storage.getCapacity());
    }

    @Override
    public void readPacket(PacketBuffer buf) {
        this.storage.setEnergy(buf.readInt());
        this.storage.setCapacity(buf.readInt());
    }

    @Override
    public String getName() {
        return name;
    }

    public int getCapacity() {
        return this.storage.getCapacity();
    }

    public int getEnergy() {
        return this.storage.getEnergy();
    }

    @Override
    public boolean storeInItem() {
        return true;
    }

}
