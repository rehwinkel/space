package deerangle.space.machine;

import deerangle.space.machine.data.FluidMachineData;
import deerangle.space.machine.data.ItemMachineData;
import deerangle.space.machine.util.FlowType;
import deerangle.space.machine.util.SideConfig;
import deerangle.space.registry.MachineTypeRegistry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class DrumMachine extends Machine {

    private final FluidMachineData tank;
    private final ItemMachineData input;
    private final ItemMachineData output;

    public DrumMachine() {
        super(MachineTypeRegistry.GAS_TANK, new SideConfig(0, 0, 0, 0, 0, 0, 3));
        tank = addMachineData(new FluidMachineData("Tank", 64000, stack -> true));
        input = addMachineData(new ItemMachineData("Input",
                stack -> stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent(),
                FlowType.INPUT));
        output = addMachineData(new ItemMachineData("Output",
                stack -> stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent(),
                FlowType.OUTPUT));
    }

    @Override
    public void update(World world, BlockPos pos) {
    }

}
