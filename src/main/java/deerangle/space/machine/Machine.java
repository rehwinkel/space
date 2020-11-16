package deerangle.space.machine;

import io.netty.buffer.Unpooled;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

public abstract class Machine {

    protected final List<Slot> slotList = new ArrayList<>();
    protected final SideConfig sideConfig;

    public Machine(SideConfig sideConfig) {
        this.sideConfig = sideConfig;
    }

    public CompoundNBT write(CompoundNBT compound) {
        CompoundNBT sideConfigNbt = new CompoundNBT();
        sideConfigNbt.putByte("F", (byte) sideConfig.getFront());
        sideConfigNbt.putByte("Ba", (byte) sideConfig.getBack());
        sideConfigNbt.putByte("R", (byte) sideConfig.getRight());
        sideConfigNbt.putByte("L", (byte) sideConfig.getLeft());
        sideConfigNbt.putByte("T", (byte) sideConfig.getTop());
        sideConfigNbt.putByte("Bo", (byte) sideConfig.getBottom());
        compound.put("SideConfig", sideConfigNbt);
        return compound;
    }

    public LazyOptional<IEnergyStorage> getEnergyStorage(Direction facing, Direction side) {
        int index = this.sideConfig.getIndexForSide(facing, side);
        if (index < 0) {
            return LazyOptional.empty();
        }
        Slot slot = slotList.get(index);
        if (slot instanceof EnergySlot) {
            return ((EnergySlot) slot).getStorage();
        }
        return LazyOptional.empty();
    }

    public LazyOptional<IItemHandler> getItemHandler(Direction facing, Direction side) {
        int index = this.sideConfig.getIndexForSide(facing, side);
        if (index < 0) {
            return LazyOptional.empty();
        }
        Slot slot = slotList.get(index);
        if (slot instanceof ItemSlot) {
            return ((ItemSlot) slot).getItemHandler();
        }
        return LazyOptional.empty();
    }

    public void writeGuiPacket(PacketBuffer buf) {
        buf.writeInt(12345);
    }

}
