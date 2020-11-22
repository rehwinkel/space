package deerangle.space.machine.data;

import deerangle.space.machine.util.FlowType;
import deerangle.space.machine.util.IColorGetter;
import deerangle.space.machine.util.Restriction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.function.Predicate;

public class ItemMachineData extends BaseMachineData {

    private final ItemMachineGate stack;

    public ItemMachineData(String name, Predicate<ItemStack> validPredicate, FlowType flowType, IColorGetter colorGetter, ITextComponent dataName) {
        super(name, flowType, colorGetter, dataName);
        stack = new ItemMachineGate(validPredicate);
    }

    public ItemMachineData(String name, FlowType flowType, IColorGetter colorGetter, ITextComponent dataName) {
        this(name, stack -> true, flowType, colorGetter, dataName);
    }

    public LazyOptional<IItemHandlerModifiable> getItemHandlerOpt(Restriction restriction) {
        return this.stack.getItemHandler(restriction);
    }

    public IItemHandlerModifiable getItemHandler(Restriction restriction) {
        return this.getItemHandlerOpt(restriction).orElseThrow(() -> new RuntimeException("Optional wasn't present"));
    }

    public IItemHandlerModifiable getItemHandler() {
        return this.getItemHandler(Restriction.UNRESTRICTED);
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
    public void writePacket(PacketBuffer buf) {
        buf.writeItemStack(stack.getStack());
    }

    @Override
    public void readPacket(PacketBuffer buf) {
        stack.setStack(buf.readItemStack());
    }

}
