package deerangle.space.jei;

import deerangle.space.main.SpaceMod;
import deerangle.space.recipe.BlastFurnaceRecipe;
import deerangle.space.registry.MachineRegistry;
import deerangle.space.registry.RecipeRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

import java.util.List;

@JeiPlugin
public class SpaceModPlugin implements IModPlugin {

    public static final ResourceLocation BLAST_FURNACE = new ResourceLocation(SpaceMod.MOD_ID, "blast_furnace");
    public static final ResourceLocation JEI_BACKGROUND = new ResourceLocation("jei", "textures/gui/gui_vanilla.png");
    public static final ResourceLocation SPACE_BACKGROUND = new ResourceLocation(SpaceMod.MOD_ID, "textures/jei/gui.png");

    private BlastFurnaceRecipeCategory blastFurnaceCategory;

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(SpaceMod.MOD_ID, "jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        registration.addRecipeCategories(this.blastFurnaceCategory = new BlastFurnaceRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager manager = Minecraft.getInstance().world.getRecipeManager();
        List<BlastFurnaceRecipe> recipes = manager.getRecipesForType(RecipeRegistry.BLAST_FURNACE_TYPE);
        registration.addRecipes(recipes, BLAST_FURNACE);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(MachineRegistry.BLAST_FURNACE.get()), BLAST_FURNACE);
    }

}
