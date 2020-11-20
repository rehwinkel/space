package deerangle.space.block.entity;

import deerangle.space.block.MachineBlock;
import deerangle.space.container.MachineContainer;
import deerangle.space.machine.Machine;
import deerangle.space.machine.element.MachineType;
import deerangle.space.registry.MachineRegistry;
import io.netty.buffer.Unpooled;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
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
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

import java.util.Objects;

public class MachineTileEntity extends TileEntity implements INamedContainerProvider, ICapabilityProvider, ITickableTileEntity {

    private Machine machine;
    private String machineName;

    public MachineTileEntity(String machineName, MachineType<?> machineType) {
        super(MachineRegistry.MACHINE_TE.get());
        this.machine = machineType.getNewInstance();
        this.machineName = machineName;
    }

    public MachineTileEntity() {
        super(MachineRegistry.MACHINE_TE.get());
        this.machineName = "unknown";
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == CapabilityEnergy.ENERGY) {
            return machine.getEnergyStorage(this.getBlockState().get(MachineBlock.FACING), side, true).cast();
        }
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return machine.getItemHandler(this.getBlockState().get(MachineBlock.FACING), side, true).cast();
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
        }
    }

}
