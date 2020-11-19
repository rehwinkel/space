package deerangle.space.machine;

import deerangle.space.machine.data.EnergyMachineData;
import deerangle.space.machine.data.IMachineData;
import deerangle.space.machine.data.ItemMachineData;
import deerangle.space.machine.element.MachineType;
import deerangle.space.machine.util.SideConfig;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

public abstract class Machine {

    protected final List<IMachineData> machineDataList = new ArrayList<>();
    protected final SideConfig sideConfig;
    private final MachineType<?> type;
    private ByteBuf prevState;

    public Machine(MachineType<?> machineType, SideConfig sideConfig) {
        this.sideConfig = sideConfig;
        this.type = machineType;
    }

    public IMachineData getMachineData(int index) {
        return machineDataList.get(index);
    }

    public int getMachineDataSize() {
        return this.machineDataList.size();
    }

    public MachineType<?> getType() {
        return this.type;
    }

    public void read(CompoundNBT nbt) {
        CompoundNBT sideConfigNbt = nbt.getCompound("SideConfig");
        sideConfig.setFront(sideConfigNbt.getByte("F"));
        sideConfig.setBack(sideConfigNbt.getByte("Ba"));
        sideConfig.setRight(sideConfigNbt.getByte("R"));
        sideConfig.setLeft(sideConfigNbt.getByte("L"));
        sideConfig.setTop(sideConfigNbt.getByte("T"));
        sideConfig.setBottom(sideConfigNbt.getByte("Bo"));

        for (IMachineData slot : this.machineDataList) {
            slot.read(nbt.get(slot.getName()));
        }
    }

    public CompoundNBT write(CompoundNBT nbt) {
        CompoundNBT sideConfigNbt = new CompoundNBT();
        sideConfigNbt.putByte("F", (byte) sideConfig.getFront());
        sideConfigNbt.putByte("Ba", (byte) sideConfig.getBack());
        sideConfigNbt.putByte("R", (byte) sideConfig.getRight());
        sideConfigNbt.putByte("L", (byte) sideConfig.getLeft());
        sideConfigNbt.putByte("T", (byte) sideConfig.getTop());
        sideConfigNbt.putByte("Bo", (byte) sideConfig.getBottom());
        nbt.put("SideConfig", sideConfigNbt);

        for (IMachineData slot : this.machineDataList) {
            nbt.put(slot.getName(), slot.write());
        }
        return nbt;
    }

    public LazyOptional<IEnergyStorage> getEnergyStorage(Direction facing, Direction side) {
        int index = this.sideConfig.getIndexForSide(facing, side);
        if (index < 0) {
            return LazyOptional.empty();
        }
        IMachineData slot = machineDataList.get(index);
        if (slot instanceof EnergyMachineData) {
            return ((EnergyMachineData) slot).getStorage();
        }
        return LazyOptional.empty();
    }

    public LazyOptional<IItemHandler> getItemHandler(Direction facing, Direction side) {
        int index = this.sideConfig.getIndexForSide(facing, side);
        if (index < 0) {
            return LazyOptional.empty();
        }
        IMachineData slot = machineDataList.get(index);
        if (slot instanceof ItemMachineData) {
            return ((ItemMachineData) slot).getItemHandler();
        }
        return LazyOptional.empty();
    }

    public void writePacket(PacketBuffer buf) {
        sideConfig.writePacket(buf);
        for (IMachineData iMachineData : this.machineDataList) {
            iMachineData.writePacket(buf);
        }
    }

    public void readPacket(PacketBuffer buf) {
        sideConfig.readPacket(buf);
        for (IMachineData iMachineData : this.machineDataList) {
            iMachineData.readPacket(buf);
        }
    }

    public abstract void update();

    public SideConfig getSideConfig() {
        return this.sideConfig;
    }

    public boolean shouldSync() {
        if (this.prevState == null) {
            this.prevState = Unpooled.buffer();
            this.writePacket(new PacketBuffer(prevState));
            return true;
        }
        ByteBuf currentState = Unpooled.buffer();
        this.writePacket(new PacketBuffer(currentState));
        if (this.prevState.hashCode() != currentState.hashCode()) {
            this.prevState = currentState;
            return true;
        }
        return false;
    }

    public void dropInventoryItems(World world, BlockPos dropPos) {
        for (IMachineData slot : machineDataList) {
            if (slot instanceof ItemMachineData) {
                IItemHandler itemHandler = ((ItemMachineData) slot).getItemHandler()
                        .orElseThrow(() -> new RuntimeException("failed to get item handler"));
                for (int i = 0; i < itemHandler.getSlots(); i++) {
                    InventoryHelper.spawnItemStack(world, dropPos.getX(), dropPos.getY(), dropPos.getZ(),
                            itemHandler.getStackInSlot(i));
                }
            }
        }
    }

    public CompoundNBT saveItemNBT(CompoundNBT nbt) {
        CompoundNBT sideConfigNbt = new CompoundNBT();
        sideConfigNbt.putByte("F", (byte) sideConfig.getFront());
        sideConfigNbt.putByte("Ba", (byte) sideConfig.getBack());
        sideConfigNbt.putByte("R", (byte) sideConfig.getRight());
        sideConfigNbt.putByte("L", (byte) sideConfig.getLeft());
        sideConfigNbt.putByte("T", (byte) sideConfig.getTop());
        sideConfigNbt.putByte("Bo", (byte) sideConfig.getBottom());
        nbt.put("SideConfig", sideConfigNbt);

        for (IMachineData slot : this.machineDataList) {
            if (slot.storeInItem()) {
                nbt.put(slot.getName(), slot.write());
            }
        }
        saveExtraItemNBT(nbt);
        return nbt;
    }

    public void loadItemNBT(CompoundNBT nbt) {
        CompoundNBT sideConfigNbt = nbt.getCompound("SideConfig");
        sideConfig.setFront(sideConfigNbt.getByte("F"));
        sideConfig.setBack(sideConfigNbt.getByte("Ba"));
        sideConfig.setRight(sideConfigNbt.getByte("R"));
        sideConfig.setLeft(sideConfigNbt.getByte("L"));
        sideConfig.setTop(sideConfigNbt.getByte("T"));
        sideConfig.setBottom(sideConfigNbt.getByte("Bo"));

        for (IMachineData slot : this.machineDataList) {
            if (slot.storeInItem()) {
                slot.read(nbt.get(slot.getName()));
            }
        }
        loadExtraItemNBT(nbt);
    }

    protected void saveExtraItemNBT(CompoundNBT nbt) {
    }

    protected void loadExtraItemNBT(CompoundNBT nbt) {
    }

}
