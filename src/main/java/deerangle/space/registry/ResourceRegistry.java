package deerangle.space.registry;

import deerangle.space.main.SpaceMod;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.*;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;

public class ResourceRegistry extends AbstractRegistry {

    public static final RegistryObject<Block> COPPER_ORE = BLOCKS.register("copper_ore", () -> new OreBlock(
            AbstractBlock.Properties.create(Material.ROCK).setRequiresTool().harvestLevel(1)
                    .hardnessAndResistance(3.0F, 3.0F)));
    public static final ItemGroup TAB = new ItemGroup(SpaceMod.MOD_ID + ".resource") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(COPPER_ORE.get());
        }
    };
    public static final RegistryObject<Block> ALUMINIUM_ORE = BLOCKS.register("aluminium_ore", () -> new OreBlock(
            AbstractBlock.Properties.create(Material.ROCK).setRequiresTool().harvestLevel(1)
                    .hardnessAndResistance(3.0F, 3.0F)));
    public static final RegistryObject<Block> ILMENITE_ORE = BLOCKS.register("ilmenite_ore", () -> new OreBlock(
            AbstractBlock.Properties.create(Material.ROCK).setRequiresTool().harvestLevel(2)
                    .hardnessAndResistance(3.0F, 3.0F)));
    public static final RegistryObject<Block> ALUMINIUM_BLOCK = BLOCKS.register("aluminium_block", () -> new Block(
            AbstractBlock.Properties.create(Material.IRON).setRequiresTool().harvestLevel(1)
                    .hardnessAndResistance(3.0F, 3.0F)));
    public static final RegistryObject<Block> COPPER_BLOCK = BLOCKS.register("copper_block", () -> new Block(
            AbstractBlock.Properties.create(Material.IRON).setRequiresTool().harvestLevel(1)
                    .hardnessAndResistance(3.0F, 3.0F)));
    public static final RegistryObject<Block> STEEL_BLOCK = BLOCKS.register("steel_block", () -> new Block(
            AbstractBlock.Properties.create(Material.IRON).setRequiresTool().harvestLevel(2)
                    .hardnessAndResistance(4.0F, 4.0F)));
    public static final RegistryObject<Block> TITANIUM_BLOCK = BLOCKS.register("titanium_block", () -> new Block(
            AbstractBlock.Properties.create(Material.IRON).setRequiresTool().harvestLevel(3)
                    .hardnessAndResistance(6.0F, 6.0F)));
    public static final RegistryObject<Block> QUARTZ_SAND = BLOCKS.register("quartz_sand", () -> new Block(
            AbstractBlock.Properties.create(Material.SAND).harvestTool(ToolType.SHOVEL)
                    .hardnessAndResistance(1.0F, 1.0F)));
    public static final RegistryObject<Block> CONTROLLER = BLOCKS.register("controller",
            () -> new Block(AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(3.0F, 6.0F)));

    public static final RegistryObject<Item> ALUMINIUM_INGOT = ITEMS
            .register("aluminium_ingot", () -> new Item(new Item.Properties().group(TAB)));
    public static final RegistryObject<Item> COPPER_INGOT = ITEMS
            .register("copper_ingot", () -> new Item(new Item.Properties().group(TAB)));
    public static final RegistryObject<Item> STEEL_INGOT = ITEMS
            .register("steel_ingot", () -> new Item(new Item.Properties().group(TAB)));
    public static final RegistryObject<Item> TITANIUM_INGOT = ITEMS
            .register("titanium_ingot", () -> new Item(new Item.Properties().group(TAB)));
    public static final RegistryObject<Item> ALUMINIUM_NUGGET = ITEMS
            .register("aluminium_nugget", () -> new Item(new Item.Properties().group(TAB)));
    public static final RegistryObject<Item> COPPER_NUGGET = ITEMS
            .register("copper_nugget", () -> new Item(new Item.Properties().group(TAB)));
    public static final RegistryObject<Item> STEEL_NUGGET = ITEMS
            .register("steel_nugget", () -> new Item(new Item.Properties().group(TAB)));
    public static final RegistryObject<Item> TITANIUM_NUGGET = ITEMS
            .register("titanium_nugget", () -> new Item(new Item.Properties().group(TAB)));
    public static final RegistryObject<Item> ALUMINIUM_PLATE = ITEMS
            .register("aluminium_plate", () -> new Item(new Item.Properties().group(TAB)));
    public static final RegistryObject<Item> COPPER_PLATE = ITEMS
            .register("copper_plate", () -> new Item(new Item.Properties().group(TAB)));
    public static final RegistryObject<Item> IRON_PLATE = ITEMS
            .register("iron_plate", () -> new Item(new Item.Properties().group(TAB)));
    public static final RegistryObject<Item> STEEL_PLATE = ITEMS
            .register("steel_plate", () -> new Item(new Item.Properties().group(TAB)));
    public static final RegistryObject<Item> BATTERY = ITEMS
            .register("battery", () -> new Item(new Item.Properties().group(TAB)));
    public static final RegistryObject<Item> CYLINDER = ITEMS
            .register("cylinder", () -> new Item(new Item.Properties().group(TAB)));
    public static final RegistryObject<Item> IGNITION_COIL = ITEMS
            .register("ignition_coil", () -> new Item(new Item.Properties().group(TAB)));
    public static final RegistryObject<Item> INDUSTRIAL_PISTON = ITEMS
            .register("industrial_piston", () -> new Item(new Item.Properties().group(TAB)));
    public static final RegistryObject<Item> COPPER_TUBE = ITEMS
            .register("copper_tube", () -> new Item(new Item.Properties().group(TAB)));
    public static final RegistryObject<Item> IRON_TUBE = ITEMS
            .register("iron_tube", () -> new Item(new Item.Properties().group(TAB)));
    public static final RegistryObject<Item> IRON_DUST = ITEMS
            .register("iron_dust", () -> new Item(new Item.Properties().group(TAB)));
    public static final RegistryObject<Item> QUARTZ_DUST = ITEMS
            .register("quartz_dust", () -> new Item(new Item.Properties().group(TAB)));
    public static final RegistryObject<Item> STEEL_ROD = ITEMS
            .register("steel_rod", () -> new Item(new Item.Properties().group(TAB)));
    public static final RegistryObject<Item> HEAT_RESISTENT_GLASS = ITEMS
            .register("heat_resistent_glass", () -> new Item(new Item.Properties().group(TAB)));
    public static final RegistryObject<Item> MACHINE_BASE = ITEMS
            .register("machine_base", () -> new Item(new Item.Properties().group(TAB)));
    public static final RegistryObject<Item> SILICA_TILE = ITEMS
            .register("silica_tile", () -> new Item(new Item.Properties().group(TAB)));
    public static final RegistryObject<Item> PLATED_ROCKET_BODY = ITEMS
            .register("plated_rocket_body", () -> new Item(new Item.Properties().group(TAB)));
    public static final RegistryObject<Item> ROCKET_CONE = ITEMS
            .register("rocket_cone", () -> new Item(new Item.Properties().group(TAB)));
    public static final RegistryObject<Item> ROCKET_FIN = ITEMS
            .register("rocket_fin", () -> new Item(new Item.Properties().group(TAB)));
    public static final RegistryObject<Item> ROCKET_THRUSTER = ITEMS
            .register("rocket_thruster", () -> new Item(new Item.Properties().group(TAB)));
    public static final RegistryObject<Item> HEATING_COIL = ITEMS
            .register("heating_coil", () -> new Item(new Item.Properties().group(TAB)));
    public static final RegistryObject<Item> ISOLATING_FABRIC = ITEMS
            .register("isolating_fabric", () -> new Item(new Item.Properties().group(TAB)));
    public static final RegistryObject<Item> EMPTY_CAN = ITEMS
            .register("empty_can", () -> new Item(new Item.Properties().group(TAB)));
    public static final RegistryObject<Item> ASTRONAUT_FOOD = ITEMS.register("astronaut_food", () -> new Item(
            new Item.Properties().group(TAB).food(new Food.Builder().hunger(8).saturation(0.2F).build())));

    static {
        ITEMS.register("copper_ore", () -> new BlockItem(COPPER_ORE.get(), new Item.Properties().group(TAB)));
        ITEMS.register("aluminium_ore", () -> new BlockItem(ALUMINIUM_ORE.get(), new Item.Properties().group(TAB)));
        ITEMS.register("ilmenite_ore", () -> new BlockItem(ILMENITE_ORE.get(), new Item.Properties().group(TAB)));
        ITEMS.register("aluminium_block", () -> new BlockItem(ALUMINIUM_BLOCK.get(), new Item.Properties().group(TAB)));
        ITEMS.register("copper_block", () -> new BlockItem(COPPER_BLOCK.get(), new Item.Properties().group(TAB)));
        ITEMS.register("steel_block", () -> new BlockItem(STEEL_BLOCK.get(), new Item.Properties().group(TAB)));
        ITEMS.register("titanium_block", () -> new BlockItem(TITANIUM_BLOCK.get(), new Item.Properties().group(TAB)));
        ITEMS.register("quartz_sand", () -> new BlockItem(QUARTZ_SAND.get(), new Item.Properties().group(TAB)));
        ITEMS.register("controller", () -> new BlockItem(CONTROLLER.get(), new Item.Properties().group(TAB)));
    }

    public static void register() {
    }

}
