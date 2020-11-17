package deerangle.space.machine;

import net.minecraftforge.energy.EnergyStorage;

public class MachineEnergyStorage extends EnergyStorage {

    public MachineEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    public void setEnergy(int value) {
        this.energy = value;
    }

    public void setCapacity(int value) {
        this.capacity = value;
    }

}
