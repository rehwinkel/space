package deerangle.space.block.entity;

import deerangle.space.container.CoalGeneratorContainer;
import deerangle.space.machine.Machine;
import deerangle.space.machine.MachineTypeRegistry;
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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.Objects;

public class CoalGeneratorTE extends TileEntity implements INamedContainerProvider, ICapabilityProvider, ITickableTileEntity {

    private final Machine machine;

    public CoalGeneratorTE() {
        super(MachineRegistry.COAL_GENERATOR_TE.get());
        machine = MachineTypeRegistry.COAL_GENERATOR.getNewInstance();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == CapabilityEnergy.ENERGY) {
            return machine.getEnergyStorage(Direction.NORTH, side).cast();
        }
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return machine.getItemHandler(Direction.NORTH, side).cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        super.write(nbt);
        return machine.write(nbt);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        machine.read(nbt);
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        // left empty to avoid client autosync
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("block.space.coal_generator");
    }

    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
        PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
        this.writeGuiPacket(buf);
        return new CoalGeneratorContainer(windowId, playerInventory, buf, this.machine);
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
        this.machine.update();
    }

}
