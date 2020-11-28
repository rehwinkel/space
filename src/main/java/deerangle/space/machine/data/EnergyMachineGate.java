package deerangle.space.machine.data;

import deerangle.space.machine.util.MachineEnergyStorage;
import deerangle.space.machine.util.Ref;
import deerangle.space.machine.util.Restriction;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyMachineGate {

    private final Ref<Integer> capacity;
    private final Ref<Integer> maxReceive;
    private final Ref<Integer> maxExtract;
    private final Ref<Integer> energy;
    private final LazyOptional<IEnergyStorage> unrestricted;
    private final LazyOptional<IEnergyStorage> only_out;
    private final LazyOptional<IEnergyStorage> only_in;

    public EnergyMachineGate(int capacity, int maxReceive, int maxExtract, int energy) {
        this.capacity = new Ref<>(capacity);
        this.maxReceive = new Ref<>(maxReceive);
        this.maxExtract = new Ref<>(maxExtract);
        this.energy = new Ref<>(energy);
        unrestricted = LazyOptional.of(() -> new MachineEnergyStorage(this.capacity, this.maxReceive, this.maxExtract, this.energy, Restriction.UNRESTRICTED));
        only_out = LazyOptional.of(() -> new MachineEnergyStorage(this.capacity, this.maxReceive, this.maxExtract, this.energy, Restriction.ONLY_OUT));
        only_in = LazyOptional.of(() -> new MachineEnergyStorage(this.capacity, this.maxReceive, this.maxExtract, this.energy, Restriction.ONLY_IN));

    }

    public LazyOptional<IEnergyStorage> getEnergyStorage(Restriction restriction) {
        switch (restriction) {
            case UNRESTRICTED:
                return unrestricted;
            case ONLY_OUT:
                return only_out;
            case ONLY_IN:
                return only_in;
        }
        return null;
    }

    public int getCapacity() {
        return capacity.get();
    }

    public void setCapacity(int cap) {
        this.capacity.set(cap);
    }

    public int getMaxExtract() {
        return maxExtract.get();
    }

    public void setMaxExtract(int ext) {
        this.maxExtract.set(ext);
    }

    public int getMaxReceive() {
        return maxReceive.get();
    }

    public void setMaxReceive(int rec) {
        this.maxReceive.set(rec);
    }

    public int getEnergy() {
        return energy.get();
    }

    public void setEnergy(int eng) {
        this.energy.set(eng);
    }

}
