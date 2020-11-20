package deerangle.space.data;

import com.google.gson.JsonObject;
import deerangle.space.main.SpaceMod;
import deerangle.space.registry.RecipeRegistry;
import deerangle.space.registry.ResourceRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
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
                Ingredient.fromTag(Tags.Items.INGOTS_IRON), ResourceRegistry.STEEL_INGOT.get(), 1.5F, 200 * 16));
        //TODO: craft machines to machines without data
    }

    private class BlastFurnaceResult implements IFinishedRecipe {
        //TODO remove experience field
        private final ResourceLocation recipeId;
        private final Ingredient ingredient;
        private final Item result;
        private final float experience;
        private final int duration;

        public BlastFurnaceResult(ResourceLocation recipeId, Ingredient ingredient, Item result, float experience, int duration) {
            this.recipeId = recipeId;
            this.ingredient = ingredient;
            this.result = result;
            this.experience = experience;
            this.duration = duration;
        }

        @Override
        public void serialize(JsonObject json) {
            json.add("ingredient", this.ingredient.serialize());
            json.addProperty("result", Registry.ITEM.getKey(this.result).toString());
            json.addProperty("experience", this.experience);
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
