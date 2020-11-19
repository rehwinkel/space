package deerangle.space.machine;

import deerangle.space.block.MachineBlock;
import deerangle.space.machine.data.BurnMachineData;
import deerangle.space.machine.data.ItemMachineData;
import deerangle.space.machine.data.ProgressMachineData;
import deerangle.space.machine.util.SideConfig;
import deerangle.space.recipe.BlastFurnaceRecipe;
import deerangle.space.registry.MachineTypeRegistry;
import deerangle.space.registry.RecipeRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import java.util.Optional;

public class BlastFurnaceMachine extends Machine {

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
        super(MachineTypeRegistry.BLAST_FURNACE,
                new SideConfig(-1, -1, 1, 2, -1, 0, true, false, false, false, true, false, 3));
        fuel = addMachineData(new ItemMachineData("Fuel"));
        input = addMachineData(new ItemMachineData("Input"));
        output = addMachineData(new ItemMachineData("Output"));
        burn = addMachineData(new BurnMachineData("Burn"));
        progress = addMachineData(new ProgressMachineData("Prog"));
    }

    @Override
    public void update(World world, BlockPos pos) {
        boolean wasBurning = this.isBurning();
        Optional<BlastFurnaceRecipe> recipeOpt = world.getRecipeManager().getRecipe(RecipeRegistry.BLAST_FURNACE_TYPE,
                new StackInventory(this.input.getItemHandlerOrThrow().getStackInSlot(0)), world);
        if (currentBurnTime <= 1) {
            if (currentBurnTime == 0) {
                this.currentMaxProgress = 0;
                this.currentProgress = 0;
                this.currentRecipe = null;
                this.progress.setProgress(0);
            }
            ItemStack currentFuelStack = this.fuel.getItemHandlerOrThrow().getStackInSlot(0);
            int burnTime = ForgeHooks.getBurnTime(currentFuelStack);
            if (burnTime > 0 && recipeOpt.isPresent()) {
                this.fuel.getItemHandlerOrThrow().extractItem(0, 1, false);
                currentMaxBurnTime = burnTime;
                currentBurnTime += currentMaxBurnTime + 1;
            }
        }

        if (currentBurnTime > 0) {
            if (currentRecipe == null) {
                recipeOpt.ifPresent(recipe -> {
                    this.currentRecipe = recipe;
                    this.currentMaxProgress = recipe.getDuration();
                    this.currentProgress = recipe.getDuration();
                });
            } else {
                this.currentProgress--;

                if (this.currentProgress == 0) {
                    if (ItemStack.EMPTY == this.output.getItemHandlerOrThrow()
                            .insertItem(0, this.currentRecipe.getCraftingResult(null), true)) {
                        this.output.getItemHandlerOrThrow()
                                .insertItem(0, this.currentRecipe.getCraftingResult(null), false);
                        this.input.getItemHandlerOrThrow().extractItem(0, 1, false);
                    }
                    this.currentRecipe = null;
                    this.currentMaxProgress = 0;
                }
            }
        }

        if (currentBurnTime > 0) {
            currentBurnTime--;
        }

        if (currentMaxProgress > 0) {
            this.progress.setProgress(1 - (currentProgress / (float) currentMaxProgress));
        } else {
            this.progress.setProgress(0);
        }

        if (currentMaxBurnTime > 0) {
            this.burn.setProgress(currentBurnTime / (float) currentMaxBurnTime);
        } else {
            this.burn.setProgress(0);
        }

        if (wasBurning != this.isBurning()) {
            world.setBlockState(pos, world.getBlockState(pos).with(MachineBlock.RUNNING, this.isBurning()), 3);
        }
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

    private boolean isBurning() {
        return currentBurnTime > 0;
    }

}
