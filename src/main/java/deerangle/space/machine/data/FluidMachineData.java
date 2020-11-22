package deerangle.space.machine.data;

import deerangle.space.machine.util.FlowType;
import deerangle.space.machine.util.IColorGetter;
import deerangle.space.machine.util.Restriction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.function.Predicate;

public class FluidMachineData extends BaseMachineData {

    private final FluidMachineGate tank;

    public FluidMachineData(String name, int capacity, Predicate<FluidStack> validPredicate, FlowType flowType, IColorGetter colorGetter, ITextComponent dataName) {
        super(name, flowType, colorGetter, dataName);
        this.tank = new FluidMachineGate(capacity, validPredicate);
    }


    public LazyOptional<IFluidHandler> getFluidHandlerOpt(Restriction restriction) {
        return this.tank.getFluidHandler(restriction);
    }

    public IFluidHandler getFluidHandler(Restriction restriction) {
        return this.getFluidHandlerOpt(restriction).orElseThrow(() -> new RuntimeException("Optional wasn't present"));
    }

    public IFluidHandler getFluidHandler() {
        return this.getFluidHandler(Restriction.UNRESTRICTED);
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

    public int getCapacity() {
        return this.tank.getCapacity();
    }

    public FluidStack getFluidStack() {
        return this.tank.getFluid();
    }

}
