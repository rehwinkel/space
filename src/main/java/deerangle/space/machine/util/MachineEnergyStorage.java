package deerangle.space.machine.util;

import net.minecraftforge.energy.IEnergyStorage;

public class MachineEnergyStorage implements IEnergyStorage {

    private final Ref<Integer> capacity;
    private final Ref<Integer> maxReceive;
    private final Ref<Integer> maxExtract;
    private final Ref<Integer> energy;
    private final Restriction restriction;

    public MachineEnergyStorage(Ref<Integer> capacity, Ref<Integer> maxReceive, Ref<Integer> maxExtract, Ref<Integer> energy, Restriction restriction) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.energy = energy;
        this.restriction = restriction;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (this.restriction == Restriction.ONLY_OUT) {
            return 0;
        }
        if (!canReceive())
            return 0;

        int energyReceived = Math.min(capacity.get() - energy.get(), Math.min(this.maxReceive.get(), maxReceive));
        if (!simulate)
            energy.set(energy.get() + energyReceived);
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (this.restriction == Restriction.ONLY_IN) {
            return 0;
        }
        if (!canExtract())
            return 0;

        int energyExtracted = Math.min(energy.get(), Math.min(this.maxExtract.get(), maxExtract));
        if (!simulate)
            energy.set(energy.get() - energyExtracted);
        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        return energy.get();
    }

    @Override
    public int getMaxEnergyStored() {
        return capacity.get();
    }

    @Override
    public boolean canExtract() {
        return this.maxExtract.get() > 0;
    }

    @Override
    public boolean canReceive() {
        return this.maxReceive.get() > 0;
    }
}
