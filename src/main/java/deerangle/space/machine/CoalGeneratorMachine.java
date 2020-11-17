package deerangle.space.machine;

import deerangle.space.machine.data.BurnMachineData;
import deerangle.space.machine.data.EnergyMachineData;
import deerangle.space.machine.data.ItemMachineData;
import deerangle.space.machine.util.SideConfig;
import deerangle.space.registry.MachineTypeRegistry;

public class CoalGeneratorMachine extends Machine {

    private final ItemMachineData fuel;
    private final EnergyMachineData energy;
    private final BurnMachineData burn;

    public CoalGeneratorMachine() {
        super(MachineTypeRegistry.COAL_GENERATOR, new SideConfig(0, 1, -1, -1, -1, -1));
        fuel = new ItemMachineData("Fuel");
        energy = new EnergyMachineData("Eng", 16000, 1000);
        burn = new BurnMachineData("Burn");
        machineDataList.add(fuel);
        machineDataList.add(energy);
        machineDataList.add(burn);
    }

    @Override
    public void update() {
        this.energy.getStorage().orElseThrow(() -> new RuntimeException("a")).receiveEnergy(40, false);
    }
}
