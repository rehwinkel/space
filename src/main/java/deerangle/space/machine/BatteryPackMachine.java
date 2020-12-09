package deerangle.space.machine;

import deerangle.space.machine.data.EnergyMachineData;
import deerangle.space.machine.data.ItemMachineData;
import deerangle.space.machine.util.FlowType;
import deerangle.space.registry.MachineTypeRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;

public class BatteryPackMachine extends Machine {

    private static final int DRAIN_AMOUNT = 1000;

    private final EnergyMachineData energy;
    private final ItemMachineData input;
    private final ItemMachineData output;

    public BatteryPackMachine() {
        super(MachineTypeRegistry.BATTERY_PACK);
        energy = addMachineData(new EnergyMachineData("Energy", 500000, DRAIN_AMOUNT, FlowType.INOUT, this, ENERGY_TEXT));
        input = addMachineData(new ItemMachineData("Input", stack -> stack.getCapability(CapabilityEnergy.ENERGY).isPresent(), FlowType.NONE, this, BATTERY_TEXT));
        output = addMachineData(new ItemMachineData("Output", stack -> stack.getCapability(CapabilityEnergy.ENERGY).isPresent(), FlowType.NONE, this, BATTERY_TEXT));
        this.sideConfig.setAll(energy.getOutputAccessor());
    }

    @Override
    public void update(World world, BlockPos pos) {
        ItemStack inputItem = this.input.getItemHandler().getStackInSlot(0);
        inputItem.getCapability(CapabilityEnergy.ENERGY).ifPresent(cap -> {
            int actuallyExtracted = cap.extractEnergy(DRAIN_AMOUNT, true);
            int accepted = this.energy.getEnergyStorage().receiveEnergy(actuallyExtracted, true);
            this.energy.getEnergyStorage().receiveEnergy(cap.extractEnergy(accepted, false), false);
        });

        ItemStack outputItem = this.output.getItemHandler().getStackInSlot(0);
        outputItem.getCapability(CapabilityEnergy.ENERGY).ifPresent(cap -> {
            int actuallyExtracted = this.energy.getEnergyStorage().extractEnergy(DRAIN_AMOUNT, true);
            int accepted = cap.receiveEnergy(actuallyExtracted, true);
            cap.receiveEnergy(this.energy.getEnergyStorage().extractEnergy(accepted, false), false);
        });
    }

}
