package deerangle.space.data;

import com.google.gson.JsonObject;
import deerangle.space.main.SpaceMod;
import deerangle.space.registry.RecipeRegistry;
import deerangle.space.registry.ResourceRegistry;
import deerangle.space.registry.MachineRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.CookingRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
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

        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ResourceRegistry.COPPER_ORE.get().asItem()), ResourceRegistry.COPPER_INGOT.get(), 0.7F, 200)
                .addCriterion("has_copper_ore", hasItem(ResourceRegistry.COPPER_ORE.get().asItem())).build(consumer);
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ResourceRegistry.ALUMINIUM_ORE.get().asItem()), ResourceRegistry.ALUMINIUM_INGOT.get(), 0.7F, 200)
                .addCriterion("has_aluminium_ore", hasItem(ResourceRegistry.ALUMINIUM_ORE.get().asItem())).build(consumer);
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ResourceRegistry.ILMENITE_ORE.get().asItem()), ResourceRegistry.TITANIUM_INGOT.get(), 2.0F, 200)
                .addCriterion("has_ilmenite_ore", hasItem(ResourceRegistry.ILMENITE_ORE.get().asItem())).build(consumer);
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ResourceRegistry.RUSTY_DUST.get().asItem()), ResourceRegistry.IRON_DUST.get(), 0.5F, 200)
                .addCriterion("has_rusty_dust", hasItem(ResourceRegistry.RUSTY_DUST.get().asItem())).build(consumer);

        CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(ResourceRegistry.COPPER_ORE.get()), ResourceRegistry.COPPER_INGOT.get(), 0.7F, 100)
                .addCriterion("has_copper_ore", hasItem(ResourceRegistry.COPPER_ORE.get())).build(consumer, "copper_ingot_from_blasting");
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(ResourceRegistry.ALUMINIUM_ORE.get()), ResourceRegistry.ALUMINIUM_INGOT.get(), 0.7F, 100)
                .addCriterion("has_aluminium_ore", hasItem(ResourceRegistry.ALUMINIUM_ORE.get())).build(consumer, "aluminium_ingot_from_blasting");
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(ResourceRegistry.ILMENITE_ORE.get()), ResourceRegistry.TITANIUM_INGOT.get(), 2.0F, 100)
                .addCriterion("has_ilmenite_ore", hasItem(ResourceRegistry.ILMENITE_ORE.get())).build(consumer, "titanium_ingot_from_blasting");

        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.QUARTZ_SAND.get()).key('q', ResourceRegistry.QUARTZ_DUST.get())
                .patternLine("qqq").patternLine("qqq").patternLine("qqq").addCriterion("has_quartz_dust", 
                hasItem(ResourceRegistry.QUARTZ_DUST.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(Items.IRON_NUGGET).key('i', ResourceRegistry.IRON_DUST.get())
                .patternLine("ii ").patternLine("ii ").patternLine("   ").addCriterion("has_iron_dust", 
                hasItem(ResourceRegistry.IRON_DUST.get())).build(consumer, "iron_nugget_from_iron_dust");
        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.COPPER_PIPE.get(), 4).key('c', ResourceRegistry.COPPER_INGOT.get())
                .patternLine("ccc").patternLine("c c").patternLine("ccc").addCriterion("has_copper_ingot", 
                hasItem(ResourceRegistry.COPPER_INGOT.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.STEEL_ROD.get(), 2).key('s', ResourceRegistry.STEEL_INGOT.get())
                .patternLine("s  ").patternLine("s  ").patternLine("   ").addCriterion("has_steel_ingot", 
                hasItem(ResourceRegistry.STEEL_INGOT.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.CYLINDER.get()).key('i', Items.IRON_INGOT).key('s', ResourceRegistry.STEEL_ROD.get())
                .patternLine("i  ").patternLine("s  ").patternLine("   ").addCriterion("has_steel_rod", 
                hasItem(ResourceRegistry.STEEL_ROD.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.IRON_PIPE.get(), 4).key('i', Items.IRON_INGOT)
                .patternLine("iii").patternLine("i i").patternLine("iii").addCriterion("has_iron_ingot", 
                hasItem(Items.IRON_INGOT)).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.BATTERY.get()).key('a', ResourceRegistry.ALUMINIUM_PLATE.get()).key('c', 
                ResourceRegistry.COPPER_PLATE.get()).key('r', Items.REDSTONE)
                .patternLine("aca").patternLine("ara").patternLine("aca").addCriterion("has_aluminium_plate", 
                hasItem(ResourceRegistry.ALUMINIUM_PLATE.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.MACHINE_BASE.get()).key('s', ResourceRegistry.STEEL_INGOT.get()).key('c', ResourceRegistry.COPPER_NUGGET.get())
                .patternLine("c c").patternLine("sss").patternLine("   ").addCriterion("has_steel_ingot", 
                hasItem(ResourceRegistry.STEEL_INGOT.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.INDUSTRIAL_PISTON.get()).key('s', ResourceRegistry.STEEL_INGOT.get()).key('i', Items.IRON_INGOT).key('c', Blocks.COBBLESTONE).key('r', Items.REDSTONE)
                .patternLine("crc").patternLine("cic").patternLine("sss").addCriterion("has_steel_ingot", 
                hasItem(ResourceRegistry.STEEL_INGOT.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(MachineRegistry.COAL_GENERATOR.get()).key('i', Items.IRON_INGOT).key('b', ResourceRegistry.MACHINE_BASE.get()).key('f', Blocks.FURNACE).key('c', ResourceRegistry.COPPER_PIPE.get())
                .patternLine("cic").patternLine("ifi").patternLine("ibi").addCriterion("has_machine_base", 
                hasItem(ResourceRegistry.MACHINE_BASE.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(MachineRegistry.BLAST_FURNACE.get()).key('f', Blocks.FURNACE).key('b', Blocks.IRON_BLOCK).key('c', ResourceRegistry.COPPER_PIPE.get()).key('i', Items.IRON_INGOT)
                .patternLine("cic").patternLine("cbi").patternLine("bfb").addCriterion("has_machine_base", 
                hasItem(ResourceRegistry.MACHINE_BASE.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.ROCKET_FIN.get()).key('i', ResourceRegistry.STEEL_PLATE.get()).key('t', ResourceRegistry.SILICA_TILE.get())
                .patternLine(" ti").patternLine("tti").patternLine("tii").addCriterion("has_silica_tile", 
                hasItem(ResourceRegistry.SILICA_TILE.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.IGNITION_COIL.get()).key('d', Items.DIAMOND).key('b', Items.BLAZE_ROD).key('s', ResourceRegistry.STEEL_NUGGET.get())
                .patternLine("sss").patternLine("dbd").patternLine("sss").addCriterion("has_silica_tile", 
                hasItem(ResourceRegistry.SILICA_TILE.get())).build(consumer);

        registerIngotRecipes(ResourceRegistry.COPPER_INGOT.get(), ResourceRegistry.COPPER_BLOCK.get(), ResourceRegistry.COPPER_NUGGET.get(), consumer, "copper");
        registerIngotRecipes(ResourceRegistry.STEEL_INGOT.get(), ResourceRegistry.STEEL_BLOCK.get(), ResourceRegistry.STEEL_NUGGET.get(), consumer, "steel");
        registerIngotRecipes(ResourceRegistry.ALUMINIUM_INGOT.get(), ResourceRegistry.ALUMINIUM_BLOCK.get(), ResourceRegistry.ALUMINIUM_NUGGET.get(), consumer, "aluminium");
        registerIngotRecipes(ResourceRegistry.TITANIUM_INGOT.get(), ResourceRegistry.TITANIUM_BLOCK.get(), ResourceRegistry.TITANIUM_NUGGET.get(), consumer, "titanium");

        //TODO: craft machines to machines without data
    }

    private void registerIngotRecipes(Item itemIngot, Block itemBlock, Item itemNugget, Consumer<IFinishedRecipe> consumer, String name) {
        ShapelessRecipeBuilder.shapelessRecipe(itemIngot, 9).addIngredient(itemBlock)
                .addCriterion("has_ingot", hasItem(itemIngot)).build(consumer, name + "_ingot_from_" + name + "_block");
        ShapelessRecipeBuilder.shapelessRecipe(itemBlock).addIngredient(itemIngot, 9)
                .addCriterion("has_ingot", hasItem(itemIngot)).build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(itemIngot).addIngredient(itemNugget, 9)
                .addCriterion("has_ingot", hasItem(itemIngot)).build(consumer, name + "_ingot_from_" + name + "_nugget");
        ShapelessRecipeBuilder.shapelessRecipe(itemNugget, 9).addIngredient(itemIngot)
                .addCriterion("has_ingot", hasItem(itemIngot)).build(consumer);
    }

    private class BlastFurnaceResult implements IFinishedRecipe {

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
