package deerangle.space.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;

import java.util.function.Consumer;

public class RecipeGenerator extends RecipeProvider {

    public RecipeGenerator(DataGenerator generatorIn) {
        super(generatorIn);
    }

    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        // shapelessPlanksNew(consumer, Blocks.ACACIA_PLANKS, ItemTags.ACACIA_LOGS);
        //TODO: craft machines to machines without data
    }

}
