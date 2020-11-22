package deerangle.space.machine;

import deerangle.space.block.MachineBlock;
import deerangle.space.machine.data.BurnMachineData;
import deerangle.space.machine.data.EnergyMachineData;
import deerangle.space.machine.data.ItemMachineData;
import deerangle.space.machine.util.FlowType;
import deerangle.space.registry.MachineTypeRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class CoalGeneratorMachine extends Machine {

    private static final int RF_PER_TICK = 20;
    private final ItemMachineData fuel;
    private final EnergyMachineData energy;
    private final BurnMachineData burn;
    private int currentMaxBurnTime = 0;
    private int currentBurnTime = 0;

    public CoalGeneratorMachine() {
        super(MachineTypeRegistry.COAL_GENERATOR, true, false, false, false, false, false);
        fuel = addMachineData(
                new ItemMachineData("Fuel", MachineTypeRegistry::isFuel, FlowType.INPUT, this, FUEL_TEXT));
        energy = addMachineData(new EnergyMachineData("Eng", 30000, 1000, FlowType.OUTPUT, this, ENERGY_TEXT));
        burn = addMachineData(new BurnMachineData("Burn"));
        this.sideConfig.setTop(fuel.getInputAccessor());
        this.sideConfig.setBack(energy.getOutputAccessor());
    }

    @Override
    public void update(World world, BlockPos pos) {
        boolean wasBurning = this.isBurning();
        if (currentBurnTime == 0) {
            ItemStack currentFuelStack = this.fuel.getItemHandler().getStackInSlot(0);
            int burnTime = ForgeHooks.getBurnTime(currentFuelStack);
            if (burnTime > 0 && this.energy.getEnergyStorage().receiveEnergy(RF_PER_TICK, true) != 0) {
                this.fuel.getItemHandler().extractItem(0, 1, false);
                currentMaxBurnTime = burnTime;
                currentBurnTime = currentMaxBurnTime;
            }
        } else {
            currentBurnTime--;
            this.energy.getEnergyStorage().receiveEnergy(RF_PER_TICK, false);
        }
        if (currentMaxBurnTime > 0) {
            this.burn.setProgress(currentBurnTime / (float) currentMaxBurnTime);
        }
        if (wasBurning != this.isBurning()) {
            world.setBlockState(pos, world.getBlockState(pos).with(MachineBlock.RUNNING, this.isBurning()), 3);
        }
    }

    private boolean isBurning() {
        return this.currentBurnTime > 0;
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        super.write(nbt);
        nbt.putInt("CurrentBurn", currentBurnTime);
        nbt.putInt("CurrentMax", currentMaxBurnTime);
        return nbt;
    }

    @Override
    public void read(CompoundNBT nbt) {
        super.read(nbt);
        currentMaxBurnTime = nbt.getInt("CurrentMax");
        currentBurnTime = nbt.getInt("CurrentBurn");
    }

}
