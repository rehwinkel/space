package deerangle.space.planets.venus.world;

import deerangle.space.planets.feature.BoulderFeatureConfig;
import deerangle.space.planets.venus.VenusRegistry;
import deerangle.space.planets.venus.tags.BlockTags;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.template.TagMatchRuleTest;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class VenusBiomeMaker {

    public static Biome makeHillsBiome() {
        float depth = 0.6f;
        float scale = 0.1f;
        MobSpawnInfo.Builder mobSpawnSettings = new MobSpawnInfo.Builder();
        BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder().withSurfaceBuilder(
                SurfaceBuilder.DEFAULT.func_242929_a(
                        new SurfaceBuilderConfig(VenusRegistry.PULCHERITE_TURF.get().getDefaultState(),
                                VenusRegistry.PULCHERITE.get().getDefaultState(),
                                VenusRegistry.PULCHERITE.get().getDefaultState())));
        builder.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(
                new OreFeatureConfig(new TagMatchRuleTest(BlockTags.VENUS_GROUND),
                        VenusRegistry.PULCHERITE_COAL.get().getDefaultState(), 17)).range(128).square()
                .func_242731_b(20));

        builder.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(
                new OreFeatureConfig(new TagMatchRuleTest(BlockTags.VENUS_GROUND),
                        VenusRegistry.PULCHERITE_SULFUR.get().getDefaultState(), 9)).range(64).square()
                .func_242731_b(20));

        builder.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(
                new OreFeatureConfig(new TagMatchRuleTest(BlockTags.VENUS_GROUND),
                        VenusRegistry.PULCHERITE_ILMENITE.get().getDefaultState(), 4)).range(40).square()
                .func_242731_b(4));

        builder.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(
                new OreFeatureConfig(new TagMatchRuleTest(BlockTags.VENUS_GROUND),
                        VenusRegistry.TURPIUM.get().getDefaultState(), 33)).range(80).square().func_242731_b(10));
        builder.withFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS,
                deerangle.space.planets.feature.Features.BOULDER.get().withConfiguration(new BoulderFeatureConfig(
                        new SimpleBlockStateProvider(VenusRegistry.TURPIUM.get().getDefaultState()),
                        FeatureSpread.func_242253_a(2, 1))).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT)
                        .chance(2));
        builder.withFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS,
                deerangle.space.planets.feature.Features.BOULDER.get().withConfiguration(new BoulderFeatureConfig(
                        new WeightedBlockStateProvider()
                                .addWeightedBlockstate(VenusRegistry.TURPIUM.get().getDefaultState(), 2)
                                .addWeightedBlockstate(VenusRegistry.GLOWING_TURPIUM.get().getDefaultState(), 1),
                        FeatureSpread.func_242253_a(2, 1))).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT)
                        .chance(5));
        builder.withFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Feature.FLOWER.withConfiguration(
                (new BlockClusterFeatureConfig.Builder(
                        new SimpleBlockStateProvider(VenusRegistry.TURPIUM_ROCK.get().getDefaultState()),
                        SimpleBlockPlacer.PLACER)).tries(40).build())
                .withPlacement(Features.Placements.VEGETATION_PLACEMENT)
                .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).func_242731_b(4));
        // DefaultBiomeFeatures.withMooshroomsAndBats(mobSpawnSettings);
        /*
        DefaultBiomeFeatures.withStrongholdAndMineshaft(generationSettings);
        generationSettings.withStructure(StructureFeatures.RUINED_PORTAL);
        DefaultBiomeFeatures.withCavesAndCanyons(generationSettings);
        DefaultBiomeFeatures.withLavaAndWaterLakes(generationSettings);
        DefaultBiomeFeatures.withMonsterRoom(generationSettings);
        DefaultBiomeFeatures.withCommonOverworldBlocks(generationSettings);
        DefaultBiomeFeatures.withOverworldOres(generationSettings);
        DefaultBiomeFeatures.withDisks(generationSettings);
        DefaultBiomeFeatures.withMushroomBiomeVegetation(generationSettings);
        DefaultBiomeFeatures.withNormalMushroomGeneration(generationSettings);
        DefaultBiomeFeatures.withSugarCaneAndPumpkins(generationSettings);
        DefaultBiomeFeatures.withLavaAndWaterSprings(generationSettings);
        */
        return (new Biome.Builder()).precipitation(Biome.RainType.NONE).category(Biome.Category.NONE).depth(depth)
                .scale(scale).temperature(0.9F).downfall(2.0F).setEffects(
                        (new BiomeAmbience.Builder()).setWaterColor(0x577545).setWaterFogColor(0x4aad36)
                                .setFogColor(0xd6c156).withSkyColor(0xc2c27e)
                                .setMoodSound(MoodSoundAmbience.DEFAULT_CAVE).build())
                .withMobSpawnSettings(mobSpawnSettings.copy()).withGenerationSettings(builder.build()).build();
    }

}
