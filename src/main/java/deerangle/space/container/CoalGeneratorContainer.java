package deerangle.space.container;

import deerangle.space.block.entity.CoalGeneratorTE;
import deerangle.space.machine.IMachineData;
import deerangle.space.machine.ItemMachineData;
import deerangle.space.machine.Machine;
import deerangle.space.machine.type.DataElement;
import deerangle.space.machine.type.Element;
import deerangle.space.machine.type.ItemSlotElement;
import deerangle.space.machine.type.MachineType;
import deerangle.space.main.PacketHandler;
import deerangle.space.network.UpdateMachineMsg;
import deerangle.space.registry.MachineRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

import java.util.Objects;

public class CoalGeneratorContainer extends Container {

    public final MachineType<?> machineType;
    private final Machine machine;
    private final BlockPos pos;

    public CoalGeneratorContainer(int windowId, PlayerInventory inv, PacketBuffer data) {
        this(windowId, inv, data, null);
    }

    public CoalGeneratorContainer(int windowId, PlayerInventory inv, PacketBuffer data, Machine machine) {
        super(MachineRegistry.COAL_GENERATOR_CONTAINER.get(), windowId);
        this.pos = data.readBlockPos();
        if (machine == null) {
            CoalGeneratorTE tileEntity = (CoalGeneratorTE) Minecraft.getInstance().world.getTileEntity(this.pos);
            this.machine = tileEntity.getMachine();
        } else {
            this.machine = machine;
        }
        IForgeRegistry<MachineType<?>> registry = RegistryManager.ACTIVE.getRegistry(MachineType.class);
        this.machineType = Objects.requireNonNull(registry.getValue(data.readResourceLocation()));

        for (Element el : this.machineType.getElements()) {
            if (el instanceof ItemSlotElement) {
                IItemHandler itemHandler;
                if (machine != null) {
                    IMachineData slot = machine.getMachineData(((DataElement) el).getIndex());
                    assert slot instanceof ItemMachineData;
                    itemHandler = ((ItemMachineData) slot).getItemHandler()
                            .orElseThrow(() -> new RuntimeException("couldn't get item handler"));
                } else {
                    itemHandler = new ItemStackHandler();
                }
                this.addSlot(new SlotItemHandler(itemHandler, 0, el.getX() + 1, el.getY() + 1));
            }
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inv, k, 8 + k * 18, 142));
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (IContainerListener listener : this.listeners) {
            if (listener instanceof ServerPlayerEntity) {
                PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) listener),
                        new UpdateMachineMsg(this.pos, this.machine));
            }
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true; // TODO
    }

    public Machine getMachine() {
        return this.machine;
    }
}
