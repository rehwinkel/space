package deerangle.space.machine;

import com.google.common.collect.ImmutableMap;
import deerangle.space.block.MachineBlock;
import deerangle.space.machine.data.BurnMachineData;
import deerangle.space.machine.data.EnergyMachineData;
import deerangle.space.machine.data.FluidMachineData;
import deerangle.space.machine.data.ItemMachineData;
import deerangle.space.machine.util.SideConfig;
import deerangle.space.registry.MachineTypeRegistry;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
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
    //TODO: oil, not lava
    public static final Map<Fluid, Integer> BURN_TIME_HASHMAP = ImmutableMap.of(Fluids.LAVA, 2);
    private int currentBurnTime;
    private int currentMaxBurnTime;

    public static int getBurnTime(FluidStack stack) {
        if (!BURN_TIME_HASHMAP.containsKey(stack.getFluid())) {
            return 0;
        }
        return BURN_TIME_HASHMAP.get(stack.getFluid()) * stack.getAmount();
    }

    public CombustionGeneratorMachine() {
        super(MachineTypeRegistry.COMBUSTION_GENERATOR,
                new SideConfig(0, 1, -1, -1, -1, 1, false, false, true, true, true, false, 2));
        fuel = addMachineData(new FluidMachineData("Fuel", 4000, stack -> getBurnTime(stack) > 0));
        energy = addMachineData(new EnergyMachineData("Eng", 60000, 1000));
        bucket = addMachineData(new ItemMachineData("Bucket",
                stack -> stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent()));
        burn = addMachineData(new BurnMachineData("Burn"));
    }

    @Override
    public void update(World world, BlockPos pos) {
        boolean wasBurning = this.isBurning();

        ItemStack bucketItem = bucket.getMachineItemHandler().getStackInSlot(0);
        bucketItem.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(cap -> {
            FluidStack simDrain = cap.drain(20, IFluidHandler.FluidAction.SIMULATE);
            if (simDrain.isEmpty()) {
                simDrain = cap.drain(1000, IFluidHandler.FluidAction.SIMULATE);
                if (!simDrain.isEmpty()) {
                    int doneFill = this.fuel.getTankOrThrow().fill(simDrain, IFluidHandler.FluidAction.SIMULATE);
                    if (doneFill == 1000) {
                        cap.drain(doneFill, IFluidHandler.FluidAction.EXECUTE);
                        this.fuel.getTankOrThrow().fill(simDrain, IFluidHandler.FluidAction.EXECUTE);
                    }
                }
            } else {
                int doneFill = this.fuel.getTankOrThrow().fill(simDrain, IFluidHandler.FluidAction.SIMULATE);
                cap.drain(doneFill, IFluidHandler.FluidAction.EXECUTE);
                this.fuel.getTankOrThrow().fill(simDrain, IFluidHandler.FluidAction.EXECUTE);
            }
            bucket.getMachineItemHandler().setStackInSlot(0, cap.getContainer());
        });

        FluidStack drainedSim = this.fuel.getTankOrThrow().drain(SIP_SIZE, IFluidHandler.FluidAction.SIMULATE);
        if (!drainedSim.isEmpty() && currentBurnTime <= 0) {
            if (this.energy.getStorageOrThrow().receiveEnergy(RF_PER_TICK, true) > 0) {
                currentMaxBurnTime = getBurnTime(drainedSim);
                currentBurnTime = currentMaxBurnTime;
                this.fuel.getTankOrThrow().drain(SIP_SIZE, IFluidHandler.FluidAction.EXECUTE);
            } else {
                currentBurnTime = 0;
            }
        }
        if (currentBurnTime > 0) {
            this.energy.getStorageOrThrow().receiveEnergy(RF_PER_TICK, false);
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