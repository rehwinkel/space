package deerangle.space.machine;

import deerangle.space.machine.data.FluidMachineData;
import deerangle.space.machine.data.ItemMachineData;
import deerangle.space.machine.util.FlowType;
import deerangle.space.machine.util.SideConfig;
import deerangle.space.registry.MachineTypeRegistry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GasTankMachine extends Machine {

    private final FluidMachineData tank;
    private final ItemMachineData input;
    private final ItemMachineData output;

    public GasTankMachine() {
        super(MachineTypeRegistry.GAS_TANK, new SideConfig(0, 0, 0, 0, 0, 0, 1));
        tank = addMachineData(new FluidMachineData("Tank", 64000, stack -> true, FlowType.INOUT));
        input = addMachineData(new ItemMachineData("Input", MachineTypeRegistry::holdsFluid, FlowType.INPUT));
        output = addMachineData(new ItemMachineData("Output", MachineTypeRegistry::holdsFluid, FlowType.OUTPUT));
    }

    @Override
    public void update(World world, BlockPos pos) {
        // TODO
    }

}
