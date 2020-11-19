package deerangle.space.machine.data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ItemMachineData implements IMachineData {

    private final String name;
    private LazyOptional<IItemHandler> stack;

    public ItemMachineData(String name) {
        stack = LazyOptional.of(ItemStackHandler::new);
        this.name = name;
    }

    public LazyOptional<IItemHandler> getItemHandler() {
        return this.stack;
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
