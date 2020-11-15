package deerangle.space.block.entity;

import deerangle.space.container.CoalGeneratorContainer;
import deerangle.space.registry.MachineRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class CoalGeneratorTE extends TileEntity implements INamedContainerProvider {

    public CoalGeneratorTE() {
        super(MachineRegistry.COAL_GENERATOR_TE.get());
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("block.space.coal_generator");
    }

    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
        return new CoalGeneratorContainer(windowId, playerInventory, null);
    }
}
