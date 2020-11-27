package deerangle.space.planet.planets.venus.data;

import deerangle.space.data.AbstractRecipeGenerator;
import deerangle.space.planet.planets.venus.VenusRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;

import java.util.function.Consumer;

public class RecipeGenerator extends AbstractRecipeGenerator {

    public RecipeGenerator(DataGenerator generatorIn) {
        super(generatorIn);
    }

    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        addStoneCutterRecipe(consumer, VenusRegistry.PULCHERITE.get(), VenusRegistry.POLISHED_PULCHERITE.get(), 1,
                "polished_pulcherite_from_pulcherite");
        addStoneCutterRecipe(consumer, VenusRegistry.PULCHERITE.get(), VenusRegistry.PULCHERITE_BRICKS.get(), 1,
                "pulcherite_bricks_from_pulcherite");
        addStoneCutterRecipe(consumer, VenusRegistry.POLISHED_PULCHERITE.get(), VenusRegistry.PULCHERITE_BRICKS.get(),
                1, "pulcherite_bricks_from_polished_pulcherite");

        ShapedRecipeBuilder.shapedRecipe(VenusRegistry.SHRIEKWOOD_WOOD.get(), 3)
                .key('l', VenusRegistry.SHRIEKWOOD_LOG.get()).patternLine("ll ").patternLine("ll ").patternLine("   ")
                .addCriterion("has_shriekwood_log", hasItem(VenusRegistry.SHRIEKWOOD_LOG.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(VenusRegistry.SHRIEKWOOD_PLANKS.get(), 4)
                .key('l', VenusRegistry.SHRIEKWOOD_LOG.get()).patternLine("l  ").patternLine("   ").patternLine("   ")
                .addCriterion("has_shriekwood_log", hasItem(VenusRegistry.SHRIEKWOOD_LOG.get()))
                .build(consumer, "shriekwood_planks_from_shriekwood_log");
        ShapedRecipeBuilder.shapedRecipe(VenusRegistry.SHRIEKWOOD_PLANKS.get(), 4)
                .key('w', VenusRegistry.SHRIEKWOOD_WOOD.get()).patternLine("w  ").patternLine("   ").patternLine("   ")
                .addCriterion("has_shriekwood_wood", hasItem(VenusRegistry.SHRIEKWOOD_WOOD.get()))
                .build(consumer, "shriekwood_planks_from_shriekwood_wood");
        ShapedRecipeBuilder.shapedRecipe(VenusRegistry.SHRIEKWOOD_STAIRS.get(), 4)
                .key('p', VenusRegistry.SHRIEKWOOD_PLANKS.get()).patternLine("p  ").patternLine("pp ")
                .patternLine("ppp")
                .addCriterion("has_shriekwood_planks", hasItem(VenusRegistry.SHRIEKWOOD_PLANKS.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(VenusRegistry.SHRIEKWOOD_DOOR.get(), 3)
                .key('p', VenusRegistry.SHRIEKWOOD_PLANKS.get()).patternLine("pp ").patternLine("pp ")
                .patternLine("pp ")
                .addCriterion("has_shriekwood_planks", hasItem(VenusRegistry.SHRIEKWOOD_PLANKS.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(VenusRegistry.SHRIEKWOOD_SLAB.get(), 6)
                .key('p', VenusRegistry.SHRIEKWOOD_PLANKS.get()).patternLine("ppp").patternLine("   ")
                .patternLine("   ")
                .addCriterion("has_shriekwood_planks", hasItem(VenusRegistry.SHRIEKWOOD_PLANKS.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(VenusRegistry.POLISHED_PULCHERITE.get(), 4)
                .key('p', VenusRegistry.PULCHERITE.get()).patternLine("pp ").patternLine("pp ").patternLine("   ")
                .addCriterion("has_pulcherite", hasItem(VenusRegistry.PULCHERITE.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(VenusRegistry.PULCHERITE_BRICKS.get(), 4)
                .key('p', VenusRegistry.POLISHED_PULCHERITE.get()).patternLine("pp ").patternLine("pp ")
                .patternLine("   ").addCriterion("has_pulcherite", hasItem(VenusRegistry.PULCHERITE.get()))
                .build(consumer);
    }

}
