package deerangle.space.machine;

import deerangle.space.machine.data.EnergyMachineData;
import deerangle.space.machine.data.FluidMachineData;
import deerangle.space.machine.data.IMachineData;
import deerangle.space.machine.data.ItemMachineData;
import deerangle.space.machine.element.MachineType;
import deerangle.space.machine.util.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Machine implements IColorGetter {

    protected static final ITextComponent BUCKET_TEXT = new TranslationTextComponent("info.space.element.bucket");
    protected static final ITextComponent BATTERY_TEXT = new TranslationTextComponent("info.space.element.battery");
    protected static final ITextComponent TANK_TEXT = new TranslationTextComponent("info.space.element.tank");
    protected static final ITextComponent FUEL_TEXT = new TranslationTextComponent("info.space.element.fuel");
    protected static final ITextComponent ENERGY_TEXT = new TranslationTextComponent("info.space.element.energy");
    protected static final ITextComponent INPUT_TEXT = new TranslationTextComponent("info.space.element.input");
    protected static final ITextComponent OUTPUT_TEXT = new TranslationTextComponent("info.space.element.output");
    private static final int[][] COLORS = new int[][]{new int[]{0x0000ff, 0x007fff, 0x00ffff}, new int[]{0xff0000, 0xff7f00, 0xffff00}, new int[]{0xff00ff}, null};
    protected final SideConfig sideConfig;
    private final List<IMachineData> machineDataList = new ArrayList<>();
    private final MachineType<?> type;
    private final int[] indices;
    private ByteBuf prevState;
    private int prevHash;
    private FlowType prevFlowType = null;

    public Machine(MachineType<?> machineType) {
        this.sideConfig = new SideConfig();
        this.type = machineType;
        this.prevHash = -1;
        this.indices = new int[]{0, 0, 0, 0};
    }

    public Machine(MachineType<?> machineType, boolean... blocked) {
        assert blocked.length == 6;
        this.sideConfig = new SideConfig(blocked);
        this.type = machineType;
        this.prevHash = -1;
        this.indices = new int[]{0, 0, 0, 0};
    }

    private static Restriction getRestriction(boolean fromCapability, Accessor accessor) {
        return fromCapability ? (accessor
                .isInput() ? Restriction.ONLY_IN : Restriction.ONLY_OUT) : Restriction.UNRESTRICTED;
    }

    protected <MD extends IMachineData> MD addMachineData(MD data) {
        this.machineDataList.add(data);
        Accessor input = data.getInputAccessor();
        if (input != null) {
            this.sideConfig.addAccessor(input);
        }
        Accessor output = data.getOutputAccessor();
        if (output != null) {
            this.sideConfig.addAccessor(output);
        }
        return data;
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
        sideConfig.read(nbt.getCompound("SideConfig"));
        for (IMachineData slot : this.machineDataList) {
            slot.read(nbt.get(slot.getName()));
        }
    }

    public CompoundNBT write(CompoundNBT nbt) {
        nbt.put("SideConfig", this.sideConfig.write(new CompoundNBT()));
        for (IMachineData slot : this.machineDataList) {
            nbt.put(slot.getName(), slot.write());
        }
        return nbt;
    }

    public LazyOptional<IEnergyStorage> getEnergyStorage(Direction facing, Direction side, boolean fromCapability) {
        Accessor accessor = this.sideConfig.getAccessorForSide(facing, side);
        if (accessor == null) {
            return LazyOptional.empty();
        }
        IMachineData data = accessor.getAssociatedData();
        if (data instanceof EnergyMachineData) {
            return ((EnergyMachineData) data).getEnergyStorageOpt(getRestriction(fromCapability, accessor));
        }
        return LazyOptional.empty();
    }

    public LazyOptional<IItemHandlerModifiable> getItemHandler(Direction facing, Direction side, boolean fromCapability) {
        Accessor accessor = this.sideConfig.getAccessorForSide(facing, side);
        if (accessor == null) {
            return LazyOptional.empty();
        }
        IMachineData data = accessor.getAssociatedData();
        if (data instanceof ItemMachineData) {
            return ((ItemMachineData) data).getItemHandlerOpt(getRestriction(fromCapability, accessor));
        }
        return LazyOptional.empty();
    }

    public LazyOptional<IFluidHandler> getFluidHandler(Direction facing, Direction side, boolean fromCapability) {
        Accessor accessor = this.sideConfig.getAccessorForSide(facing, side);
        if (accessor == null) {
            return LazyOptional.empty();
        }
        IMachineData data = accessor.getAssociatedData();
        if (data instanceof FluidMachineData) {
            return ((FluidMachineData) data).getFluidHandlerOpt(getRestriction(fromCapability, accessor));
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

    public abstract void update(World world, BlockPos pos);

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
                IItemHandler itemHandler = ((ItemMachineData) slot).getItemHandler(Restriction.UNRESTRICTED);
                for (int i = 0; i < itemHandler.getSlots(); i++) {
                    InventoryHelper.spawnItemStack(world, dropPos.getX(), dropPos.getY(), dropPos.getZ(),
                            itemHandler.getStackInSlot(i));
                }
            }
        }
    }

    @Override
    public int getNextColor(int hash, FlowType flowType, ColorType colorType) {
        if (flowType == FlowType.NONE) {
            return 0x333333;
        }
        if (hash != prevHash && prevHash != -1 && flowType == prevFlowType) {
            this.indices[flowType.ordinal()]++;
        }
        prevHash = hash;
        prevFlowType = flowType;
        int color = Objects.requireNonNull(COLORS[flowType.ordinal()])[this.indices[flowType.ordinal()]];
        if (flowType == FlowType.INOUT) {
            switch (colorType) {
                case GUI:
                    return color;
                case IN:
                    return 0xff007f;
                case OUT:
                    return 0x7f00ff;
            }
        }
        return color;
    }

}
