package deerangle.space.recipe;

import deerangle.space.registry.RecipeRegistry;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class BlastFurnaceRecipe implements IRecipe<IInventory> {

    private final ResourceLocation recipeId;
    public final Ingredient ingredient;
    public final ItemStack result;
    public final int duration;

    public BlastFurnaceRecipe(ResourceLocation recipeId, Ingredient ingredient, ItemStack result, int duration) {
        this.recipeId = recipeId;
        this.ingredient = ingredient;
        this.result = result;
        this.duration = duration;
    }

    public int getDuration() {
        return duration / 2;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return this.ingredient.test(inv.getStackInSlot(0));
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return this.result.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.result;
    }

    @Override
    public ResourceLocation getId() {
        return this.recipeId;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeRegistry.BLAST_FURNACE.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return RecipeRegistry.BLAST_FURNACE_TYPE;
    }

}
