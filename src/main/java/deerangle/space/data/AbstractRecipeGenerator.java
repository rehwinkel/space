package deerangle.space.data;

import com.google.gson.JsonObject;
import deerangle.space.recipe.RefineryRecipeSerializer;
import deerangle.space.registry.RecipeRegistry;
import net.minecraft.block.Block;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Consumer;

public class AbstractRecipeGenerator extends RecipeProvider {

    public AbstractRecipeGenerator(DataGenerator generatorIn) {
        super(generatorIn);
    }

    protected void addStoneCutterRecipe(Consumer<IFinishedRecipe> consumer, IItemProvider ingredient, IItemProvider result, int count, String recipeName) {
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(ingredient), result, count)
                .addCriterion("has_ingredient", hasItem(ingredient))
                .build(consumer, recipeName + "_from_stone_cutting");
    }

    protected void addSpaceBlastFurnaceRecipe(Consumer<IFinishedRecipe> consumer, Ingredient input, Item result, int duration) {
        addSpaceBlastFurnaceRecipe(consumer, input, result, duration, "");
    }

    protected void addSpaceBlastFurnaceRecipe(Consumer<IFinishedRecipe> consumer, Ingredient input, Item result, int duration, String recipeName) {
        ResourceLocation loc = result.getRegistryName();
        consumer.accept(new BlastFurnaceResult(
                new ResourceLocation(loc.getNamespace(), loc.getPath() + recipeName + "_from_space_blast_furnace"),
                input, result, duration));
    }

    protected void addRefineryRecipe(Consumer<IFinishedRecipe> consumer, FluidStack input, FluidStack result, int duration) {
        ResourceLocation loc = result.getFluid().getRegistryName();
        consumer.accept(
                new RefineryResult(new ResourceLocation(loc.getNamespace(), loc.getPath() + "_from_refinery"), input,
                        result, duration));
    }

    protected void addMachineRemoveNBTRecipe(Block block, Consumer<IFinishedRecipe> consumer) {
        String name = block.getRegistryName().getPath();
        ShapelessRecipeBuilder.shapelessRecipe(block).addIngredient(block).addCriterion("has_" + name, hasItem(block))
                .build(consumer, block.getRegistryName().getNamespace() + ":" + name + "_remove_nbt");
    }

    protected void registerIngotRecipes(Item itemIngot, Block itemBlock, Item itemNugget, Consumer<IFinishedRecipe> consumer, String name) {
        ShapelessRecipeBuilder.shapelessRecipe(itemIngot, 9).addIngredient(itemBlock)
                .addCriterion("has_ingot", hasItem(itemIngot)).build(consumer, name + "_ingot_from_" + name + "_block");
        ShapelessRecipeBuilder.shapelessRecipe(itemBlock).addIngredient(itemIngot, 9)
                .addCriterion("has_ingot", hasItem(itemIngot)).build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(itemIngot).addIngredient(itemNugget, 9)
                .addCriterion("has_ingot", hasItem(itemIngot))
                .build(consumer, name + "_ingot_from_" + name + "_nugget");
        ShapelessRecipeBuilder.shapelessRecipe(itemNugget, 9).addIngredient(itemIngot)
                .addCriterion("has_ingot", hasItem(itemIngot)).build(consumer);
    }

    private class BlastFurnaceResult implements IFinishedRecipe {
        private final ResourceLocation recipeId;
        private final Ingredient ingredient;
        private final Item result;
        private final int duration;

        public BlastFurnaceResult(ResourceLocation recipeId, Ingredient ingredient, Item result, int duration) {
            this.recipeId = recipeId;
            this.ingredient = ingredient;
            this.result = result;
            this.duration = duration;
        }

        @Override
        public void serialize(JsonObject json) {
            json.add("ingredient", this.ingredient.serialize());
            json.addProperty("result", Registry.ITEM.getKey(this.result).toString());
            json.addProperty("duration", this.duration);
        }

        @Override
        public ResourceLocation getID() {
            return this.recipeId;
        }

        @Override
        public IRecipeSerializer<?> getSerializer() {
            return RecipeRegistry.BLAST_FURNACE.get();
        }

        @Override
        public JsonObject getAdvancementJson() {
            return null;
        }

        @Override
        public ResourceLocation getAdvancementID() {
            return null;
        }

    }

    private class RefineryResult implements IFinishedRecipe {
        private final ResourceLocation recipeId;
        private final FluidStack input;
        private final FluidStack result;
        private final int duration;

        public RefineryResult(ResourceLocation recipeId, FluidStack input, FluidStack result, int duration) {
            this.recipeId = recipeId;
            this.input = input;
            this.result = result;
            this.duration = duration;
        }

        @Override
        public void serialize(JsonObject json) {
            json.add("input", RefineryRecipeSerializer.writeFluidStack(this.input));
            json.add("result", RefineryRecipeSerializer.writeFluidStack(this.result));
            json.addProperty("duration", this.duration);
        }

        @Override
        public ResourceLocation getID() {
            return this.recipeId;
        }

        @Override
        public IRecipeSerializer<?> getSerializer() {
            return RecipeRegistry.REFINERY.get();
        }

        @Override
        public JsonObject getAdvancementJson() {
            return null;
        }

        @Override
        public ResourceLocation getAdvancementID() {
            return null;
        }

    }

}
