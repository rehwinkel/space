package deerangle.space.machine;

import com.google.common.collect.ImmutableMap;
import deerangle.space.block.MachineBlock;
import deerangle.space.machine.data.BurnMachineData;
import deerangle.space.machine.data.EnergyMachineData;
import deerangle.space.machine.data.FluidMachineData;
import deerangle.space.machine.data.ItemMachineData;
import deerangle.space.machine.util.FlowType;
import deerangle.space.registry.FluidRegistry;
import deerangle.space.registry.MachineTypeRegistry;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.Map;

public class CombustionGeneratorMachine extends Machine {

    private final FluidMachineData fuel;
    private final EnergyMachineData energy;
    private final ItemMachineData bucket;
    private final BurnMachineData burn;

    private static final int SIP_SIZE = 20;
    private static final int RF_PER_TICK = 60;

    public static final Map<Fluid, Integer> BURN_TIME_MAP = ImmutableMap
            .of(FluidRegistry.CRUDE_OIL.get(), 2, FluidRegistry.KEROSENE.get(), 5);
    private int currentBurnTime;
    private int currentMaxBurnTime;

    public static int getBurnTime(FluidStack stack) {
        if (!BURN_TIME_MAP.containsKey(stack.getFluid())) {
            return 0;
        }
        return BURN_TIME_MAP.get(stack.getFluid()) * stack.getAmount();
    }

    public CombustionGeneratorMachine() {
        super(MachineTypeRegistry.COMBUSTION_GENERATOR, false, false, true, true, true, false);
        fuel = addMachineData(
                new FluidMachineData("Fuel", 4000, stack -> getBurnTime(stack) > 0, FlowType.INPUT, this, FUEL_TEXT));
        energy = addMachineData(new EnergyMachineData("Eng", 60000, 1000, FlowType.OUTPUT, this, ENERGY_TEXT));
        bucket = addMachineData(
                new ItemMachineData("Bucket", MachineTypeRegistry::holdsFluid, FlowType.NONE, this, BUCKET_TEXT));
        burn = addMachineData(new BurnMachineData("Burn"));
        this.sideConfig.setFront(fuel.getInputAccessor());
        this.sideConfig.setBack(energy.getOutputAccessor());
    }

    @Override
    public void update(World world, BlockPos pos) {
        boolean wasBurning = this.isBurning();

        ItemStack bucketItem = bucket.getItemHandler().getStackInSlot(0);
        bucketItem.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(cap -> {
            FluidStack simDrain = cap.drain(20, IFluidHandler.FluidAction.SIMULATE);
            if (simDrain.isEmpty()) {
                simDrain = cap.drain(1000, IFluidHandler.FluidAction.SIMULATE);
                if (!simDrain.isEmpty()) {
                    int doneFill = this.fuel.getFluidHandler().fill(simDrain, IFluidHandler.FluidAction.SIMULATE);
                    if (doneFill == 1000) {
                        cap.drain(doneFill, IFluidHandler.FluidAction.EXECUTE);
                        this.fuel.getFluidHandler().fill(simDrain, IFluidHandler.FluidAction.EXECUTE);
                    }
                }
            } else {
                int doneFill = this.fuel.getFluidHandler().fill(simDrain, IFluidHandler.FluidAction.SIMULATE);
                cap.drain(doneFill, IFluidHandler.FluidAction.EXECUTE);
                this.fuel.getFluidHandler().fill(simDrain, IFluidHandler.FluidAction.EXECUTE);
            }
            bucket.getItemHandler().setStackInSlot(0, cap.getContainer());
        });

        FluidStack drainedSim = this.fuel.getFluidHandler().drain(SIP_SIZE, IFluidHandler.FluidAction.SIMULATE);
        if (!drainedSim.isEmpty() && currentBurnTime <= 0) {
            if (this.energy.getEnergyStorage().receiveEnergy(RF_PER_TICK, true) > 0) {
                currentMaxBurnTime = getBurnTime(drainedSim);
                currentBurnTime = currentMaxBurnTime;
                this.fuel.getFluidHandler().drain(SIP_SIZE, IFluidHandler.FluidAction.EXECUTE);
            } else {
                currentBurnTime = 0;
            }
        }
        if (currentBurnTime > 0) {
            this.energy.getEnergyStorage().receiveEnergy(RF_PER_TICK, false);
            currentBurnTime--;
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
