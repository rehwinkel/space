package deerangle.space.data;

import deerangle.space.registry.FluidRegistry;
import deerangle.space.registry.MachineRegistry;
import deerangle.space.registry.ResourceRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.data.CookingRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Consumer;

public class RecipeGenerator extends AbstractRecipeGenerator {

    public RecipeGenerator(DataGenerator generatorIn) {
        super(generatorIn);
    }

    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        addSpaceBlastFurnaceRecipe(consumer, Ingredient.fromTag(Tags.Items.INGOTS_IRON), ResourceRegistry.STEEL_INGOT.get(), 200 * 16);
        addSpaceBlastFurnaceRecipe(consumer, Ingredient.fromItems(Blocks.IRON_BLOCK), ResourceRegistry.STEEL_BLOCK.get().asItem(), 200 * 16 * 9);
        addSpaceBlastFurnaceRecipe(consumer, Ingredient.fromItems(Blocks.SAND), ResourceRegistry.QUARTZ_DUST.get(), 40 * 10);
        addSpaceBlastFurnaceRecipe(consumer, Ingredient.fromItems(ResourceRegistry.QUARTZ_SAND.get()), ResourceRegistry.SILICA_TILE.get(), 40 * 30, "_from_quartz_sand");
        addSpaceBlastFurnaceRecipe(consumer, Ingredient.fromItems(Blocks.QUARTZ_BLOCK), ResourceRegistry.SILICA_TILE.get(), 40 * 30, "_from_quartz_block");
        addSpaceBlastFurnaceRecipe(consumer, Ingredient.fromItems(Blocks.SOUL_SAND), ResourceRegistry.HEAT_RESISTENT_GLASS.get(), 40 * 60);

