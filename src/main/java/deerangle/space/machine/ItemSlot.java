package deerangle.space.machine;

import net.minecraft.nbt.INBT;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ItemSlot extends Slot {

    private LazyOptional<IItemHandler> stack;

    public ItemSlot(int x, int y, boolean input) {
        super(x, y, input);
        stack = LazyOptional.of(ItemStackHandler::new);
    }

    @Override
    public INBT write() {
        return ((ItemStackHandler) stack.orElseThrow(() -> new RuntimeException("failed to write item slot")))
                .serializeNBT();
    }

    public LazyOptional<IItemHandler> getItemHandler() {
        return this.stack;
    }
}
