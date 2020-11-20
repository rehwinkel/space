package deerangle.space.machine.data;

import deerangle.space.machine.util.FlowType;
import deerangle.space.machine.util.MachineFluidHandler;
import deerangle.space.machine.util.Ref;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.function.Predicate;

public class FluidMachineGate {

    private final Ref<Integer> capacity;
    private final Ref<FluidStack> fluidStack;
    private final LazyOptional<IFluidHandler> restricted;
    private final LazyOptional<IFluidHandler> unrestricted;

    public FluidMachineGate(int capacity, Predicate<FluidStack> validPredicate, FlowType flowType) {
        this.capacity = new Ref<>(capacity);
        this.fluidStack = new Ref<>(FluidStack.EMPTY);
        restricted = LazyOptional
                .of(() -> new MachineFluidHandler(this.fluidStack, this.capacity, validPredicate, flowType));
        unrestricted = LazyOptional
                .of(() -> new MachineFluidHandler(this.fluidStack, this.capacity, validPredicate, null));
    }

    public LazyOptional<IFluidHandler> getFluidHandler(boolean fromCapability) {
        if (fromCapability) {
            return restricted;
        } else {
            return unrestricted;
        }
    }

    public int getCapacity() {
        return this.capacity.get();
    }

    public FluidStack getFluid() {
        return fluidStack.get();
    }

    public void setFluid(FluidStack fluid) {
        this.fluidStack.set(fluid);
    }

}
