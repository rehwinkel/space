package deerangle.space.block.entity;

import deerangle.space.container.CoalGeneratorContainer;
import deerangle.space.machine.CoalGeneratorMachine;
import deerangle.space.machine.Machine;
import deerangle.space.registry.MachineRegistry;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;

public class CoalGeneratorTE extends TileEntity implements INamedContainerProvider, ICapabilityProvider {

    private final Machine machine;

    public CoalGeneratorTE() {
        super(MachineRegistry.COAL_GENERATOR_TE.get());
        machine = new CoalGeneratorMachine();
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
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        return machine.write(compound);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("block.space.coal_generator");
    }

    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
        PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
        this.writeGuiPacket(buf);
        return new CoalGeneratorContainer(windowId, playerInventory, buf);
    }

    public void writeGuiPacket(PacketBuffer buf) {
        machine.writeGuiPacket(buf);
    }

}
