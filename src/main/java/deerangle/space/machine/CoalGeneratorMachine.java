package deerangle.space.machine;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

public class CoalGeneratorMachine extends Machine {

    private final ItemSlot fuel;
    private final EnergySlot energy;
    private final BurnDisplay burn;

    public CoalGeneratorMachine() {
        super(new SideConfig(0, 1, -1, -1, -1, -1));
        fuel = new ItemSlot(79, 47, true);
        energy = new EnergySlot(17, 17, false, 16000, 1000);
        burn = new BurnDisplay(79, 29);
        slotList.add(fuel);
        slotList.add(energy);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.put("Fuel", fuel.write());
        compound.put("Energy", energy.write());
        //TODO: save burn?
        return compound;
    }

}
