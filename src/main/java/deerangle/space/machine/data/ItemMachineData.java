package deerangle.space.machine.data;

import deerangle.space.machine.util.FlowType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.function.Predicate;

public class ItemMachineData implements IMachineData {

    private final String name;
    private final ItemMachineGate stack;
    private final FlowType flowType;

    public ItemMachineData(String name, Predicate<ItemStack> validPredicate, FlowType flowType) {
        stack = new ItemMachineGate(validPredicate, flowType);
        this.flowType = flowType;
        this.name = name;
    }

    public FlowType getFlowType() {
        return flowType;
    }

    public ItemMachineData(String name, FlowType flowType) {
        this(name, stack -> true, flowType);
    }

    public LazyOptional<IItemHandlerModifiable> getItemHandler(boolean fromCapability) {
        return stack.getItemHandler(fromCapability);
    }

    public IItemHandlerModifiable getItemHandlerForce(boolean fromCapability) {
        return this.getItemHandler(fromCapability)
                .orElseThrow(() -> new RuntimeException("failed to get fluid handler"));
    }

    public IItemHandlerModifiable getItemHandlerForce() {
        return this.getItemHandlerForce(false);
    }

    @Override
    public INBT write() {
        return stack.getStack().write(new CompoundNBT());
    }

    @Override
    public void read(INBT nbt) {
        stack.setStack(ItemStack.read((CompoundNBT) nbt));
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void writePacket(PacketBuffer buf) {
        buf.writeItemStack(stack.getStack());
    }

    @Override
    public void readPacket(PacketBuffer buf) {
        stack.setStack(buf.readItemStack());
    }

    @Override
    public boolean storeInItem() {
        return false;
    }

}
