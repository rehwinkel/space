package deerangle.space.machine;

import deerangle.space.machine.data.EnergyMachineData;
import deerangle.space.machine.data.FluidMachineData;
import deerangle.space.machine.data.ItemMachineData;
import deerangle.space.machine.data.ProgressMachineData;
import deerangle.space.machine.util.FlowType;
import deerangle.space.recipe.RefineryRecipe;
import deerangle.space.registry.MachineTypeRegistry;
import deerangle.space.registry.RecipeRegistry;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.List;
import java.util.Optional;

public class RefineryMachine extends Machine {

    private static final int SIP_SIZE = 20;
    private static final int RF_PER_TICK = 10;

    private final EnergyMachineData energy;
    private final FluidMachineData input;
    private final FluidMachineData output;
    private final ItemMachineData bucketInput;
    private final ItemMachineData bucketOutput;
    private final ProgressMachineData progress;

    private int currentMaxProgress = 0;
    private int currentProgress = 0;
    private RefineryRecipe currentRecipe = null;
    private int recipeCount = 0;

    public RefineryMachine() {
        super(MachineTypeRegistry.REFINERY);
        energy = addMachineData(new EnergyMachineData("Energy", 15000, 1000, FlowType.INPUT, this, ENERGY_TEXT));
        input = addMachineData(new FluidMachineData("Input", 4000, fluidStack -> true, FlowType.INPUT, this, INPUT_TEXT));
        output = addMachineData(new FluidMachineData("Output", 4000, fluidStack -> true, FlowType.OUTPUT, this, OUTPUT_TEXT));
        bucketInput = addMachineData(new ItemMachineData("InBuck", MachineTypeRegistry::holdsFluid, FlowType.NONE, this, BUCKET_TEXT));
        bucketOutput = addMachineData(new ItemMachineData("OutBuck", MachineTypeRegistry::holdsFluid, FlowType.NONE, this, BUCKET_TEXT));
        progress = addMachineData(new ProgressMachineData("Progress"));
        this.sideConfig.setFront(energy.getInputAccessor());
        this.sideConfig.setBack(energy.getInputAccessor());
        this.sideConfig.setLeft(input.getInputAccessor());
        this.sideConfig.setRight(output.getOutputAccessor());
    }

    @Override
    public void update(World world, BlockPos pos) {
        ItemStack inputItem = this.bucketInput.getItemHandler().getStackInSlot(0);
        inputItem.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(cap -> {
            FluidStack simDrain = cap.drain(20, IFluidHandler.FluidAction.SIMULATE);
            if (simDrain.isEmpty()) {
                simDrain = cap.drain(1000, IFluidHandler.FluidAction.SIMULATE);
                if (!simDrain.isEmpty()) {
                    int doneFill = this.input.getFluidHandler().fill(simDrain, IFluidHandler.FluidAction.SIMULATE);
                    if (doneFill == 1000) {
                        cap.drain(doneFill, IFluidHandler.FluidAction.EXECUTE);
                        this.input.getFluidHandler().fill(simDrain, IFluidHandler.FluidAction.EXECUTE);
                    }
                }
            } else {
                int doneFill = this.input.getFluidHandler().fill(simDrain, IFluidHandler.FluidAction.SIMULATE);
                cap.drain(doneFill, IFluidHandler.FluidAction.EXECUTE);
                this.input.getFluidHandler().fill(simDrain, IFluidHandler.FluidAction.EXECUTE);
            }
            this.bucketInput.getItemHandler().setStackInSlot(0, cap.getContainer());
        });

        ItemStack outputItem = this.bucketOutput.getItemHandler().getStackInSlot(0);
        outputItem.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(cap -> {
            FluidStack simDrain = this.output.getFluidHandler().drain(20, IFluidHandler.FluidAction.SIMULATE);
            int filled = cap.fill(simDrain, IFluidHandler.FluidAction.SIMULATE);
            if (filled == 0) {
                simDrain = this.output.getFluidHandler().drain(1000, IFluidHandler.FluidAction.SIMULATE);
                filled = cap.fill(simDrain, IFluidHandler.FluidAction.SIMULATE);
                if (filled == 1000) {
                    FluidStack drain = this.output.getFluidHandler().drain(1000, IFluidHandler.FluidAction.EXECUTE);
                    cap.fill(drain, IFluidHandler.FluidAction.EXECUTE);
                }
            } else {
                FluidStack drain = this.output.getFluidHandler().drain(20, IFluidHandler.FluidAction.EXECUTE);
                cap.fill(drain, IFluidHandler.FluidAction.EXECUTE);
            }
            this.bucketOutput.getItemHandler().setStackInSlot(0, cap.getContainer());
        });

        if (currentRecipe == null) {
            FluidStack drainedSim = this.input.getFluidHandler().drain(SIP_SIZE, IFluidHandler.FluidAction.SIMULATE);
            Optional<RefineryRecipe> recipeOpt = getRecipe(world, drainedSim.getFluid());
            if (recipeOpt.isPresent()) {
                RefineryRecipe recipe = recipeOpt.get();
                float inOutRatio = recipe.getInputFluid().getAmount() / (float) recipe.getResultFluid().getAmount();
                assert inOutRatio % 1 == 0;
                int recipes = (int) (drainedSim.getAmount() / inOutRatio);
                int toProcess = (int) (recipes * inOutRatio);
                int energyRequired = toProcess * recipe.getDuration() * RF_PER_TICK;
                if (this.energy.getEnergy() >= energyRequired) {
                    int resultAmount = recipes * recipe.getResultFluid().getAmount();
                    int simFill = this.output.getFluidHandler().fill(new FluidStack(recipe.getResultFluid().getFluid(), resultAmount), IFluidHandler.FluidAction.SIMULATE);
                    if (simFill == resultAmount) {
                        this.recipeCount = recipes;
                        this.currentMaxProgress = toProcess * recipe.getDuration();
                        this.currentProgress = this.currentMaxProgress;
                        this.currentRecipe = recipe;
                        this.input.getFluidHandler().drain(toProcess, IFluidHandler.FluidAction.EXECUTE);
                    }
                }
            }
        } else {
            if (this.currentProgress == 0) {
                this.output.getFluidHandler().fill(new FluidStack(this.currentRecipe.getResultFluid().getFluid(), this.recipeCount * this.currentRecipe.getResultFluid().getAmount()), IFluidHandler.FluidAction.EXECUTE);
                this.currentRecipe = null;
                this.recipeCount = 0;
                this.currentMaxProgress = 0;
                this.currentProgress = 0;
            }
            this.currentProgress--;
            this.energy.getEnergyStorage().extractEnergy(RF_PER_TICK, false);
        }

        if (this.currentMaxProgress > 0) {
            this.progress.setProgress(1 - (this.currentProgress / (float) this.currentMaxProgress));
        } else {
            this.progress.setProgress(0);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        super.write(nbt);
        nbt.putInt("ProgCurr", currentProgress);
        nbt.putInt("ProgMax", currentMaxProgress);
        return nbt;
    }

    @Override
    public void read(CompoundNBT nbt) {
        super.read(nbt);
        currentMaxProgress = nbt.getInt("ProgMax");
        currentProgress = nbt.getInt("ProgCurr");
    }

    private Optional<RefineryRecipe> getRecipe(World world, Fluid input) {
        List<RefineryRecipe> refineryRecipeList = world.getRecipeManager().getRecipesForType(RecipeRegistry.REFINERY_TYPE);
        return refineryRecipeList.stream().filter(recipe -> recipe.matchesFluid(input)).findFirst();
    }

}
