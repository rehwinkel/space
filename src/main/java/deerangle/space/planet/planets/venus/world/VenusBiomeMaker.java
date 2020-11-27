package deerangle.space.planet.planets.venus.world;

import com.google.common.collect.ImmutableList;
import deerangle.space.planet.feature.AlgaePathFeatureConfig;
import deerangle.space.planet.feature.BetterLakeFeatureConfig;
import deerangle.space.planet.feature.BoulderFeatureConfig;
import deerangle.space.planet.feature.CraterFeatureConfig;
import deerangle.space.planet.planets.venus.VenusRegistry;
import deerangle.space.planet.planets.venus.tags.BlockTags;
import deerangle.space.registry.FluidRegistry;
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
        MobSpawnInfo.Builder spawnSettings = new MobSpawnInfo.Builder();
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
                deerangle.space.planet.feature.Features.BOULDER.get().withConfiguration(new BoulderFeatureConfig(
                        new SimpleBlockStateProvider(VenusRegistry.TURPIUM.get().getDefaultState()),
                        FeatureSpread.func_242253_a(2, 1))).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT)
                        .chance(2));
        builder.withFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS,
                deerangle.space.planet.feature.Features.BOULDER.get().withConfiguration(new BoulderFeatureConfig(
                        new WeightedBlockStateProvider()
                                .addWeightedBlockstate(VenusRegistry.TURPIUM.get().getDefaultState(), 2)
                                .addWeightedBlockstate(VenusRegistry.GLOWING_TURPIUM.get().getDefaultState(), 1),
                        FeatureSpread.func_242253_a(2, 1))).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT)
                        .chance(5));
        builder.withFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS,
                deerangle.space.planet.feature.Features.CRATER.get().withConfiguration(new CraterFeatureConfig(
                        new SimpleBlockStateProvider(VenusRegistry.PULCHERITE_TURF.get().getDefaultState()),
                        FeatureSpread.func_242253_a(3, 4), FeatureSpread.func_242253_a(2, 4),
                        FeatureSpread.func_242253_a(1, 1))).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT)
                        .chance(6));
        builder.withFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS,
                deerangle.space.planet.feature.Features.CRATER.get().withConfiguration(new CraterFeatureConfig(
                        new SimpleBlockStateProvider(VenusRegistry.PULCHERITE_TURF.get().getDefaultState()),
                        FeatureSpread.func_242253_a(7, 5), FeatureSpread.func_242253_a(5, 4),
                        FeatureSpread.func_242253_a(1, 2))).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT)
                        .chance(30));
        builder.withFeature(GenerationStage.Decoration.LAKES, deerangle.space.planet.feature.Features.LAKE.get()
                .withConfiguration(new BetterLakeFeatureConfig(
                        new SimpleBlockStateProvider(FluidRegistry.ACID_BLOCK.get().getDefaultState()),
                        new SimpleBlockStateProvider(VenusRegistry.TURPIUM.get().getDefaultState())))
                .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).chance(5));
        builder.withFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Feature.FLOWER.withConfiguration(
                (new BlockClusterFeatureConfig.Builder(
                        new SimpleBlockStateProvider(VenusRegistry.TURPIUM_ROCK.get().getDefaultState()),
                        SimpleBlockPlacer.PLACER)).tries(6).build())
                .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).func_242731_b(4));
        builder.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
                deerangle.space.planet.feature.Features.DISK.get().withConfiguration(
                        new SphereReplaceConfig(VenusRegistry.GLOWING_TURPIUM.get().getDefaultState(),
                                FeatureSpread.func_242253_a(2, 2), 2, ImmutableList
                                .of(VenusRegistry.PULCHERITE_TURF.get().getDefaultState(),
                                        VenusRegistry.PULCHERITE.get().getDefaultState())))
                        .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).func_242731_b(1));
        builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION,
                deerangle.space.planet.feature.Features.ALGAE_PATCH.get()
                        .withConfiguration(new AlgaePathFeatureConfig(FeatureSpread.func_242253_a(2, 1)))
                        .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).func_242731_b(2));
        DefaultBiomeFeatures.withCavesAndCanyons(builder);
        return (new Biome.Builder()).precipitation(Biome.RainType.NONE).category(Biome.Category.NONE).depth(0.7f)
                .scale(0.4f).temperature(0.8F).downfall(2.0F).setEffects(
                        new BiomeAmbience.Builder().setWaterColor(0x577545).setWaterFogColor(0x4aad36)
                                .setFogColor(0xd6c156).withSkyColor(0xc2c27e)
                                .setMoodSound(MoodSoundAmbience.DEFAULT_CAVE).build())
                .withMobSpawnSettings(spawnSettings.copy()).withGenerationSettings(builder.build()).build();
    }

    public static Biome makeRiverBiome() {
        MobSpawnInfo.Builder spawnSettings = new MobSpawnInfo.Builder();
        BiomeGenerationSettings.Builder builder = (new BiomeGenerationSettings.Builder()).withSurfaceBuilder(
                SurfaceBuilder.DEFAULT.func_242929_a(
                        new SurfaceBuilderConfig(VenusRegistry.TURPIUM.get().getDefaultState(),
                                VenusRegistry.PULCHERITE.get().getDefaultState(),
                                VenusRegistry.TURPIUM.get().getDefaultState())));
        return (new Biome.Builder()).precipitation(Biome.RainType.NONE).category(Biome.Category.RIVER).depth(-0.6F)
                .scale(0.0F).temperature(0.8F).downfall(2.0F).setEffects(
                        new BiomeAmbience.Builder().setWaterColor(0x577545).setWaterFogColor(0x4aad36)
                                .setFogColor(0xd6c156).withSkyColor(0xc2c27e)
                                .setMoodSound(MoodSoundAmbience.DEFAULT_CAVE).build())
                .withMobSpawnSettings(spawnSettings.copy()).withGenerationSettings(builder.build()).build();
    }

    // TODO: add caves to venus
    // TODO: add vulcanos

}
