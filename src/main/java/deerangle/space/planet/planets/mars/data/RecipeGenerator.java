package deerangle.space.planet.planets.mars.data;

import deerangle.space.data.AbstractRecipeGenerator;
import deerangle.space.planet.planets.mars.MarsRegistry;
import deerangle.space.registry.ResourceRegistry;
import net.minecraft.data.CookingRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.Ingredient;

import java.util.function.Consumer;

public class RecipeGenerator extends AbstractRecipeGenerator {

    public RecipeGenerator(DataGenerator generatorIn) {
        super(generatorIn);
    }

    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(MarsRegistry.RUSTY_DUST.get().asItem()), ResourceRegistry.IRON_DUST.get(), 0.3F, 100).addCriterion("has_rusty_dust", hasItem(MarsRegistry.RUSTY_DUST.get().asItem())).build(consumer);
    }

}
