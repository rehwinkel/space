package deerangle.space.planets.venus;

import deerangle.space.main.SpaceMod;
import deerangle.space.planets.PlanetRegistry;
import deerangle.space.planets.venus.block.AlgeeBlock;
import deerangle.space.planets.venus.block.GlowingBlock;
import deerangle.space.planets.venus.block.OvergrownBlock;
import deerangle.space.planets.venus.block.RockBlock;
import deerangle.space.planets.venus.data.BlockStateGenerator;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.MusicDiscItem;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class VenusRegistry {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister
            .create(ForgeRegistries.BLOCKS, SpaceMod.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SpaceMod.MOD_ID);

    public static final RegistryObject<Block> PULCHERITE = BLOCKS
            .register("pulcherite", () -> new Block(AbstractBlock.Properties.create(Material.ROCK)));
    public static final RegistryObject<Block> PULCHERITE_COAL = BLOCKS
            .register("pulcherite_coal", () -> new OreBlock(AbstractBlock.Properties.create(Material.ROCK)));
    public static final RegistryObject<Block> PULCHERITE_SULFUR = BLOCKS
            .register("pulcherite_sulfur", () -> new OreBlock(AbstractBlock.Properties.create(Material.ROCK)));
    public static final RegistryObject<Block> PULCHERITE_ILMENITE = BLOCKS
            .register("pulcherite_ilmenite", () -> new OreBlock(AbstractBlock.Properties.create(Material.ROCK)));
    public static final RegistryObject<Block> OVERGROWN_PULCHERITE = BLOCKS
            .register("overgrown_pulcherite", () -> new OvergrownBlock(AbstractBlock.Properties.create(Material.ROCK)));
    public static final RegistryObject<Block> SHRIEKGRASS = BLOCKS
            .register("shriekgrass", () -> new TallGrassBlock(AbstractBlock.Properties.create(Material.ROCK)));
    public static final RegistryObject<Block> PULCHERITE_BRICKS = BLOCKS
            .register("pulcherite_bricks", () -> new Block(AbstractBlock.Properties.create(Material.ROCK)));
    public static final RegistryObject<Block> POLISHED_PULCHERITE = BLOCKS
            .register("polished_pulcherite", () -> new Block(AbstractBlock.Properties.create(Material.ROCK)));
    public static final RegistryObject<Block> TURPIUM = BLOCKS
            .register("turpium", () -> new Block(AbstractBlock.Properties.create(Material.ROCK)));
    public static final RegistryObject<Block> GLOWING_TURPIUM = BLOCKS.register("glowing_turpium",
            () -> new GlowingBlock(AbstractBlock.Properties.create(Material.ROCK).setLightLevel(state -> 7)));
    public static final RegistryObject<Block> TURPIUM_COBBLESTONE = BLOCKS
            .register("turpium_cobblestone", () -> new Block(AbstractBlock.Properties.create(Material.ROCK)));
    public static final RegistryObject<Block> TURPIUM_ROCK = BLOCKS
            .register("turpium_rock", () -> new RockBlock(AbstractBlock.Properties.create(Material.ROCK)));
    public static final RegistryObject<Block> VENUS_BACTERIA = BLOCKS
            .register("venus_bacteria", () -> new VineBlock(AbstractBlock.Properties.create(Material.PLANTS)));
    public static final RegistryObject<Block> SHRIEKWOOD_LOG = BLOCKS
            .register("shriekwood_log", () -> new RotatedPillarBlock(AbstractBlock.Properties.create(Material.WOOD)));
    public static final RegistryObject<Block> SHRIEKWOOD_WOOD = BLOCKS
            .register("shriekwood_wood", () -> new RotatedPillarBlock(AbstractBlock.Properties.create(Material.WOOD)));
    public static final RegistryObject<Block> SHRIEKWOOD_LEAVES = BLOCKS.register("shriekwood_leaves",
            () -> new RotatedPillarBlock(AbstractBlock.Properties.create(Material.LEAVES)));
    public static final RegistryObject<Block> SHRIEKWOOD_PLANKS = BLOCKS
            .register("shriekwood_planks", () -> new Block(AbstractBlock.Properties.create(Material.WOOD)));
    public static final RegistryObject<Block> SHRIEKWOOD_STAIRS = BLOCKS.register("shriekwood_stairs",
            () -> new StairsBlock(() -> SHRIEKWOOD_PLANKS.get().getDefaultState(),
                    AbstractBlock.Properties.create(Material.WOOD)));
    public static final RegistryObject<Block> SHRIEKWOOD_SLAB = BLOCKS
            .register("shriekwood_slab", () -> new SlabBlock(AbstractBlock.Properties.create(Material.WOOD)));
    public static final RegistryObject<Block> SHRIEKWOOD_DOOR = BLOCKS
            .register("shriekwood_door", () -> new DoorBlock(AbstractBlock.Properties.create(Material.WOOD)));
    public static final RegistryObject<Block> SLIMY_ALGAE = BLOCKS
            .register("slimy_algae", () -> new AlgeeBlock(AbstractBlock.Properties.create(Material.WOOD)));

    public static final RegistryObject<Item> MUSIC_DISC_SPICY_AND_SOUR = ITEMS.register("music_disc_spicy_and_sour",
            () -> new MusicDiscItem(1, () -> null, new Item.Properties().group(PlanetRegistry.TAB).maxStackSize(1)));

    public static final RegistryObject<Item> MUSIC_DISC_LOVE = ITEMS.register("music_disc_love",
            () -> new MusicDiscItem(2, () -> null, new Item.Properties().group(PlanetRegistry.TAB).maxStackSize(1)));

    public static final RegistryObject<Item> SULFUR = ITEMS
            .register("sulfur", () -> new Item(new Item.Properties().group(PlanetRegistry.TAB)));

    public static final RegistryObject<Item> SHRIEKWOOD_DOOR_ITEM = ITEMS.register("shriekwood_door",
            () -> new BlockItem(SHRIEKWOOD_DOOR.get(), new Item.Properties().group(PlanetRegistry.TAB)));

    public static final RegistryObject<Item> SLIMY_ALGAE_ITEM = ITEMS.register("slimy_algae", () -> new Item(
            new Item.Properties().group(PlanetRegistry.TAB)
                    .food(new Food.Builder().fastToEat().hunger(2).saturation(0.2F).build())));

    //TODO: add mobs and their drops

    static {
        ITEMS.register("pulcherite",
                () -> new BlockItem(PULCHERITE.get(), new Item.Properties().group(PlanetRegistry.TAB)));
        ITEMS.register("pulcherite_coal",
                () -> new BlockItem(PULCHERITE_COAL.get(), new Item.Properties().group(PlanetRegistry.TAB)));
        ITEMS.register("pulcherite_sulfur",
                () -> new BlockItem(PULCHERITE_SULFUR.get(), new Item.Properties().group(PlanetRegistry.TAB)));
        ITEMS.register("pulcherite_ilmenite",
                () -> new BlockItem(PULCHERITE_ILMENITE.get(), new Item.Properties().group(PlanetRegistry.TAB)));
        ITEMS.register("overgrown_pulcherite",
                () -> new BlockItem(OVERGROWN_PULCHERITE.get(), new Item.Properties().group(PlanetRegistry.TAB)));
        ITEMS.register("shriekgrass",
                () -> new BlockItem(SHRIEKGRASS.get(), new Item.Properties().group(PlanetRegistry.TAB)));
        ITEMS.register("pulcherite_bricks",
                () -> new BlockItem(PULCHERITE_BRICKS.get(), new Item.Properties().group(PlanetRegistry.TAB)));
        ITEMS.register("polished_pulcherite_bricks",
                () -> new BlockItem(POLISHED_PULCHERITE.get(), new Item.Properties().group(PlanetRegistry.TAB)));
        ITEMS.register("turpium", () -> new BlockItem(TURPIUM.get(), new Item.Properties().group(PlanetRegistry.TAB)));
        ITEMS.register("glowing_turpium",
                () -> new BlockItem(GLOWING_TURPIUM.get(), new Item.Properties().group(PlanetRegistry.TAB)));
        ITEMS.register("turpium_cobblestone",
                () -> new BlockItem(TURPIUM_COBBLESTONE.get(), new Item.Properties().group(PlanetRegistry.TAB)));
        ITEMS.register("turpium_rock",
                () -> new BlockItem(TURPIUM_ROCK.get(), new Item.Properties().group(PlanetRegistry.TAB)));
        ITEMS.register("venus_bacteria",
                () -> new BlockItem(VENUS_BACTERIA.get(), new Item.Properties().group(PlanetRegistry.TAB)));
        ITEMS.register("shriekwood_log",
                () -> new BlockItem(SHRIEKWOOD_LOG.get(), new Item.Properties().group(PlanetRegistry.TAB)));
        ITEMS.register("shriekwood_wood",
                () -> new BlockItem(SHRIEKWOOD_WOOD.get(), new Item.Properties().group(PlanetRegistry.TAB)));
        ITEMS.register("shriekwood_leaves",
                () -> new BlockItem(SHRIEKWOOD_LEAVES.get(), new Item.Properties().group(PlanetRegistry.TAB)));
        ITEMS.register("shriekwood_planks",
                () -> new BlockItem(SHRIEKWOOD_PLANKS.get(), new Item.Properties().group(PlanetRegistry.TAB)));
        ITEMS.register("shriekwood_stairs",
                () -> new BlockItem(SHRIEKWOOD_STAIRS.get(), new Item.Properties().group(PlanetRegistry.TAB)));
        ITEMS.register("shriekwood_slab",
                () -> new BlockItem(SHRIEKWOOD_SLAB.get(), new Item.Properties().group(PlanetRegistry.TAB)));
    }

    public static void register() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static void registerData(GatherDataEvent event) {
        event.getGenerator().addProvider(
                new BlockStateGenerator(event.getGenerator(), SpaceMod.MOD_ID, event.getExistingFileHelper()));
    }

}
