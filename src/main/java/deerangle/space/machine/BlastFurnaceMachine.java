package deerangle.space.machine;

import deerangle.space.block.MachineBlock;
import deerangle.space.machine.data.BurnMachineData;
import deerangle.space.machine.data.ItemMachineData;
import deerangle.space.machine.data.ProgressMachineData;
import deerangle.space.machine.util.FlowType;
import deerangle.space.machine.util.StackInventory;
import deerangle.space.recipe.BlastFurnaceRecipe;
import deerangle.space.registry.MachineTypeRegistry;
import deerangle.space.registry.RecipeRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.IItemHandler;

import java.util.Optional;

public class BlastFurnaceMachine extends Machine {

    private static final int BURN_RATE = 2;

    private final ItemMachineData fuel;
    private final ItemMachineData input;
    private final ItemMachineData output;
    private final BurnMachineData burn;
    private final ProgressMachineData progress;

    private int currentMaxBurnTime = 0;
    private int currentBurnTime = 0;
    private int currentMaxProgress = 0;
    private int currentProgress = 0;
    private BlastFurnaceRecipe currentRecipe = null;

    public BlastFurnaceMachine() {
        super(MachineTypeRegistry.BLAST_FURNACE, true, false, false, false, true, false);
        fuel = addMachineData(new ItemMachineData("Fuel", stack -> stack.getItem().equals(Items.COAL), FlowType.INPUT, this, FUEL_TEXT));
        input = addMachineData(new ItemMachineData("Input", FlowType.INPUT, this, INPUT_TEXT));
        output = addMachineData(new ItemMachineData("Output", FlowType.OUTPUT, this, OUTPUT_TEXT));
        burn = addMachineData(new BurnMachineData("Burn"));
        progress = addMachineData(new ProgressMachineData("Prog"));
        this.sideConfig.setLeft(input.getInputAccessor());
        this.sideConfig.setRight(output.getOutputAccessor());
        this.sideConfig.setBack(fuel.getInputAccessor());
    }

    @Override
    public void update(World world, BlockPos pos) {
        boolean wasBurning = this.isBurning();

        Optional<BlastFurnaceRecipe> recipeOpt = world.getRecipeManager().getRecipe(RecipeRegistry.BLAST_FURNACE_TYPE, new StackInventory(this.input.getItemHandler().getStackInSlot(0)), world);
        boolean shouldUseFuel = recipeOpt.isPresent() && this.output.getItemHandler().insertItem(0, recipeOpt.get().getCraftingResult(null), true) == ItemStack.EMPTY;
        if (this.currentRecipe != null && this.currentProgress == 1) {
            shouldUseFuel = false;
        }

        this.doBurn(shouldUseFuel);

        if (this.currentRecipe != null) {
            this.currentProgress--;
        }

        boolean isFuelled = currentBurnTime > 0;
        if (isFuelled && recipeOpt.isPresent()) {
            if (this.currentRecipe == null) {
                this.currentRecipe = recipeOpt.get();
                this.currentMaxProgress = this.currentRecipe.getDuration();
                this.currentProgress = this.currentMaxProgress;
            }
        } else {
            if (this.currentProgress != 0 && this.currentRecipe != null) {
                this.currentRecipe = null;
                this.currentMaxProgress = 0;
                this.currentProgress = 0;
            }
        }

        if (this.currentProgress == 0 && this.currentRecipe != null) {
            IItemHandler out = this.output.getItemHandler();
            out.insertItem(0, this.currentRecipe.getCraftingResult(null), false);
            this.input.getItemHandler().extractItem(0, 1, false);
            this.currentRecipe = null;
            this.currentMaxProgress = 0;
            this.currentProgress = 0;
        }

        if (this.currentMaxBurnTime > 0) {
            this.burn.setProgress(this.currentBurnTime / (float) this.currentMaxBurnTime);
        } else {
            this.burn.setProgress(0);
        }

        if (this.currentMaxProgress > 0) {
            this.progress.setProgress(1 - (this.currentProgress / (float) this.currentMaxProgress));
        } else {
            this.progress.setProgress(0);
        }

        if (wasBurning != this.isBurning()) {
            world.setBlockState(pos, world.getBlockState(pos).with(MachineBlock.RUNNING, this.isBurning()), 3);
        }
    }

    protected void doBurn(boolean shouldUseFuel) {
        if (currentBurnTime > 0) {
            currentBurnTime -= BURN_RATE;
        }
        if (currentBurnTime == 0) {
            ItemStack currentFuelStack = this.fuel.getItemHandler().getStackInSlot(0);
            int burnTime = ForgeHooks.getBurnTime(currentFuelStack);
            if (burnTime > 0 && shouldUseFuel) {
                this.fuel.getItemHandler().extractItem(0, 1, false);
                currentMaxBurnTime = burnTime;
                currentBurnTime = currentMaxBurnTime;
            }
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        super.write(nbt);
        nbt.putInt("BurnCurr", currentBurnTime);
        nbt.putInt("BurnMax", currentMaxBurnTime);
        nbt.putInt("ProgCurr", currentProgress);
        nbt.putInt("ProgMax", currentMaxProgress);
        return nbt;
    }

    @Override
    public void read(CompoundNBT nbt) {
        super.read(nbt);
        currentMaxBurnTime = nbt.getInt("BurnMax");
        currentBurnTime = nbt.getInt("BurnCurr");
        currentMaxProgress = nbt.getInt("ProgMax");
        currentProgress = nbt.getInt("ProgCurr");
    }

    private boolean isBurning() {
        return currentBurnTime > 0;
    }

}
