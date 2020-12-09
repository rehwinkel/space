package deerangle.space.planet.planets.venus;

import deerangle.space.data.LanguageGenerator;
import deerangle.space.main.SpaceMod;
import deerangle.space.planet.PlanetManager;
import deerangle.space.planet.planets.venus.block.*;
import deerangle.space.planet.planets.venus.data.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class VenusRegistry {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SpaceMod.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SpaceMod.MOD_ID);
    private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, SpaceMod.MOD_ID);

    // FIXME
    public static final RegistryObject<SoundEvent> MUSIC_LOVE = SOUND_EVENTS.register("music_disc_love", () -> new SoundEvent(new ResourceLocation(SpaceMod.MOD_ID, "music_disc.love")));
    public static final RegistryObject<SoundEvent> MUSIC_SPICY_AND_SOUR = SOUND_EVENTS.register("music_disc_spicy_and_sour", () -> new SoundEvent(new ResourceLocation(SpaceMod.MOD_ID, "music_disc.spicy_and_sour")));

    public static final RegistryObject<Block> PULCHERITE = BLOCKS.register("pulcherite", () -> new Block(AbstractBlock.Properties.create(Material.ROCK).setRequiresTool().hardnessAndResistance(1.5F, 6.0F)));
    public static final RegistryObject<Block> PULCHERITE_TURF = BLOCKS.register("pulcherite_turf", () -> new Block(AbstractBlock.Properties.create(Material.ROCK).sound(SoundType.GROUND).hardnessAndResistance(1.0f, 2.0f)));
    public static final RegistryObject<Block> PULCHERITE_COAL = BLOCKS.register("pulcherite_coal", () -> new OreBlock(AbstractBlock.Properties.create(Material.ROCK).setRequiresTool().hardnessAndResistance(3.0F, 3.0F)));
    public static final RegistryObject<Block> PULCHERITE_SULFUR = BLOCKS.register("pulcherite_sulfur", () -> new OreBlock(AbstractBlock.Properties.create(Material.ROCK).setRequiresTool().hardnessAndResistance(3.0F, 3.0F)));
    public static final RegistryObject<Block> PULCHERITE_ILMENITE = BLOCKS.register("pulcherite_ilmenite", () -> new OreBlock(AbstractBlock.Properties.create(Material.ROCK).setRequiresTool().hardnessAndResistance(3.0F, 3.0F).harvestLevel(2)));
    public static final RegistryObject<Block> OVERGROWN_PULCHERITE = BLOCKS.register("overgrown_pulcherite", () -> new OvergrownBlock(AbstractBlock.Properties.from(VenusRegistry.PULCHERITE.get()).hardnessAndResistance(2.0F, 6.0F)));
    public static final RegistryObject<Block> SHRIEKGRASS = BLOCKS.register("shriekgrass", () -> new ShriekGrassBlock(AbstractBlock.Properties.create(Material.PLANTS).doesNotBlockMovement().zeroHardnessAndResistance().notSolid().sound(SoundType.PLANT)));
    public static final RegistryObject<Block> PULCHERITE_BRICKS = BLOCKS.register("pulcherite_bricks", () -> new Block(AbstractBlock.Properties.from(VenusRegistry.PULCHERITE.get())));
    public static final RegistryObject<Block> POLISHED_PULCHERITE = BLOCKS.register("polished_pulcherite", () -> new Block(AbstractBlock.Properties.from(VenusRegistry.PULCHERITE.get())));
    public static final RegistryObject<Block> TURPIUM = BLOCKS.register("turpium", () -> new Block(AbstractBlock.Properties.create(Material.ROCK).setRequiresTool().hardnessAndResistance(1.5F, 6.0F)));
    public static final RegistryObject<Block> GLOWING_TURPIUM = BLOCKS.register("glowing_turpium", () -> new GlowingBlock(AbstractBlock.Properties.from(VenusRegistry.TURPIUM.get()).setLightLevel(value -> 8).tickRandomly()));
    public static final RegistryObject<Block> TURPIUM_COBBLESTONE = BLOCKS.register("turpium_cobblestone", () -> new Block(AbstractBlock.Properties.from(VenusRegistry.TURPIUM.get())));
    public static final RegistryObject<Block> TURPIUM_ROCK = BLOCKS.register("turpium_rock", () -> new RockBlock(AbstractBlock.Properties.from(VenusRegistry.TURPIUM.get()).hardnessAndResistance(0.3F, 3.0F)));
    public static final RegistryObject<Block> VENUS_BACTERIA = BLOCKS.register("venus_bacteria", () -> new VineBlock(AbstractBlock.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0.2F).sound(SoundType.SLIME)));
    public static final RegistryObject<Block> SHRIEKWOOD_LOG = BLOCKS.register("shriekwood_log", () -> new RotatedPillarBlock(AbstractBlock.Properties.create(Material.WOOD).hardnessAndResistance(2.0F)));
    public static final RegistryObject<Block> SHRIEKWOOD_WOOD = BLOCKS.register("shriekwood_wood", () -> new RotatedPillarBlock(AbstractBlock.Properties.create(Material.WOOD).hardnessAndResistance(2.0F)));
    public static final RegistryObject<Block> SHRIEKWOOD_LEAVES = BLOCKS.register("shriekwood_leaves", () -> new LeavesBlock(AbstractBlock.Properties.create(Material.LEAVES).notSolid().hardnessAndResistance(0.2F).setBlocksVision((state, reader, pos) -> false).tickRandomly().sound(SoundType.PLANT)));
    public static final RegistryObject<Block> SHRIEKWOOD_PLANKS = BLOCKS.register("shriekwood_planks", () -> new Block(AbstractBlock.Properties.create(Material.WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> SHRIEKWOOD_SAPLING = BLOCKS.register("shriekwood_sapling", () -> new ShriekwoodSaplingBlock(AbstractBlock.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().zeroHardnessAndResistance().sound(SoundType.PLANT)));
    public static final RegistryObject<Block> SHRIEKWOOD_STAIRS = BLOCKS.register("shriekwood_stairs", () -> new StairsBlock(() -> SHRIEKWOOD_PLANKS.get().getDefaultState(), AbstractBlock.Properties.from(SHRIEKWOOD_PLANKS.get())));
    public static final RegistryObject<Block> SHRIEKWOOD_SLAB = BLOCKS.register("shriekwood_slab", () -> new SlabBlock(AbstractBlock.Properties.from(SHRIEKWOOD_PLANKS.get())));
    public static final RegistryObject<Block> SHRIEKWOOD_DOOR = BLOCKS.register("shriekwood_door", () -> new DoorBlock(AbstractBlock.Properties.from(SHRIEKWOOD_PLANKS.get())));
    public static final RegistryObject<Block> SLIMY_ALGAE = BLOCKS.register("slimy_algae", () -> new AlgaeBlock(AbstractBlock.Properties.create(Material.WOOD).setBlocksVision((state, reader, pos) -> false).tickRandomly().notSolid().sound(SoundType.SLIME).hardnessAndResistance(0.1F).speedFactor(0.5F)));
    public static final RegistryObject<Block> CRYSTAL = BLOCKS.register("crystal", () -> new CrystalBlock(AbstractBlock.Properties.create(Material.GLASS).notSolid().doesNotBlockMovement()));
    public static final RegistryObject<Block> CRYSTAL_BLOCK = BLOCKS.register("crystal_block", () -> new CrystalCubeBlock(AbstractBlock.Properties.create(Material.GLASS)));
    public static final RegistryObject<Item> MUSIC_DISC_SPICY_AND_SOUR = ITEMS.register("music_disc_spicy_and_sour", () -> new MusicDiscItem(1, MUSIC_SPICY_AND_SOUR, new Item.Properties().group(PlanetManager.TAB).rarity(Rarity.RARE).maxStackSize(1)));
    public static final RegistryObject<Item> MUSIC_DISC_LOVE = ITEMS.register("music_disc_love", () -> new MusicDiscItem(2, MUSIC_LOVE, new Item.Properties().group(PlanetManager.TAB).rarity(Rarity.RARE).maxStackSize(1)));
    public static final RegistryObject<Item> SULFUR = ITEMS.register("sulfur", () -> new Item(new Item.Properties().group(PlanetManager.TAB)));
    public static final RegistryObject<Item> SHRIEKWOOD_DOOR_ITEM = ITEMS.register("shriekwood_door", () -> new BlockItem(SHRIEKWOOD_DOOR.get(), new Item.Properties().group(PlanetManager.TAB)));

    //TODO: add mobs and their drops

    static {
        ITEMS.register("pulcherite", () -> new BlockItem(PULCHERITE.get(), new Item.Properties().group(PlanetManager.TAB)));
        ITEMS.register("pulcherite_turf", () -> new BlockItem(PULCHERITE_TURF.get(), new Item.Properties().group(PlanetManager.TAB)));
        ITEMS.register("pulcherite_coal", () -> new BlockItem(PULCHERITE_COAL.get(), new Item.Properties().group(PlanetManager.TAB)));
        ITEMS.register("pulcherite_sulfur", () -> new BlockItem(PULCHERITE_SULFUR.get(), new Item.Properties().group(PlanetManager.TAB)));
        ITEMS.register("pulcherite_ilmenite", () -> new BlockItem(PULCHERITE_ILMENITE.get(), new Item.Properties().group(PlanetManager.TAB)));
        ITEMS.register("overgrown_pulcherite", () -> new BlockItem(OVERGROWN_PULCHERITE.get(), new Item.Properties().group(PlanetManager.TAB)));
        ITEMS.register("shriekgrass", () -> new BlockItem(SHRIEKGRASS.get(), new Item.Properties().group(PlanetManager.TAB)));
        ITEMS.register("pulcherite_bricks", () -> new BlockItem(PULCHERITE_BRICKS.get(), new Item.Properties().group(PlanetManager.TAB)));
        ITEMS.register("polished_pulcherite", () -> new BlockItem(POLISHED_PULCHERITE.get(), new Item.Properties().group(PlanetManager.TAB)));
        ITEMS.register("turpium", () -> new BlockItem(TURPIUM.get(), new Item.Properties().group(PlanetManager.TAB)));
        ITEMS.register("glowing_turpium", () -> new BlockItem(GLOWING_TURPIUM.get(), new Item.Properties().group(PlanetManager.TAB)));
        ITEMS.register("turpium_cobblestone", () -> new BlockItem(TURPIUM_COBBLESTONE.get(), new Item.Properties().group(PlanetManager.TAB)));
        ITEMS.register("turpium_rock", () -> new BlockItem(TURPIUM_ROCK.get(), new Item.Properties().group(PlanetManager.TAB)));
        ITEMS.register("venus_bacteria", () -> new BlockItem(VENUS_BACTERIA.get(), new Item.Properties().group(PlanetManager.TAB)));
        ITEMS.register("shriekwood_log", () -> new BlockItem(SHRIEKWOOD_LOG.get(), new Item.Properties().group(PlanetManager.TAB)));
        ITEMS.register("shriekwood_wood", () -> new BlockItem(SHRIEKWOOD_WOOD.get(), new Item.Properties().group(PlanetManager.TAB)));
        ITEMS.register("shriekwood_leaves", () -> new BlockItem(SHRIEKWOOD_LEAVES.get(), new Item.Properties().group(PlanetManager.TAB)));
        ITEMS.register("shriekwood_planks", () -> new BlockItem(SHRIEKWOOD_PLANKS.get(), new Item.Properties().group(PlanetManager.TAB)));
        ITEMS.register("shriekwood_stairs", () -> new BlockItem(SHRIEKWOOD_STAIRS.get(), new Item.Properties().group(PlanetManager.TAB)));
        ITEMS.register("shriekwood_slab", () -> new BlockItem(SHRIEKWOOD_SLAB.get(), new Item.Properties().group(PlanetManager.TAB)));
        ITEMS.register("slimy_algae", () -> new BlockItem(SLIMY_ALGAE.get(), new Item.Properties().group(PlanetManager.TAB).food(new Food.Builder().fastToEat().hunger(1).saturation(0.05F).build())));
        ITEMS.register("shriekwood_sapling", () -> new BlockItem(SHRIEKWOOD_SAPLING.get(), new Item.Properties().group(PlanetManager.TAB)));
        ITEMS.register("crystal_block", () -> new BlockItem(CRYSTAL_BLOCK.get(), new Item.Properties().group(PlanetManager.TAB)));
        ITEMS.register("crystal", () -> new BlockItem(CRYSTAL.get(), new Item.Properties().group(PlanetManager.TAB)));
    }

    public static void register() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        SOUND_EVENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static void registerData(GatherDataEvent event) {
        event.getGenerator().addProvider(new BlockStateGenerator(event.getGenerator(), SpaceMod.MOD_ID, event.getExistingFileHelper()));
        event.getGenerator().addProvider(new ItemModelGenerator(event.getGenerator(), SpaceMod.MOD_ID, event.getExistingFileHelper()));
        event.getGenerator().addProvider(new BlockTagsGenerator(event.getGenerator(), SpaceMod.MOD_ID, event.getExistingFileHelper()));
        event.getGenerator().addProvider(new LootTableGenerator(event.getGenerator(), SpaceMod.MOD_ID));
        event.getGenerator().addProvider(new RecipeGenerator(event.getGenerator()));
    }

    public static void registerClient() {
        RenderTypeLookup.setRenderLayer(SLIMY_ALGAE.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(SHRIEKWOOD_DOOR.get(), RenderType.getCutoutMipped());
        RenderTypeLookup.setRenderLayer(SHRIEKGRASS.get(), RenderType.getCutoutMipped());
        RenderTypeLookup.setRenderLayer(SHRIEKWOOD_LEAVES.get(), RenderType.getCutoutMipped());
        RenderTypeLookup.setRenderLayer(VENUS_BACTERIA.get(), RenderType.getCutoutMipped());
        RenderTypeLookup.setRenderLayer(SHRIEKWOOD_SAPLING.get(), RenderType.getCutoutMipped());
        RenderTypeLookup.setRenderLayer(CRYSTAL.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(CRYSTAL_BLOCK.get(), RenderType.getTranslucent());
    }

    public static void registerLanguage(LanguageGenerator gen) {
        gen.add(PULCHERITE.get(), "Pulcherite");
        gen.add(PULCHERITE_TURF.get(), "Pulcherite Turf");
        gen.add(PULCHERITE_COAL.get(), "Pulcherite Coal Ore");
        gen.add(PULCHERITE_SULFUR.get(), "Pulcherite Sulfur Ore");
        gen.add(PULCHERITE_ILMENITE.get(), "Pulcherite Ilmenite Ore");
        gen.add(OVERGROWN_PULCHERITE.get(), "Overgrown Pulcherite");
        gen.add(PULCHERITE_BRICKS.get(), "Pulcherite Bricks");
        gen.add(POLISHED_PULCHERITE.get(), "Polished Pulcherite");
        gen.add(TURPIUM.get(), "Turpium");
        gen.add(GLOWING_TURPIUM.get(), "Glowing Turpium");
        gen.add(TURPIUM_COBBLESTONE.get(), "Turpium Cobblestone");
        gen.add(TURPIUM_ROCK.get(), "Turpium Rock");
        gen.add(VENUS_BACTERIA.get(), "Bacteria Colony");
        gen.add(SHRIEKGRASS.get(), "Shriekgrass");
        gen.add(SHRIEKWOOD_LOG.get(), "Shriekwood Log");
        gen.add(SHRIEKWOOD_WOOD.get(), "Shriekwood");
        gen.add(SHRIEKWOOD_LEAVES.get(), "Shriekwood Leaves");
        gen.add(SHRIEKWOOD_SAPLING.get(), "Shriekwood Sapling");
        gen.add(SHRIEKWOOD_PLANKS.get(), "Shriekwood Planks");
        gen.add(SHRIEKWOOD_STAIRS.get(), "Shriekwood Stairs");
        gen.add(SHRIEKWOOD_SLAB.get(), "Shriekwood Slab");
        gen.add(SHRIEKWOOD_DOOR.get(), "Shriekwood Door");
        gen.add(SLIMY_ALGAE.get(), "Slimy Algae");
        gen.add(MUSIC_DISC_SPICY_AND_SOUR.get(), "Music Disc");
        gen.add(MUSIC_DISC_LOVE.get(), "Music Disc");
        gen.add(MUSIC_DISC_SPICY_AND_SOUR.get().getTranslationKey() + ".desc", "Ian Rehwinkel - Spicy and Sour");
        gen.add(MUSIC_DISC_LOVE.get().getTranslationKey() + ".desc", "Ian Rehwinkel - Love");
        gen.add(SULFUR.get(), "Sulfur");
        gen.add(CRYSTAL.get(), "Crystal");
        gen.add(CRYSTAL_BLOCK.get(), "Crystal Block");
    }

}
