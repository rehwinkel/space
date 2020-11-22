package deerangle.space.machine;

import deerangle.space.machine.data.EnergyMachineData;
import deerangle.space.machine.data.ItemMachineData;
import deerangle.space.machine.util.FlowType;
import deerangle.space.registry.MachineTypeRegistry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;

public class BatteryPackMachine extends Machine {

    private final EnergyMachineData energy;
    private final ItemMachineData input;
    private final ItemMachineData output;

    public BatteryPackMachine() {
        super(MachineTypeRegistry.BATTERY_PACK);
        energy = addMachineData(new EnergyMachineData("Energy", 500000, 1000, FlowType.INOUT, this, ENERGY_TEXT));
        input = addMachineData(
                new ItemMachineData("Input", stack -> stack.getCapability(CapabilityEnergy.ENERGY).isPresent(),
                        FlowType.NONE, this, BATTERY_TEXT));
        output = addMachineData(
                new ItemMachineData("Output", stack -> stack.getCapability(CapabilityEnergy.ENERGY).isPresent(),
                        FlowType.NONE, this, BATTERY_TEXT));
        this.sideConfig.setAll(energy.getOutputAccessor());
    }

    @Override
    public void update(World world, BlockPos pos) {
        // TODO
    }

}
