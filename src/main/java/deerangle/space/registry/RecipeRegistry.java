package deerangle.space.registry;

import deerangle.space.main.SpaceMod;
import deerangle.space.recipe.BlastFurnaceRecipe;
import deerangle.space.recipe.BlastFurnaceRecipeSerializer;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.SmithingRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class RecipeRegistry extends AbstractRegistry {

    public static RegistryObject<BlastFurnaceRecipeSerializer> BLAST_FURNACE = RECIPE_SERIALIZERS
            .register("blast_furnace", BlastFurnaceRecipeSerializer::new);

    public static final IRecipeType<BlastFurnaceRecipe> BLAST_FURNACE_TYPE = register("blast_furnace");

    static <T extends IRecipe<?>> IRecipeType<T> register(final String key) {
        return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(SpaceMod.MOD_ID, key), new IRecipeType<T>() {
            public String toString() {
                return key;
            }
        });
    }

}