        addRefineryRecipe(consumer, new FluidStack(FluidRegistry.CRUDE_OIL.get(), 2), new FluidStack(FluidRegistry.KEROSENE.get(), 1), 4);

        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ResourceRegistry.COPPER_ORE.get().asItem()), ResourceRegistry.COPPER_INGOT.get(), 0.7F, 200).addCriterion("has_copper_ore", hasItem(ResourceRegistry.COPPER_ORE.get().asItem())).build(consumer);
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ResourceRegistry.ALUMINIUM_ORE.get().asItem()), ResourceRegistry.ALUMINIUM_INGOT.get(), 0.7F, 200).addCriterion("has_aluminium_ore", hasItem(ResourceRegistry.ALUMINIUM_ORE.get().asItem())).build(consumer);
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ResourceRegistry.ILMENITE_ORE.get().asItem()), ResourceRegistry.TITANIUM_INGOT.get(), 2.0F, 200).addCriterion("has_ilmenite_ore", hasItem(ResourceRegistry.ILMENITE_ORE.get().asItem())).build(consumer);

        CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(ResourceRegistry.COPPER_ORE.get()), ResourceRegistry.COPPER_INGOT.get(), 0.7F, 100).addCriterion("has_copper_ore", hasItem(ResourceRegistry.COPPER_ORE.get())).build(consumer, "copper_ingot_from_blasting");
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(ResourceRegistry.ALUMINIUM_ORE.get()), ResourceRegistry.ALUMINIUM_INGOT.get(), 0.7F, 100).addCriterion("has_aluminium_ore", hasItem(ResourceRegistry.ALUMINIUM_ORE.get())).build(consumer, "aluminium_ingot_from_blasting");
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(ResourceRegistry.ILMENITE_ORE.get()), ResourceRegistry.TITANIUM_INGOT.get(), 2.0F, 100).addCriterion("has_ilmenite_ore", hasItem(ResourceRegistry.ILMENITE_ORE.get())).build(consumer, "titanium_ingot_from_blasting");

        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.QUARTZ_SAND.get()).key('q', ResourceRegistry.QUARTZ_DUST.get()).patternLine("qqq").patternLine("qqq").patternLine("qqq").addCriterion("has_quartz_dust", hasItem(ResourceRegistry.QUARTZ_DUST.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(Items.IRON_NUGGET).key('i', ResourceRegistry.IRON_DUST.get()).patternLine("ii ").patternLine("ii ").patternLine("   ").addCriterion("has_iron_dust", hasItem(ResourceRegistry.IRON_DUST.get())).build(consumer, "iron_nugget_from_iron_dust");
        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.COPPER_TUBE.get(), 4).key('c', ResourceRegistry.COPPER_INGOT.get()).patternLine("ccc").patternLine("c c").patternLine("ccc").addCriterion("has_copper_ingot", hasItem(ResourceRegistry.COPPER_INGOT.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.STEEL_ROD.get(), 2).key('s', ResourceRegistry.STEEL_INGOT.get()).patternLine("s  ").patternLine("s  ").patternLine("   ").addCriterion("has_steel_ingot", hasItem(ResourceRegistry.STEEL_INGOT.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.CYLINDER.get()).key('i', Items.IRON_INGOT).key('s', ResourceRegistry.STEEL_ROD.get()).patternLine("i  ").patternLine("s  ").patternLine("   ").addCriterion("has_steel_rod", hasItem(ResourceRegistry.STEEL_ROD.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.IRON_TUBE.get(), 4).key('i', Items.IRON_INGOT).patternLine("iii").patternLine("i i").patternLine("iii").addCriterion("has_iron_ingot", hasItem(Items.IRON_INGOT)).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.BATTERY.get()).key('a', ResourceRegistry.ALUMINIUM_PLATE.get()).key('c', ResourceRegistry.COPPER_PLATE.get()).key('r', Items.REDSTONE).patternLine("aca").patternLine("ara").patternLine("aca").addCriterion("has_aluminium_plate", hasItem(ResourceRegistry.ALUMINIUM_PLATE.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.MACHINE_BASE.get()).key('s', ResourceRegistry.STEEL_INGOT.get()).key('c', ResourceRegistry.COPPER_NUGGET.get()).patternLine("c c").patternLine("sss").patternLine("   ").addCriterion("has_steel_ingot", hasItem(ResourceRegistry.STEEL_INGOT.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.INDUSTRIAL_PISTON.get()).key('s', ResourceRegistry.STEEL_INGOT.get()).key('i', Items.IRON_INGOT).key('c', Blocks.COBBLESTONE).key('r', Items.REDSTONE).patternLine("crc").patternLine("cic").patternLine("sss").addCriterion("has_steel_ingot", hasItem(ResourceRegistry.STEEL_INGOT.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(MachineRegistry.COAL_GENERATOR.get()).key('i', Items.IRON_INGOT).key('b', ResourceRegistry.MACHINE_BASE.get()).key('f', Blocks.FURNACE).key('c', ResourceRegistry.COPPER_TUBE.get()).patternLine("cic").patternLine("ifi").patternLine("ibi").addCriterion("has_machine_base", hasItem(ResourceRegistry.MACHINE_BASE.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(MachineRegistry.BLAST_FURNACE.get()).key('f', Blocks.FURNACE).key('b', Blocks.IRON_BLOCK).key('c', ResourceRegistry.COPPER_TUBE.get()).key('i', Items.IRON_INGOT).patternLine("cic").patternLine("cbi").patternLine("bfb").addCriterion("has_machine_base", hasItem(ResourceRegistry.MACHINE_BASE.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.ROCKET_FIN.get()).key('i', ResourceRegistry.STEEL_PLATE.get()).key('t', ResourceRegistry.SILICA_TILE.get()).patternLine(" ti").patternLine("tti").patternLine("tii").addCriterion("has_silica_tile", hasItem(ResourceRegistry.SILICA_TILE.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.IGNITION_COIL.get()).key('d', Items.DIAMOND).key('b', Items.BLAZE_ROD).key('s', ResourceRegistry.STEEL_NUGGET.get()).patternLine("sss").patternLine("dbd").patternLine("sss").addCriterion("has_silica_tile", hasItem(ResourceRegistry.SILICA_TILE.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.HEATING_COIL.get()).key('c', ResourceRegistry.COPPER_INGOT.get()).key('s', ResourceRegistry.STEEL_ROD.get()).patternLine("ccc").patternLine("csc").patternLine("ccc").addCriterion("has_copper_ingot", hasItem(ResourceRegistry.COPPER_INGOT.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(MachineRegistry.CABLE.get()).key('w', Blocks.BLACK_WOOL).key('r', Items.REDSTONE).patternLine("wrw").patternLine("rrr").patternLine("wrw").addCriterion("has_redstone_ingot", hasItem(Items.REDSTONE)).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(MachineRegistry.DRUM.get()).key('a', ResourceRegistry.ALUMINIUM_INGOT.get()).key('p', ResourceRegistry.ALUMINIUM_PLATE.get()).patternLine("apa").patternLine("a a").patternLine("apa").addCriterion("has_aluminium_plate", hasItem(ResourceRegistry.ALUMINIUM_PLATE.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(MachineRegistry.BATTERY_PACK.get()).key('b', ResourceRegistry.MACHINE_BASE.get()).key('i', Items.IRON_BARS).key('a', ResourceRegistry.BATTERY.get()).patternLine("ibi").patternLine("aaa").patternLine("ibi").addCriterion("has_battery", hasItem(ResourceRegistry.BATTERY.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(MachineRegistry.REFINERY.get()).key('b', ResourceRegistry.MACHINE_BASE.get()).key('g', Blocks.GLASS).key('c', ResourceRegistry.COPPER_TUBE.get()).patternLine("c c").patternLine("ggc").patternLine("cbc").addCriterion("has_machine_base", hasItem(ResourceRegistry.MACHINE_BASE.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.ISOLATING_FABRIC.get()).key('w', Blocks.WHITE_WOOL).key('s', ResourceRegistry.SILICA_TILE.get()).patternLine("ss ").patternLine("ww ").patternLine("   ").addCriterion("has_silica_tile", hasItem(ResourceRegistry.SILICA_TILE.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(MachineRegistry.GAS_TANK.get()).key('s', ResourceRegistry.STEEL_INGOT.get()).key('g', Items.GOLD_INGOT).patternLine("sgs").patternLine("s s").patternLine("sss").addCriterion("has_steel_ingot", hasItem(ResourceRegistry.STEEL_INGOT.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(MachineRegistry.COMBUSTION_GENERATOR.get()).key('b', ResourceRegistry.MACHINE_BASE.get()).key('g', Items.GOLD_INGOT).key('i', Blocks.IRON_BLOCK).key('c', ResourceRegistry.COPPER_TUBE.get()).key('s', ResourceRegistry.STEEL_BLOCK.get()).key('a', ResourceRegistry.CYLINDER.get()).patternLine("gcg").patternLine("asa").patternLine("ibi").addCriterion("has_machine_base", hasItem(ResourceRegistry.MACHINE_BASE.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.EMPTY_CAN.get()).key('a', ResourceRegistry.ALUMINIUM_INGOT.get()).patternLine("aaa").patternLine("aaa").patternLine("   ").addCriterion("has_aluminium_ingot", hasItem(ResourceRegistry.ALUMINIUM_INGOT.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.CONTROLLER.get()).key('c', Items.COMPARATOR).key('d', Items.DIAMOND).key('b', ResourceRegistry.MACHINE_BASE.get()).key('p', ResourceRegistry.COPPER_PLATE.get()).key('e', MachineRegistry.BATTERY_PACK.get()).key('t', ResourceRegistry.SILICA_TILE.get()).patternLine("tct").patternLine("pde").patternLine("cbc").addCriterion("has_machine_base", hasItem(ResourceRegistry.MACHINE_BASE.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.ROCKET_CONE.get()).key('t', ResourceRegistry.SILICA_TILE.get()).key('a', ResourceRegistry.ALUMINIUM_PLATE.get()).key('c', ResourceRegistry.CONTROLLER.get()).patternLine("ttt").patternLine("tat").patternLine("aca").addCriterion("has_controller", hasItem(ResourceRegistry.CONTROLLER.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.ROCKET_THRUSTER.get()).key('t', MachineRegistry.GAS_TANK.get()).key('f', ResourceRegistry.IGNITION_COIL.get()).key('s', ResourceRegistry.SILICA_TILE.get()).key('i', ResourceRegistry.TITANIUM_BLOCK.get()).patternLine("sts").patternLine("ifi").patternLine("i i").addCriterion("has_ignition_coil", hasItem(ResourceRegistry.IGNITION_COIL.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ResourceRegistry.PLATED_ROCKET_BODY.get()).key('t', ResourceRegistry.SILICA_TILE.get()).key('a', ResourceRegistry.ALUMINIUM_INGOT.get()).patternLine("tat").patternLine("tat").patternLine("tat").addCriterion("has_silica_tile", hasItem(ResourceRegistry.SILICA_TILE.get())).build(consumer);

        registerIngotRecipes(ResourceRegistry.COPPER_INGOT.get(), ResourceRegistry.COPPER_BLOCK.get(), ResourceRegistry.COPPER_NUGGET.get(), consumer, "copper");
        registerIngotRecipes(ResourceRegistry.STEEL_INGOT.get(), ResourceRegistry.STEEL_BLOCK.get(), ResourceRegistry.STEEL_NUGGET.get(), consumer, "steel");
        registerIngotRecipes(ResourceRegistry.ALUMINIUM_INGOT.get(), ResourceRegistry.ALUMINIUM_BLOCK.get(), ResourceRegistry.ALUMINIUM_NUGGET.get(), consumer, "aluminium");
        registerIngotRecipes(ResourceRegistry.TITANIUM_INGOT.get(), ResourceRegistry.TITANIUM_BLOCK.get(), ResourceRegistry.TITANIUM_NUGGET.get(), consumer, "titanium");

        addMachineRemoveNBTRecipe(MachineRegistry.COAL_GENERATOR.get(), consumer);
        addMachineRemoveNBTRecipe(MachineRegistry.BLAST_FURNACE.get(), consumer);
        addMachineRemoveNBTRecipe(MachineRegistry.COMBUSTION_GENERATOR.get(), consumer);
        addMachineRemoveNBTRecipe(MachineRegistry.GAS_TANK.get(), consumer);
        addMachineRemoveNBTRecipe(MachineRegistry.DRUM.get(), consumer);
        addMachineRemoveNBTRecipe(MachineRegistry.REFINERY.get(), consumer);
    }
}
