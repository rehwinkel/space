package deerangle.space.machine.data;

import deerangle.space.machine.util.MachineEnergyStorage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyMachineData implements IMachineData {

    private final int transfer;
    private final String name;
    private LazyOptional<IEnergyStorage> storage;

    public EnergyMachineData(String name, int capacity, int transfer) {
        this.storage = LazyOptional.of(() -> new MachineEnergyStorage(capacity, transfer, transfer, 0));
        this.transfer = transfer;
        this.name = name;
    }

    public LazyOptional<IEnergyStorage> getStorage() {
        return storage;
    }

    public IEnergyStorage getStorageOrThrow() {
        return storage.orElseThrow(() -> new RuntimeException("failed to get energy storage"));
    }

    @Override
    public INBT write() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("Cap", this.getCapacity());
        nbt.putInt("Eng", this.getEnergy());
        nbt.putInt("XFer", this.transfer);
        return nbt;
    }

    @Override
    public void read(INBT nbt) {
        CompoundNBT compound = (CompoundNBT) nbt;
        int capacity = compound.getInt("Cap");
        int energy = compound.getInt("Eng");
        int xfer = compound.getInt("XFer");
        this.storage = LazyOptional.of(() -> new MachineEnergyStorage(capacity, xfer, xfer, energy));
    }

    @Override
    public String getName() {
        return name;
    }

    public int getCapacity() {
        return this.storage.orElseThrow(() -> new RuntimeException("no energy storage present")).getMaxEnergyStored();
    }

    public int getEnergy() {
        return this.storage.orElseThrow(() -> new RuntimeException("no energy storage present")).getEnergyStored();
    }

    @Override
    public void writePacket(PacketBuffer buf) {
        buf.writeInt(this.getEnergy());
        buf.writeInt(this.getCapacity());
    }

    @Override
    public void readPacket(PacketBuffer buf) {
        MachineEnergyStorage storage = (MachineEnergyStorage) this.storage
                .orElseThrow(() -> new RuntimeException("no energy storage present"));
        storage.setEnergy(buf.readInt());
        storage.setCapacity(buf.readInt());
    }

    @Override
    public boolean storeInItem() {
        return true;
    }

}
