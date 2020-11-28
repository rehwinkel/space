package deerangle.space.registry;

import deerangle.space.main.SpaceMod;
import deerangle.space.recipe.BlastFurnaceRecipe;
import deerangle.space.recipe.BlastFurnaceRecipeSerializer;
import deerangle.space.recipe.RefineryRecipe;
import deerangle.space.recipe.RefineryRecipeSerializer;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class RecipeRegistry extends AbstractRegistry {

    public static final IRecipeType<BlastFurnaceRecipe> BLAST_FURNACE_TYPE = register("blast_furnace");
    public static final IRecipeType<RefineryRecipe> REFINERY_TYPE = register("refinery");

    public static RegistryObject<BlastFurnaceRecipeSerializer> BLAST_FURNACE = RECIPE_SERIALIZERS.register("blast_furnace", BlastFurnaceRecipeSerializer::new);
    public static RegistryObject<RefineryRecipeSerializer> REFINERY = RECIPE_SERIALIZERS.register("refinery", RefineryRecipeSerializer::new);

    static <T extends IRecipe<?>> IRecipeType<T> register(final String key) {
        return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(SpaceMod.MOD_ID, key), new IRecipeType<T>() {
            public String toString() {
                return key;
            }
        });
    }

}
