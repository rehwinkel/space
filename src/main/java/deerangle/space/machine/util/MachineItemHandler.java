package deerangle.space.machine.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class MachineItemHandler implements IItemHandlerModifiable {

    private final Ref<ItemStack> stack;
    private final Predicate<ItemStack> validPredicate;
    private final FlowType flowType;

    public MachineItemHandler(Ref<ItemStack> stack, Predicate<ItemStack> validPredicate, FlowType flowType) {
        this.stack = stack;
        this.validPredicate = validPredicate;
        this.flowType = flowType;
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        this.stack.set(stack);
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Override
    @Nonnull
    public ItemStack getStackInSlot(int slot) {
        return this.stack.get();
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (flowType == FlowType.OUTPUT) {
            return stack;
        }
        if (stack.isEmpty())
            return ItemStack.EMPTY;

        if (!isItemValid(slot, stack))
            return stack;

        ItemStack existing = this.stack.get();

        int limit = getStackLimit(slot, stack);

        if (!existing.isEmpty()) {
            if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
                return stack;

            limit -= existing.getCount();
        }

        if (limit <= 0)
            return stack;

        boolean reachedLimit = stack.getCount() > limit;

        if (!simulate) {
            if (existing.isEmpty()) {
                this.stack.set(reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
            } else {
                existing.grow(reachedLimit ? limit : stack.getCount());
            }
        }

        return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (flowType == FlowType.INPUT) {
            return ItemStack.EMPTY;
        }
        if (amount == 0)
            return ItemStack.EMPTY;

        ItemStack existing = this.stack.get();

        if (existing.isEmpty())
            return ItemStack.EMPTY;

        int toExtract = Math.min(amount, existing.getMaxStackSize());

        if (existing.getCount() <= toExtract) {
            if (!simulate) {
                this.stack.set(ItemStack.EMPTY);
                return existing;
            } else {
                return existing.copy();
            }
        } else {
            if (!simulate) {
                this.stack.set(ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
            }

            return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
        return Math.min(getSlotLimit(slot), stack.getMaxStackSize());
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return this.validPredicate.test(stack);
    }

}
