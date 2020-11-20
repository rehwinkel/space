package deerangle.space.machine.data;

import deerangle.space.machine.util.FlowType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.function.Predicate;

public class FluidMachineData implements IMachineData {

    private final String name;
    private final FluidMachineGate tank;

    public FluidMachineData(String name, int capacity, Predicate<FluidStack> validPredicate, FlowType flowType) {
        this.tank = new FluidMachineGate(capacity, validPredicate, flowType);
        this.name = name;
    }

    public LazyOptional<IFluidHandler> getFluidHandler(boolean fromCapability) {
        return tank.getFluidHandler(fromCapability);
    }

    public IFluidHandler getFluidHandlerForce(boolean fromCapability) {
        return this.getFluidHandler(fromCapability)
                .orElseThrow(() -> new RuntimeException("failed to get fluid handler"));
    }

    public IFluidHandler getFluidHandlerForce() {
        return this.getFluidHandlerForce(false);
    }

    @Override
    public INBT write() {
        return this.tank.getFluid().writeToNBT(new CompoundNBT());
    }

    @Override
    public void read(INBT nbt) {
        FluidStack fluid = FluidStack.loadFluidStackFromNBT((CompoundNBT) nbt);
        this.tank.setFluid(fluid);
    }

    @Override
    public void writePacket(PacketBuffer buf) {
        buf.writeFluidStack(this.tank.getFluid());
    }

    @Override
    public void readPacket(PacketBuffer buf) {
        this.tank.setFluid(buf.readFluidStack());
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean storeInItem() {
        return true;
    }

    public int getCapacity() {
        return this.tank.getCapacity();
    }

    public FluidStack getFluidStack() {
        return this.tank.getFluid();
    }

}
