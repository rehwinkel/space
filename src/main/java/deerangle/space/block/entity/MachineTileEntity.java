package deerangle.space.block.entity;

import deerangle.space.block.MachineBlock;
import deerangle.space.container.MachineContainer;
import deerangle.space.machine.Machine;
import deerangle.space.machine.data.EnergyMachineData;
import deerangle.space.machine.data.FluidMachineData;
import deerangle.space.machine.data.IMachineData;
import deerangle.space.machine.data.ItemMachineData;
import deerangle.space.machine.element.MachineType;
import deerangle.space.machine.util.Accessor;
import deerangle.space.registry.MachineRegistry;
import io.netty.buffer.Unpooled;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

import java.util.Objects;

public class MachineTileEntity extends TileEntity implements INamedContainerProvider, ICapabilityProvider, ITickableTileEntity {

    public static final int ITEM_PUSH_COOLDOWN = 8;
    public static final int ITEM_PUSH_SIZE = 4;
    public static final int FLUID_PER_TICK = 10;

    private Machine machine;
    private String machineName;
    private int pushCooldown;

    public MachineTileEntity(String machineName, MachineType<?> machineType) {
        super(MachineRegistry.MACHINE_TE.get());
        this.machine = machineType.getNewInstance();
        this.machineName = machineName;
        this.pushCooldown = 0;
    }

    public MachineTileEntity() {
        super(MachineRegistry.MACHINE_TE.get());
        this.machineName = "unknown";
        this.pushCooldown = 0;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == CapabilityEnergy.ENERGY) {
            return machine.getEnergyStorage(this.getBlockState().get(MachineBlock.FACING), side, true).cast();
        }
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return machine.getItemHandler(this.getBlockState().get(MachineBlock.FACING), side, true).cast();
        }
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return machine.getFluidHandler(this.getBlockState().get(MachineBlock.FACING), side, true).cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        super.write(nbt);
        nbt.putString("MachineType", this.machine.getType().getRegistryName().toString());
        nbt.putString("Name", this.machineName);
        return machine.write(nbt);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        IForgeRegistry<MachineType<?>> registry = RegistryManager.ACTIVE.getRegistry(MachineType.class);
        MachineType<?> machineType = Objects
                .requireNonNull(registry.getValue(new ResourceLocation(nbt.getString("MachineType"))));
        this.machine = machineType.getNewInstance();
        this.machineName = nbt.getString("Name");
        machine.read(nbt);
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        // left empty to avoid client autosync
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("block.space." + machineName);
    }

    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
        PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
        this.writeGuiPacket(buf);
        return new MachineContainer(windowId, playerInventory, buf, this.machine);
    }

    public void writeGuiPacket(PacketBuffer buf) {
        buf.writeBlockPos(getPos());
        buf.writeResourceLocation(Objects.requireNonNull(this.machine.getType().getRegistryName()));
    }

    public Machine getMachine() {
        return this.machine;
    }

    @Override
    public void tick() {
        if (!this.world.isRemote) {
            this.machine.update(this.world, this.pos);

            for (Direction dir : Direction.values()) {
                handlePush(dir);
            }
        }
    }

    private void handlePush(Direction dir) {
        Accessor accessor = this.machine.getSideConfig()
                .getAccessorForSide(this.getBlockState().get(MachineBlock.FACING), dir);
        if (accessor == null) {
            return;
        }
        IMachineData data = accessor.getAssociatedData();
        if (!accessor.isInput()) {
            TileEntity tileEntity = this.getWorld().getTileEntity(getPos().offset(dir));
            if (tileEntity == null) {
                return;
            }
            if (data instanceof ItemMachineData && this.pushCooldown == 0) {
                handleItemPush(((ItemMachineData) data).getItemHandler(),
                        tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir.getOpposite()));
            } else if (data instanceof EnergyMachineData) {
                handleEnergyPush(((EnergyMachineData) data).getEnergyStorage(),
                        tileEntity.getCapability(CapabilityEnergy.ENERGY, dir.getOpposite()));
            } else if (data instanceof FluidMachineData) {
                handleFluidPush(((FluidMachineData) data).getFluidHandler(),
                        tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, dir.getOpposite()));
            }
        }
        if (this.pushCooldown > 0) {
            this.pushCooldown--;
        }
    }

    private void handleFluidPush(IFluidHandler fluidHandler, LazyOptional<IFluidHandler> otherHandlerOpt) {
        if (otherHandlerOpt.isPresent()) {
            FluidStack drainedSim = fluidHandler.drain(FLUID_PER_TICK, IFluidHandler.FluidAction.SIMULATE);
            IFluidHandler otherHandler = otherHandlerOpt.orElse(null);
            int filled = otherHandler.fill(drainedSim, IFluidHandler.FluidAction.SIMULATE);
            otherHandler.fill(fluidHandler.drain(filled, IFluidHandler.FluidAction.EXECUTE),
                    IFluidHandler.FluidAction.EXECUTE);
        }
    }

    private void handleEnergyPush(IEnergyStorage energyStorage, LazyOptional<IEnergyStorage> otherStorageOpt) {
        if (otherStorageOpt.isPresent()) {
            int simExtracted = energyStorage.extractEnergy(energyStorage.getEnergyStored(), true);
            IEnergyStorage otherStorage = otherStorageOpt.orElse(null);
            int simAccepted = otherStorage.receiveEnergy(simExtracted, true);
            otherStorage.receiveEnergy(energyStorage.extractEnergy(simAccepted, false), false);
        }
    }

    private void handleItemPush(IItemHandler itemHandler, LazyOptional<IItemHandler> otherHandlerOpt) {
        if (otherHandlerOpt.isPresent()) {
            IItemHandler otherHandler = otherHandlerOpt.orElse(null);
            ItemStack extractedSim = itemHandler.extractItem(0, ITEM_PUSH_SIZE, true);
            if (extractedSim.isEmpty()) {
                return;
            }
            ItemStack rest = extractedSim;
            for (int slot = 0; slot < otherHandler.getSlots(); slot++) {
                rest = otherHandler.insertItem(slot, rest, true);
                if (rest.isEmpty()) {
                    break;
                }
            }
            int toTransfer = extractedSim.getCount() - rest.getCount();
            if (toTransfer == 0) {
                return;
            }
            ItemStack toInsert = itemHandler.extractItem(0, toTransfer, false);
            for (int slot = 0; slot < otherHandler.getSlots(); slot++) {
                toInsert = otherHandler.insertItem(slot, toInsert, false);
                if (toInsert.isEmpty()) {
                    break;
                }
            }
            this.pushCooldown = ITEM_PUSH_COOLDOWN;
        }
    }

}
