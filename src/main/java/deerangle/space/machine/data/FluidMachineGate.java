package deerangle.space.machine.data;

import deerangle.space.machine.util.MachineFluidHandler;
import deerangle.space.machine.util.Ref;
import deerangle.space.machine.util.Restriction;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.function.Predicate;

public class FluidMachineGate {

    private final Ref<Integer> capacity;
    private final Ref<FluidStack> fluidStack;
    private final LazyOptional<IFluidHandler> unrestricted;
    private final LazyOptional<IFluidHandler> only_out;
    private final LazyOptional<IFluidHandler> only_in;

    public FluidMachineGate(int capacity, Predicate<FluidStack> validPredicate) {
        this.capacity = new Ref<>(capacity);
        this.fluidStack = new Ref<>(FluidStack.EMPTY);
        unrestricted = LazyOptional.of(() -> new MachineFluidHandler(this.fluidStack, this.capacity, validPredicate, Restriction.UNRESTRICTED));
        only_out = LazyOptional.of(() -> new MachineFluidHandler(this.fluidStack, this.capacity, validPredicate, Restriction.ONLY_OUT));
        only_in = LazyOptional.of(() -> new MachineFluidHandler(this.fluidStack, this.capacity, validPredicate, Restriction.ONLY_IN));
    }

    public LazyOptional<IFluidHandler> getFluidHandler(Restriction restriction) {
        switch (restriction) {
            case UNRESTRICTED:
                return unrestricted;
            case ONLY_OUT:
                return only_out;
            case ONLY_IN:
                return only_in;
        }
        return null;
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
