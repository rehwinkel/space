package deerangle.space.machine.data;

import deerangle.space.machine.util.MachineItemHandler;
import deerangle.space.machine.util.Ref;
import deerangle.space.machine.util.Restriction;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.function.Predicate;

public class ItemMachineGate {

    private final Ref<ItemStack> stack;
    private final LazyOptional<IItemHandlerModifiable> unrestricted;
    private final LazyOptional<IItemHandlerModifiable> only_out;
    private final LazyOptional<IItemHandlerModifiable> only_in;

    public ItemMachineGate(Predicate<ItemStack> validPredicate) {
        stack = new Ref<>(ItemStack.EMPTY);
        unrestricted = LazyOptional.of(() -> new MachineItemHandler(this.stack, validPredicate, Restriction.UNRESTRICTED));
        only_out = LazyOptional.of(() -> new MachineItemHandler(this.stack, validPredicate, Restriction.ONLY_OUT));
        only_in = LazyOptional.of(() -> new MachineItemHandler(this.stack, validPredicate, Restriction.ONLY_IN));
    }

    public LazyOptional<IItemHandlerModifiable> getItemHandler(Restriction restriction) {
        switch (restriction) {
            case UNRESTRICTED:
                return unrestricted;
            case ONLY_OUT:
                return only_out;
            case ONLY_IN:
                return only_in;
        }
        return null;
    }

    public ItemStack getStack() {
        return stack.get();
    }

    public void setStack(ItemStack stack) {
        this.stack.set(stack);
    }

}
