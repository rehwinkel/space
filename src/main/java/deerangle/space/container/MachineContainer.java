package deerangle.space.container;

import deerangle.space.block.entity.MachineTileEntity;
import deerangle.space.machine.Machine;
import deerangle.space.machine.data.IMachineData;
import deerangle.space.machine.data.ItemMachineData;
import deerangle.space.machine.element.DataElement;
import deerangle.space.machine.element.Element;
import deerangle.space.machine.element.ItemElement;
import deerangle.space.machine.element.MachineType;
import deerangle.space.network.PacketHandler;
import deerangle.space.network.UpdateMachineMsg;
import deerangle.space.registry.MachineRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

import java.util.Objects;

public class MachineContainer extends Container {

    public final MachineType<?> machineType;
    public final BlockPos pos;
    private final Machine machine;
    private final int itemSlotCount;

    public MachineContainer(int windowId, PlayerInventory inv, PacketBuffer data) {
        this(windowId, inv, data, null);
    }

    public MachineContainer(int windowId, PlayerInventory inv, PacketBuffer data, Machine machine) {
        super(MachineRegistry.MACHINE_CONTAINER.get(), windowId);
        this.pos = data.readBlockPos();
        if (machine == null) {
            MachineTileEntity tileEntity = (MachineTileEntity) Minecraft.getInstance().world.getTileEntity(this.pos);
            this.machine = tileEntity.getMachine();
        } else {
            this.machine = machine;
        }
        IForgeRegistry<MachineType<?>> registry = RegistryManager.ACTIVE.getRegistry(MachineType.class);
        ResourceLocation loc = data.readResourceLocation();
        this.machineType = Objects.requireNonNull(registry.getValue(loc));
        this.itemSlotCount = this.getItemSlotCount();

        for (Element el : this.machineType.getElements()) {
            if (el instanceof ItemElement) {
                IItemHandler itemHandler;
                if (machine != null) {
                    IMachineData slot = machine.getMachineData(((DataElement) el).getIndex());
                    assert slot instanceof ItemMachineData;
                    itemHandler = ((ItemMachineData) slot).getItemHandler();
                } else {
                    itemHandler = new ItemStackHandler();
                }
                this.addSlot(new MachineSlot(itemHandler, 0, el.getX() + 1, el.getY() + 1, ((ItemElement) el)::isValid));
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
                if (this.machine.shouldSync()) {
                    PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) listener), new UpdateMachineMsg(this.pos, this.machine));
                }
            }
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return playerIn.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }

    public Machine getMachine() {
        return this.machine;
    }

    private int getItemSlotCount() {
        int counter = 0;
        for (int i = 0; i < this.machine.getMachineDataSize(); i++) {
            if (this.machine.getMachineData(i) instanceof ItemMachineData) {
                counter++;
            }
        }
        return counter;
    }

    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index < this.itemSlotCount) {
                if (!this.mergeItemStack(itemstack1, this.itemSlotCount, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, this.itemSlotCount, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

}
