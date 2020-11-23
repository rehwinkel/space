package deerangle.space.data;

import com.google.gson.JsonObject;
import deerangle.space.recipe.RefineryRecipeSerializer;
import deerangle.space.registry.FluidRegistry;
import deerangle.space.registry.MachineRegistry;
import deerangle.space.registry.RecipeRegistry;
import deerangle.space.registry.ResourceRegistry;
import deerangle.space.planets.venus.VenusRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Consumer;

public class RecipeGenerator extends RecipeProvider {

    public RecipeGenerator(DataGenerator generatorIn) {
        super(generatorIn);
    }

    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        addSpaceBlastFurnaceRecipe(consumer, Ingredient.fromTag(Tags.Items.INGOTS_IRON),
                ResourceRegistry.STEEL_INGOT.get(), 200 * 16);

        addRefineryRecipe(consumer, new FluidStack(FluidRegistry.CRUDE_OIL.get(), 2),
                new FluidStack(FluidRegistry.KEROSENE.get(), 1), 4);

        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ResourceRegistry.COPPER_ORE.get().asItem()),
                ResourceRegistry.COPPER_INGOT.get(), 0.7F, 200)
                .addCriterion("has_copper_ore", hasItem(ResourceRegistry.COPPER_ORE.get().asItem())).build(consumer);
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ResourceRegistry.ALUMINIUM_ORE.get().asItem()),
                ResourceRegistry.ALUMINIUM_INGOT.get(), 0.7F, 200)
                .addCriterion("has_aluminium_ore", hasItem(ResourceRegistry.ALUMINIUM_ORE.get().asItem()))
                .build(consumer);
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ResourceRegistry.ILMENITE_ORE.get().asItem()),
                ResourceRegistry.TITANIUM_INGOT.get(), 2.0F, 200)
                .addCriterion("has_ilmenite_ore", hasItem(ResourceRegistry.ILMENITE_ORE.get().asItem()))
                .build(consumer);
        /*TODO
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ResourceRegistry.RUSTY_DUST.get().asItem()),
                ResourceRegistry.IRON_DUST.get(), 0.5F, 200)
                .addCriterion("has_rusty_dust", hasItem(ResourceRegistry.RUSTY_DUST.get().asItem())).build(consumer);
         */

        CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(ResourceRegistry.COPPER_ORE.get()),
                ResourceRegistry.COPPER_INGOT.get(), 0.7F, 100)
                .addCriterion("has_copper_ore", hasItem(ResourceRegistry.COPPER_ORE.get()))
                .build(consumer, "copper_ingot_from_blasting");
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(ResourceRegistry.ALUMINIUM_ORE.get()),
                ResourceRegistry.ALUMINIUM_INGOT.get(), 0.7F, 100)
                .addCriterion("has_aluminium_ore", hasItem(ResourceRegistry.ALUMINIUM_ORE.get()))
                .build(consumer, "aluminium_ingot_from_blasting");
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(ResourceRegistry.ILMENITE_ORE.get()),
                ResourceRegistry.TITANIUM_INGOT.get(), 2.0F, 100)
                .addCriterion("has_ilmenite_ore", hasItem(ResourceRegistry.ILMENITE_ORE.get()))
                .build(consumer, "titanium_ingot_from_blasting");

        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.QUARTZ_SAND.get())
                .key('q', ResourceRegistry.QUARTZ_DUST.get()).patternLine("qqq").patternLine("qqq").patternLine("qqq")
                .addCriterion("has_quartz_dust", hasItem(ResourceRegistry.QUARTZ_DUST.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(Items.IRON_NUGGET).key('i', ResourceRegistry.IRON_DUST.get())
                .patternLine("ii ").patternLine("ii ").patternLine("   ")
                .addCriterion("has_iron_dust", hasItem(ResourceRegistry.IRON_DUST.get()))
                .build(consumer, "iron_nugget_from_iron_dust");
        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.COPPER_TUBE.get(), 4)
                .key('c', ResourceRegistry.COPPER_INGOT.get()).patternLine("ccc").patternLine("c c").patternLine("ccc")
                .addCriterion("has_copper_ingot", hasItem(ResourceRegistry.COPPER_INGOT.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.STEEL_ROD.get(), 2)
                .key('s', ResourceRegistry.STEEL_INGOT.get()).patternLine("s  ").patternLine("s  ").patternLine("   ")
                .addCriterion("has_steel_ingot", hasItem(ResourceRegistry.STEEL_INGOT.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.CYLINDER.get()).key('i', Items.IRON_INGOT)
                .key('s', ResourceRegistry.STEEL_ROD.get()).patternLine("i  ").patternLine("s  ").patternLine("   ")
                .addCriterion("has_steel_rod", hasItem(ResourceRegistry.STEEL_ROD.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.IRON_TUBE.get(), 4).key('i', Items.IRON_INGOT)
                .patternLine("iii").patternLine("i i").patternLine("iii")
                .addCriterion("has_iron_ingot", hasItem(Items.IRON_INGOT)).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.BATTERY.get())
                .key('a', ResourceRegistry.ALUMINIUM_PLATE.get()).key('c', ResourceRegistry.COPPER_PLATE.get())
                .key('r', Items.REDSTONE).patternLine("aca").patternLine("ara").patternLine("aca")
                .addCriterion("has_aluminium_plate", hasItem(ResourceRegistry.ALUMINIUM_PLATE.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.MACHINE_BASE.get())
                .key('s', ResourceRegistry.STEEL_INGOT.get()).key('c', ResourceRegistry.COPPER_NUGGET.get())
                .patternLine("c c").patternLine("sss").patternLine("   ")
                .addCriterion("has_steel_ingot", hasItem(ResourceRegistry.STEEL_INGOT.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.INDUSTRIAL_PISTON.get())
                .key('s', ResourceRegistry.STEEL_INGOT.get()).key('i', Items.IRON_INGOT).key('c', Blocks.COBBLESTONE)
                .key('r', Items.REDSTONE).patternLine("crc").patternLine("cic").patternLine("sss")
                .addCriterion("has_steel_ingot", hasItem(ResourceRegistry.STEEL_INGOT.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(MachineRegistry.COAL_GENERATOR.get()).key('i', Items.IRON_INGOT)
                .key('b', ResourceRegistry.MACHINE_BASE.get()).key('f', Blocks.FURNACE)
                .key('c', ResourceRegistry.COPPER_TUBE.get()).patternLine("cic").patternLine("ifi").patternLine("ibi")
                .addCriterion("has_machine_base", hasItem(ResourceRegistry.MACHINE_BASE.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(MachineRegistry.BLAST_FURNACE.get()).key('f', Blocks.FURNACE)
                .key('b', Blocks.IRON_BLOCK).key('c', ResourceRegistry.COPPER_TUBE.get()).key('i', Items.IRON_INGOT)
                .patternLine("cic").patternLine("cbi").patternLine("bfb")
                .addCriterion("has_machine_base", hasItem(ResourceRegistry.MACHINE_BASE.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.ROCKET_FIN.get()).key('i', ResourceRegistry.STEEL_PLATE.get())
                .key('t', ResourceRegistry.SILICA_TILE.get()).patternLine(" ti").patternLine("tti").patternLine("tii")
                .addCriterion("has_silica_tile", hasItem(ResourceRegistry.SILICA_TILE.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.IGNITION_COIL.get()).key('d', Items.DIAMOND)
                .key('b', Items.BLAZE_ROD).key('s', ResourceRegistry.STEEL_NUGGET.get()).patternLine("sss")
                .patternLine("dbd").patternLine("sss")
                .addCriterion("has_silica_tile", hasItem(ResourceRegistry.SILICA_TILE.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.HEATING_COIL.get()).key('c', ResourceRegistry.COPPER_INGOT.get()).key('s', ResourceRegistry.STEEL_ROD.get())
                .patternLine("ccc").patternLine("csc").patternLine("ccc")
                .addCriterion("has_copper_ingot", hasItem(ResourceRegistry.COPPER_INGOT.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(MachineRegistry.CABLE.get()).key('w', Blocks.BLACK_WOOL).key('r', Items.REDSTONE)
                .patternLine("wrw").patternLine("rrr").patternLine("wrw")
                .addCriterion("has_redstone_ingot", hasItem(Items.REDSTONE)).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(MachineRegistry.DRUM.get()).key('a', ResourceRegistry.ALUMINIUM_INGOT.get()).key('p', ResourceRegistry.ALUMINIUM_PLATE.get())
                .patternLine("apa").patternLine("a a").patternLine("apa")
                .addCriterion("has_aluminium_plate", hasItem(ResourceRegistry.ALUMINIUM_PLATE.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(MachineRegistry.BATTERY_PACK.get()).key('b', ResourceRegistry.MACHINE_BASE.get()).key('i', Items.IRON_BARS).key('a', ResourceRegistry.BATTERY.get())
                .patternLine("ibi").patternLine("aaa").patternLine("ibi")
                .addCriterion("has_battery", hasItem(ResourceRegistry.BATTERY.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(MachineRegistry.REFINERY.get()).key('b', ResourceRegistry.MACHINE_BASE.get()).key('g', Blocks.GLASS).key('c', ResourceRegistry.COPPER_TUBE.get())
                .patternLine("c c").patternLine("ggc").patternLine("cbc")
                .addCriterion("has_machine_base", hasItem(ResourceRegistry.MACHINE_BASE.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.ISOLATING_FABRIC.get()).key('w', Blocks.WHITE_WOOL).key('s', ResourceRegistry.SILICA_TILE.get())
                .patternLine("ss ").patternLine("ww ").patternLine("   ")
                .addCriterion("has_silica_tile", hasItem(ResourceRegistry.SILICA_TILE.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(VenusRegistry.SHRIEKWOOD_WOOD.get(), 3).key('l', VenusRegistry.SHRIEKWOOD_LOG.get())
                .patternLine("ll ").patternLine("ll ").patternLine("   ")
                .addCriterion("has_shriekwood_log", hasItem(VenusRegistry.SHRIEKWOOD_LOG.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(MachineRegistry.GAS_TANK.get()).key('s', ResourceRegistry.STEEL_INGOT.get()).key('g', Items.GOLD_INGOT)
                .patternLine("sgs").patternLine("s s").patternLine("sss")
                .addCriterion("has_steel_ingot", hasItem(ResourceRegistry.STEEL_INGOT.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(VenusRegistry.SHRIEKWOOD_PLANKS.get(), 4).key('l', VenusRegistry.SHRIEKWOOD_LOG.get())
                .patternLine("l  ").patternLine("   ").patternLine("   ")
                .addCriterion("has_shriekwood_log", hasItem(VenusRegistry.SHRIEKWOOD_LOG.get())).build(consumer, "shriekwood_planks_from_shriekwood_log");
        ShapedRecipeBuilder.shapedRecipe(VenusRegistry.SHRIEKWOOD_PLANKS.get(), 4).key('w', VenusRegistry.SHRIEKWOOD_WOOD.get())
                .patternLine("w  ").patternLine("   ").patternLine("   ")
                .addCriterion("has_shriekwood_wood", hasItem(VenusRegistry.SHRIEKWOOD_WOOD.get())).build(consumer, "shriekwood_planks_from_shriekwood_wood");
        ShapedRecipeBuilder.shapedRecipe(VenusRegistry.SHRIEKWOOD_STAIRS.get(), 4).key('p', VenusRegistry.SHRIEKWOOD_PLANKS.get())
                .patternLine("p  ").patternLine("pp ").patternLine("ppp")
                .addCriterion("has_shriekwood_planks", hasItem(VenusRegistry.SHRIEKWOOD_PLANKS.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(VenusRegistry.SHRIEKWOOD_DOOR.get(), 3).key('p', VenusRegistry.SHRIEKWOOD_PLANKS.get())
                .patternLine("pp ").patternLine("pp ").patternLine("pp ")
                .addCriterion("has_shriekwood_planks", hasItem(VenusRegistry.SHRIEKWOOD_PLANKS.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(VenusRegistry.SHRIEKWOOD_SLAB.get(), 6).key('p', VenusRegistry.SHRIEKWOOD_PLANKS.get())
                .patternLine("ppp").patternLine("   ").patternLine("   ")
                .addCriterion("has_shriekwood_planks", hasItem(VenusRegistry.SHRIEKWOOD_PLANKS.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(VenusRegistry.POLISHED_PULCHERITE.get(), 4).key('p', VenusRegistry.PULCHERITE.get())
                .patternLine("pp ").patternLine("pp ").patternLine("   ")
                .addCriterion("has_pulcherite", hasItem(VenusRegistry.PULCHERITE.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(VenusRegistry.PULCHERITE_BRICKS.get(), 4).key('p', VenusRegistry.POLISHED_PULCHERITE.get())
                .patternLine("pp ").patternLine("pp ").patternLine("   ")
                .addCriterion("has_pulcherite", hasItem(VenusRegistry.PULCHERITE.get())).build(consumer);

        registerIngotRecipes(ResourceRegistry.COPPER_INGOT.get(), ResourceRegistry.COPPER_BLOCK.get(),
                ResourceRegistry.COPPER_NUGGET.get(), consumer, "copper");
        registerIngotRecipes(ResourceRegistry.STEEL_INGOT.get(), ResourceRegistry.STEEL_BLOCK.get(),
                ResourceRegistry.STEEL_NUGGET.get(), consumer, "steel");
        registerIngotRecipes(ResourceRegistry.ALUMINIUM_INGOT.get(), ResourceRegistry.ALUMINIUM_BLOCK.get(),
                ResourceRegistry.ALUMINIUM_NUGGET.get(), consumer, "aluminium");
        registerIngotRecipes(ResourceRegistry.TITANIUM_INGOT.get(), ResourceRegistry.TITANIUM_BLOCK.get(),
                ResourceRegistry.TITANIUM_NUGGET.get(), consumer, "titanium");

        addMachineRemoveNBTRecipe(MachineRegistry.COAL_GENERATOR.get(), consumer);
        addMachineRemoveNBTRecipe(MachineRegistry.BLAST_FURNACE.get(), consumer);
        addMachineRemoveNBTRecipe(MachineRegistry.COMBUSTION_GENERATOR.get(), consumer);
        addMachineRemoveNBTRecipe(MachineRegistry.GAS_TANK.get(), consumer);
        addMachineRemoveNBTRecipe(MachineRegistry.DRUM.get(), consumer);
    }

    private void addSpaceBlastFurnaceRecipe(Consumer<IFinishedRecipe> consumer, Ingredient input, Item result, int duration) {
        ResourceLocation loc = result.getRegistryName();
        consumer.accept(new BlastFurnaceResult(
                new ResourceLocation(loc.getNamespace(), loc.getPath() + "_from_space_blast_furnace"), input, result,
                duration));
    }

    private void addRefineryRecipe(Consumer<IFinishedRecipe> consumer, FluidStack input, FluidStack result, int duration) {
        ResourceLocation loc = result.getFluid().getRegistryName();
        consumer.accept(
                new RefineryResult(new ResourceLocation(loc.getNamespace(), loc.getPath() + "_from_refinery"), input,
                        result, duration));
    }

    private void addMachineRemoveNBTRecipe(Block block, Consumer<IFinishedRecipe> consumer) {
        String name = block.getRegistryName().getPath();
        ShapelessRecipeBuilder.shapelessRecipe(block).addIngredient(block).addCriterion("has_" + name, hasItem(block))
                .build(consumer, block.getRegistryName().getNamespace() + ":" + name + "_remove_nbt");
    }

    private void registerIngotRecipes(Item itemIngot, Block itemBlock, Item itemNugget, Consumer<IFinishedRecipe> consumer, String name) {
        ShapelessRecipeBuilder.shapelessRecipe(itemIngot, 9).addIngredient(itemBlock)
                .addCriterion("has_ingot", hasItem(itemIngot)).build(consumer, name + "_ingot_from_" + name + "_block");
        ShapelessRecipeBuilder.shapelessRecipe(itemBlock).addIngredient(itemIngot, 9)
                .addCriterion("has_ingot", hasItem(itemIngot)).build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(itemIngot).addIngredient(itemNugget, 9)
                .addCriterion("has_ingot", hasItem(itemIngot))
                .build(consumer, name + "_ingot_from_" + name + "_nugget");
        ShapelessRecipeBuilder.shapelessRecipe(itemNugget, 9).addIngredient(itemIngot)
                .addCriterion("has_ingot", hasItem(itemIngot)).build(consumer);
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

    private class RefineryResult implements IFinishedRecipe {
        private final ResourceLocation recipeId;
        private final FluidStack input;
        private final FluidStack result;
        private final int duration;

        public RefineryResult(ResourceLocation recipeId, FluidStack input, FluidStack result, int duration) {
            this.recipeId = recipeId;
            this.input = input;
            this.result = result;
            this.duration = duration;
        }

        @Override
        public void serialize(JsonObject json) {
            json.add("input", RefineryRecipeSerializer.writeFluidStack(this.input));
            json.add("result", RefineryRecipeSerializer.writeFluidStack(this.result));
            json.addProperty("duration", this.duration);
        }

        @Override
        public ResourceLocation getID() {
            return this.recipeId;
        }

        @Override
        public IRecipeSerializer<?> getSerializer() {
            return RecipeRegistry.REFINERY.get();
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
