package deerangle.space.machine.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.function.Predicate;

public class MachineItemHandler implements IItemHandler {

    private final Predicate<ItemStack> validPredicate;
    private final boolean input;
    private final ItemStackHandler internal;

    public MachineItemHandler(Predicate<ItemStack> validPredicate, boolean input) {
        this.internal = new ItemStackHandler();
        this.validPredicate = validPredicate;
        this.input = input;
    }

    @Override
    public int getSlots() {
        return this.internal.getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.internal.getStackInSlot(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (this.input) {
            if (!this.isItemValid(slot, stack))
                return stack;
            return this.internal.insertItem(slot, stack, simulate);
        }
        return stack;
    }

    public ItemStack insertItemOverride(int slot, ItemStack stack, boolean simulate) {
        if (!this.isItemValid(slot, stack))
            return stack;
        return this.internal.insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return this.internal.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return this.internal.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return this.validPredicate.test(stack);
    }

}
