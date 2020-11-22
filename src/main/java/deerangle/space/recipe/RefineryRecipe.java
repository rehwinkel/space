package deerangle.space.recipe;

import deerangle.space.registry.RecipeRegistry;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class RefineryRecipe implements IRecipe<IInventory> {

    private final FluidStack input;
    private final FluidStack result;
    private final int duration;
    private final ResourceLocation id;

    public RefineryRecipe(ResourceLocation id, FluidStack input, FluidStack result, int duration) {
        this.id = id;
        this.input = input;
        this.result = result;
        this.duration = duration;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return false;
    }

    //TODO: fix npe

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return null;
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeRegistry.REFINERY.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return RecipeRegistry.REFINERY_TYPE;
    }

    public FluidStack getInputFluid() {
        return this.input;
    }

    public FluidStack getResultFluid() {
        return this.result;
    }

    public int getDuration() {
        return this.duration;
    }

    public boolean matchesFluid(Fluid input) {
        return this.input.getFluid() == input;
    }

}
