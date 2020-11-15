package deerangle.space.container;

import deerangle.space.registry.MachineRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;

public class CoalGeneratorContainer extends Container {

    public CoalGeneratorContainer(int windowId, PlayerInventory inv, PacketBuffer data) {
        super(MachineRegistry.COAL_GENERATOR_CONTAINER.get(), windowId);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true; // TODO
    }

}
