package deerangle.space.machine;

import deerangle.space.machine.data.FluidMachineData;
import deerangle.space.machine.data.ItemMachineData;
import deerangle.space.machine.util.FlowType;
import deerangle.space.registry.MachineTypeRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class DrumMachine extends Machine {

    private final FluidMachineData tank;
    private final ItemMachineData input;
    private final ItemMachineData output;

    public DrumMachine() {
        super(MachineTypeRegistry.DRUM);
        tank = addMachineData(new FluidMachineData("Tank", 64000, stack -> true, FlowType.INOUT, this, TANK_TEXT));
        input = addMachineData(
                new ItemMachineData("Input", MachineTypeRegistry::holdsFluid, FlowType.NONE, this, BUCKET_TEXT));
        output = addMachineData(
                new ItemMachineData("Output", MachineTypeRegistry::holdsFluid, FlowType.NONE, this, BUCKET_TEXT));
        this.sideConfig.setAll(tank.getOutputAccessor());
    }

    @Override
    public void update(World world, BlockPos pos) {
        ItemStack inputItem = this.input.getItemHandler().getStackInSlot(0);
        inputItem.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(cap -> {
            FluidStack simDrain = cap.drain(20, IFluidHandler.FluidAction.SIMULATE);
            if (simDrain.isEmpty()) {
                simDrain = cap.drain(1000, IFluidHandler.FluidAction.SIMULATE);
                if (!simDrain.isEmpty()) {
                    int doneFill = this.tank.getFluidHandler().fill(simDrain, IFluidHandler.FluidAction.SIMULATE);
                    if (doneFill == 1000) {
                        cap.drain(doneFill, IFluidHandler.FluidAction.EXECUTE);
                        this.tank.getFluidHandler().fill(simDrain, IFluidHandler.FluidAction.EXECUTE);
                    }
                }
            } else {
                int doneFill = this.tank.getFluidHandler().fill(simDrain, IFluidHandler.FluidAction.SIMULATE);
                cap.drain(doneFill, IFluidHandler.FluidAction.EXECUTE);
                this.tank.getFluidHandler().fill(simDrain, IFluidHandler.FluidAction.EXECUTE);
            }
            this.input.getItemHandler().setStackInSlot(0, cap.getContainer());
        });

        ItemStack outputItem = this.output.getItemHandler().getStackInSlot(0);
        outputItem.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(cap -> {
            FluidStack simDrain = this.tank.getFluidHandler().drain(20, IFluidHandler.FluidAction.SIMULATE);
            int filled = cap.fill(simDrain, IFluidHandler.FluidAction.SIMULATE);
            if (filled == 0) {
                simDrain = this.tank.getFluidHandler().drain(1000, IFluidHandler.FluidAction.SIMULATE);
                filled = cap.fill(simDrain, IFluidHandler.FluidAction.SIMULATE);
                if (filled == 1000) {
                    FluidStack drain = this.tank.getFluidHandler().drain(1000, IFluidHandler.FluidAction.EXECUTE);
                    cap.fill(drain, IFluidHandler.FluidAction.EXECUTE);
                }
            } else {
                FluidStack drain = this.tank.getFluidHandler().drain(20, IFluidHandler.FluidAction.EXECUTE);
                cap.fill(drain, IFluidHandler.FluidAction.EXECUTE);
            }
            this.output.getItemHandler().setStackInSlot(0, cap.getContainer());
        });
    }

}
