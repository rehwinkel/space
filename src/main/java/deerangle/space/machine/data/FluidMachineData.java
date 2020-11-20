package deerangle.space.machine.data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.function.Predicate;

public class FluidMachineData implements IMachineData {

    private final String name;
    private final LazyOptional<IFluidHandler> tank;

    //TODO: inout behavior
    public FluidMachineData(String name, int capacity, Predicate<FluidStack> validPredicate) {
        this.tank = LazyOptional.of(() -> new FluidTank(capacity, validPredicate));
        this.name = name;
    }

    public LazyOptional<IFluidHandler> getTank() {
        return tank;
    }

    public IFluidHandler getTankOrThrow() {
        return tank.orElseThrow(() -> new RuntimeException("failed to get energy storage"));
    }

    @Override
    public INBT write() {
        return ((FluidTank) this.getTankOrThrow()).writeToNBT(new CompoundNBT());
    }

    @Override
    public void read(INBT nbt) {
        ((FluidTank) this.getTankOrThrow()).readFromNBT((CompoundNBT) nbt);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void writePacket(PacketBuffer buf) {
        buf.writeFluidStack(this.getTankOrThrow().getFluidInTank(0));
    }

    @Override
    public void readPacket(PacketBuffer buf) {
        FluidStack fluid = buf.readFluidStack();
        ((FluidTank) this.getTankOrThrow()).setFluid(fluid);
    }

    @Override
    public boolean storeInItem() {
        return true;
    }

    public int getCapacity() {
        return this.getTankOrThrow().getTankCapacity(0);
    }

    public FluidStack getFluidStack() {
        return this.getTankOrThrow().getFluidInTank(0);
    }

}
