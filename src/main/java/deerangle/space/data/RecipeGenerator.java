package deerangle.space.data;

import com.google.gson.JsonObject;
import deerangle.space.main.SpaceMod;
import deerangle.space.registry.MachineRegistry;
import deerangle.space.registry.RecipeRegistry;
import deerangle.space.registry.ResourceRegistry;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class RecipeGenerator extends RecipeProvider {

    public RecipeGenerator(DataGenerator generatorIn) {
        super(generatorIn);
    }

    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        consumer.accept(new BlastFurnaceResult(new ResourceLocation(SpaceMod.MOD_ID, "steel_ingot"),
                Ingredient.fromTag(Tags.Items.INGOTS_IRON), ResourceRegistry.STEEL_INGOT.get(), 200 * 16));
        addMachineRemoveNBTRecipe(MachineRegistry.COAL_GENERATOR.get(), consumer);
        addMachineRemoveNBTRecipe(MachineRegistry.BLAST_FURNACE.get(), consumer);
        addMachineRemoveNBTRecipe(MachineRegistry.COMBUSTION_GENERATOR.get(), consumer);
        addMachineRemoveNBTRecipe(MachineRegistry.GAS_TANK.get(), consumer);
        addMachineRemoveNBTRecipe(MachineRegistry.DRUM.get(), consumer);
    }

    private void addMachineRemoveNBTRecipe(Block block, Consumer<IFinishedRecipe> consumer) {
        String name = block.getRegistryName().getPath();
        ShapelessRecipeBuilder.shapelessRecipe(block).addIngredient(block).addCriterion("has_" + name, hasItem(block))
                .build(consumer, block.getRegistryName().getNamespace() + ":" + name + "_remove_nbt");
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

}
