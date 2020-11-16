package deerangle.space.machine;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergySlot extends Slot {

    private final int transfer;
    private LazyOptional<IEnergyStorage> storage;

    public EnergySlot(int x, int y, boolean input, int capacity, int transfer) {
        super(x, y, input);
        this.storage = LazyOptional.of(() -> new EnergyStorage(capacity, transfer));
        this.transfer = transfer;
    }

    public LazyOptional<IEnergyStorage> getStorage() {
        return storage;
    }

    @Override
    public INBT write() {
        CompoundNBT nbt = new CompoundNBT();
        this.storage.ifPresent(energyStorage -> {
            nbt.putInt("Cap", energyStorage.getMaxEnergyStored());
        });
        this.storage.ifPresent(energyStorage -> {
            nbt.putInt("Eng", energyStorage.getEnergyStored());
        });
        nbt.putInt("XFer", this.transfer);
        return nbt;
    }

}
