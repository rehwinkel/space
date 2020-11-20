package deerangle.space.machine.data;

import deerangle.space.machine.util.MachineItemHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.function.Predicate;

public class ItemMachineData implements IMachineData {

    private final String name;
    private LazyOptional<IItemHandler> stack;

    public ItemMachineData(String name, Predicate<ItemStack> validPredicate, boolean input) {
        stack = LazyOptional.of(() -> new MachineItemHandler(validPredicate, input));
        this.name = name;
    }

    public ItemMachineData(String name, Predicate<ItemStack> validPredicate) {
        this(name, validPredicate, true);
    }

    public ItemMachineData(String name, boolean input) {
        this(name, stack -> true, input);
    }

    public ItemMachineData(String name) {
        this(name, stack -> true, true);
    }

    public LazyOptional<IItemHandler> getItemHandler() {
        return this.stack;
    }

    public IItemHandler getItemHandlerOrThrow() {
        return this.stack.orElseThrow(() -> new RuntimeException("failed to get item handler"));
    }

    public MachineItemHandler getMachineItemHandler() {
        return (MachineItemHandler) this.getItemHandlerOrThrow();
    }

    @Override
    public INBT write() {
        return ((ItemStackHandler) stack.orElseThrow(() -> new RuntimeException("failed to write item slot")))
                .serializeNBT();
    }

    @Override
    public void read(INBT nbt) {
        ((ItemStackHandler) stack.orElseThrow(() -> new RuntimeException("failed to write item slot")))
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

}
