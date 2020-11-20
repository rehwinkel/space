package deerangle.space.machine.data;

import deerangle.space.machine.util.FlowType;
import deerangle.space.machine.util.MachineItemHandler;
import deerangle.space.machine.util.Ref;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.function.Predicate;

public class ItemMachineGate {

    private final Ref<ItemStack> stack;
    private final LazyOptional<IItemHandlerModifiable> restricted;
    private final LazyOptional<IItemHandlerModifiable> unrestricted;

    public ItemMachineGate(Predicate<ItemStack> validPredicate, FlowType flowType) {
        stack = new Ref<>(ItemStack.EMPTY);
        restricted = LazyOptional.of(() -> new MachineItemHandler(this.stack, validPredicate, flowType));
        unrestricted = LazyOptional.of(() -> new MachineItemHandler(this.stack, validPredicate, null));
    }

    public LazyOptional<IItemHandlerModifiable> getItemHandler(boolean fromCapability) {
        if (fromCapability) {
            return restricted;
        } else {
            return unrestricted;
        }
    }

    public ItemStack getStack() {
        return stack.get();
    }

    public void setStack(ItemStack stack) {
        this.stack.set(stack);
    }

}
