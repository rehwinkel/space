package deerangle.space.machine.data;

import deerangle.space.machine.util.FlowType;
import deerangle.space.machine.util.MachineEnergyStorage;
import deerangle.space.machine.util.Ref;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyMachineGate {

    private final Ref<Integer> capacity;
    private final Ref<Integer> maxReceive;
    private final Ref<Integer> maxExtract;
    private final Ref<Integer> energy;
    private final LazyOptional<IEnergyStorage> restricted;
    private final LazyOptional<IEnergyStorage> unrestricted;

    public EnergyMachineGate(int capacity, int maxReceive, int maxExtract, int energy, FlowType flowType) {
        this.capacity = new Ref<>(capacity);
        this.maxReceive = new Ref<>(maxReceive);
        this.maxExtract = new Ref<>(maxExtract);
        this.energy = new Ref<>(energy);
        restricted = LazyOptional
                .of(() -> new MachineEnergyStorage(this.capacity, this.maxReceive, this.maxExtract, this.energy,
                        flowType));
        unrestricted = LazyOptional
                .of(() -> new MachineEnergyStorage(this.capacity, this.maxReceive, this.maxExtract, this.energy, null));
    }

    public LazyOptional<IEnergyStorage> getEnergyStorage(boolean fromCapability) {
        if (fromCapability) {
            return restricted;
        } else {
            return unrestricted;
        }
    }

    public int getCapacity() {
        return capacity.get();
    }

    public int getMaxExtract() {
        return maxExtract.get();
    }

    public int getMaxReceive() {
        return maxReceive.get();
    }

    public int getEnergy() {
        return energy.get();
    }

    public void setCapacity(int cap) {
        this.capacity.set(cap);
    }

    public void setMaxReceive(int rec) {
        this.maxReceive.set(rec);
    }

    public void setMaxExtract(int ext) {
        this.maxExtract.set(ext);
    }

    public void setEnergy(int eng) {
        this.energy.set(eng);
    }

}
