package deerangle.space.machine;

import com.google.common.collect.ImmutableMap;
import deerangle.space.machine.data.EnergyMachineData;
import deerangle.space.machine.data.FluidMachineData;
import deerangle.space.machine.data.ItemMachineData;
import deerangle.space.machine.data.ProgressMachineData;
import deerangle.space.machine.util.FlowType;
import deerangle.space.machine.util.SideConfig;
import deerangle.space.registry.FluidRegistry;
import deerangle.space.registry.MachineTypeRegistry;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;

public class RefineryMachine extends Machine {

    private static final int SIP_SIZE = 20;
    private static final int RF_PER_TICK = 60;

    private final EnergyMachineData energy;
    private final FluidMachineData input;
    private final FluidMachineData output;
    private final ItemMachineData bucketInput;
    private final ItemMachineData bucketOutput;
    private final ProgressMachineData progress;

    public static final Map<Fluid, Fluid> RECIPE_MAP = ImmutableMap
            .of(FluidRegistry.CRUDE_OIL.get(), FluidRegistry.KEROSENE.get());

    public RefineryMachine() {
        super(MachineTypeRegistry.REFINERY, new SideConfig(0, -1, 1, 2, -1, -1, 3));
        energy = addMachineData(new EnergyMachineData("Energy", 15000, 1000, FlowType.INPUT));
        input = addMachineData(new FluidMachineData("Input", 4000, fluidStack -> true, FlowType.INPUT));
        output = addMachineData(new FluidMachineData("Output", 4000, fluidStack -> true, FlowType.OUTPUT));
        bucketInput = addMachineData(new ItemMachineData("InBuck", MachineTypeRegistry::holdsFluid, FlowType.INPUT));
        bucketOutput = addMachineData(new ItemMachineData("OutBuck", MachineTypeRegistry::holdsFluid, FlowType.OUTPUT));
        progress = addMachineData(new ProgressMachineData("Progress"));
    }

    @Override
    public void update(World world, BlockPos pos) {
        // TODO
    }

}
