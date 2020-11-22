package deerangle.space.machine.util;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.function.Predicate;

public class MachineFluidHandler implements IFluidHandler {

    private final Ref<Integer> capacity;
    private final Ref<FluidStack> fluid;
    private final Predicate<FluidStack> validPredicate;
    private final Restriction restriction;

    public MachineFluidHandler(Ref<FluidStack> fluidStack, Ref<Integer> capacity, Predicate<FluidStack> validPredicate, Restriction restriction) {
        this.capacity = capacity;
        this.fluid = fluidStack;
        this.validPredicate = validPredicate;
        this.restriction = restriction;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return this.fluid.get();
    }

    @Override
    public int getTankCapacity(int tank) {
        return this.capacity.get();
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return isFluidValid(stack);
    }

    private boolean isFluidValid(FluidStack stack) {
        return this.validPredicate.test(stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (this.restriction == Restriction.ONLY_OUT) {
            return 0;
        }
        if (resource.isEmpty() || !isFluidValid(resource)) {
            return 0;
        }
        if (action.simulate()) {
            if (fluid.get().isEmpty()) {
                return Math.min(capacity.get(), resource.getAmount());
            }
            if (!fluid.get().isFluidEqual(resource)) {
                return 0;
            }
            return Math.min(capacity.get() - fluid.get().getAmount(), resource.getAmount());
        }
        if (fluid.get().isEmpty()) {
            fluid.set(new FluidStack(resource, Math.min(capacity.get(), resource.getAmount())));
            return fluid.get().getAmount();
        }
        if (!fluid.get().isFluidEqual(resource)) {
            return 0;
        }
        int filled = capacity.get() - fluid.get().getAmount();

        if (resource.getAmount() < filled) {
            fluid.get().grow(resource.getAmount());
            filled = resource.getAmount();
        } else {
            fluid.get().setAmount(capacity.get());
        }
        return filled;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (this.restriction == Restriction.ONLY_IN) {
            return FluidStack.EMPTY;
        }
        if (resource.isEmpty() || !resource.isFluidEqual(fluid.get())) {
            return FluidStack.EMPTY;
        }
        return drain(resource.getAmount(), action);
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        if (this.restriction == Restriction.ONLY_IN) {
            return FluidStack.EMPTY;
        }
        int drained = maxDrain;
        if (fluid.get().getAmount() < drained) {
            drained = fluid.get().getAmount();
        }
        FluidStack stack = new FluidStack(fluid.get(), drained);
        if (action.execute() && drained > 0) {
            fluid.get().shrink(drained);
        }
        return stack;
    }

}
