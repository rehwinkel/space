package deerangle.space.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import deerangle.space.recipe.BlastFurnaceRecipe;
import deerangle.space.registry.MachineRegistry;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class BlastFurnaceRecipeCategory implements IRecipeCategory<BlastFurnaceRecipe> {

    private final String localizedName;
    private IDrawable background;
    private IDrawable icon;

    protected final IDrawableStatic staticFlame;
    protected final IDrawableAnimated animatedFlame;
    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;

    public BlastFurnaceRecipeCategory(IGuiHelper guiHelper) {
        this.localizedName = I18n.format("jei_category.space.blast_furnace");
        this.background = guiHelper.createDrawable(SpaceModPlugin.JEI_BACKGROUND, 0, 114, 82, 54);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(MachineRegistry.BLAST_FURNACE.get()));
        this.staticFlame = guiHelper.createDrawable(SpaceModPlugin.JEI_BACKGROUND, 82, 114, 14, 14);
        this.animatedFlame = guiHelper
                .createAnimatedDrawable(this.staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true);
        this.cachedArrows = CacheBuilder.newBuilder().maximumSize(25L)
                .build(new CacheLoader<Integer, IDrawableAnimated>() {
                    public IDrawableAnimated load(Integer cookTime) {
                        return guiHelper.drawableBuilder(SpaceModPlugin.JEI_BACKGROUND, 82, 128, 24, 17)
                                .buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false);
                    }
                });
    }

    @Override
    public void draw(BlastFurnaceRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        this.animatedFlame.draw(matrixStack, 1, 20);
        IDrawableAnimated arrow = this.getArrow(recipe);
        arrow.draw(matrixStack, 24, 18);
        this.drawCookTime(recipe, matrixStack, 45);
    }

    protected IDrawableAnimated getArrow(BlastFurnaceRecipe recipe) {
        int cookTime = recipe.getDuration();
        return this.cachedArrows.getUnchecked(cookTime);
    }

    protected void drawCookTime(BlastFurnaceRecipe recipe, MatrixStack matrixStack, int y) {
        int cookTime = recipe.getDuration();
        if (cookTime > 0) {
            int cookTimeSeconds = cookTime / 20;
            TranslationTextComponent timeString = new TranslationTextComponent("gui.jei.category.smelting.time.seconds",
                    cookTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            FontRenderer fontRenderer = minecraft.fontRenderer;
            int stringWidth = fontRenderer.getStringPropertyWidth(timeString);
            fontRenderer.func_243248_b(matrixStack, timeString, (float) (this.background.getWidth() - stringWidth),
                    (float) y, -8355712);
        }

    }

    @Override
    public ResourceLocation getUid() {
        return SpaceModPlugin.BLAST_FURNACE;
    }

    @Override
    public Class<? extends BlastFurnaceRecipe> getRecipeClass() {
        return BlastFurnaceRecipe.class;
    }

    @Override
    public String getTitle() {
        return localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setIngredients(BlastFurnaceRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(ImmutableList.of(recipe.getInput()));
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getCraftingResult(null));
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, BlastFurnaceRecipe blastFurnaceRecipe, IIngredients ingredients) {
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
        itemStacks.init(0, true, 0, 0);
        itemStacks.init(1, false, 60, 18);
        itemStacks.set(ingredients);
    }

}
