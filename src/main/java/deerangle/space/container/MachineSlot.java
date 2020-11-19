package deerangle.space.container;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.function.Predicate;

public class MachineSlot extends SlotItemHandler {

    private final Predicate<ItemStack> validPredicate;

    public MachineSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, Predicate<ItemStack> validPredicate) {
        super(itemHandler, index, xPosition, yPosition);
        this.validPredicate = validPredicate;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return this.validPredicate.test(stack);
    }

}
