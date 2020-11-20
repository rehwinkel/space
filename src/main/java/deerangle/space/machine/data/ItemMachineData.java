package deerangle.space.machine.data;

import deerangle.space.machine.util.FlowType;
import deerangle.space.machine.util.MachineItemHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

import java.util.function.Predicate;

public class ItemMachineData implements IMachineData {

    private final String name;
    private LazyOptional<IItemHandler> stack;

    public ItemMachineData(String name, Predicate<ItemStack> validPredicate, FlowType flowType) {
        stack = LazyOptional.of(() -> new MachineItemHandler(validPredicate, flowType));
        this.name = name;
    }

    public ItemMachineData(String name, Predicate<ItemStack> validPredicate) {
        this(name, validPredicate, FlowType.INPUT);
    }

    public ItemMachineData(String name, FlowType flowType) {
        this(name, stack -> true, flowType);
    }

    public ItemMachineData(String name) {
        this(name, stack -> true, FlowType.INPUT);
    }

    public LazyOptional<IItemHandler> getItemHandler() {
        return this.stack;
    }

    public IItemHandler getItemHandlerOrThrow() {
        return this.stack.orElseThrow(() -> new RuntimeException("failed to get item handler"));
    }

    @Override
    public INBT write() {
        return ((MachineItemHandler) stack.orElseThrow(() -> new RuntimeException("failed to write item slot")))
                .serializeNBT();
    }

    @Override
    public void read(INBT nbt) {
        ((MachineItemHandler) stack.orElseThrow(() -> new RuntimeException("failed to write item slot")))
                .deserializeNBT((CompoundNBT) nbt);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void writePacket(PacketBuffer buf) {

    }

    @Override
    public void readPacket(PacketBuffer buf) {

    }

    @Override
    public boolean storeInItem() {
        return false;
    }

    public MachineItemHandler getMachineItemHandler() {
        return (MachineItemHandler) this.getItemHandlerOrThrow(); //TODO remove
    }

}
