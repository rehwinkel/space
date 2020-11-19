package deerangle.space.machine;

import deerangle.space.machine.data.BurnMachineData;
import deerangle.space.machine.data.ItemMachineData;
import deerangle.space.machine.data.ProgressMachineData;
import deerangle.space.machine.util.SideConfig;
import deerangle.space.registry.MachineTypeRegistry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlastFurnaceMachine extends Machine {

    private final ItemMachineData fuel;
    private final ItemMachineData input;
    private final ItemMachineData output;
    private final BurnMachineData burn;
    private final ProgressMachineData progress;

    public BlastFurnaceMachine() {
        super(MachineTypeRegistry.BLAST_FURNACE,
                new SideConfig(-1, -1, 1, 2, -1, 0, true, false, false, false, true, false, 3));
        fuel = addMachineData(new ItemMachineData("Fuel"));
        input = addMachineData(new ItemMachineData("Input"));
        output = addMachineData(new ItemMachineData("Output"));
        burn = addMachineData(new BurnMachineData("Burn"));
        progress = addMachineData(new ProgressMachineData("Prog"));
    }

    @Override
    public void update(World world, BlockPos pos) {

    }

}
