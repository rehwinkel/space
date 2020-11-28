package deerangle.space.planet.planets.mars.world;

import deerangle.space.planet.feature.RockFeatureConfig;
import deerangle.space.planet.planets.mars.MarsRegistry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class MarsBiomeMaker {

    public static final SurfaceBuilderConfig RUSTY_DESERT_SURFACE_CONFIG = new SurfaceBuilderConfig(MarsRegistry.RUSTY_DUST.get().getDefaultState(), MarsRegistry.REGOLITH.get().getDefaultState(), MarsRegistry.RUSTY_DUST.get().getDefaultState());
    public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> RUSTY_DESERT_SURFACE_BUILDER = WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_SURFACE_BUILDER, "rusty_desert", SurfaceBuilder.DEFAULT.func_242929_a(RUSTY_DESERT_SURFACE_CONFIG));

    public static Biome makeDesertBiome(float depth, float scale) {
        MobSpawnInfo.Builder mobSpawnInfo = new MobSpawnInfo.Builder();
        BiomeGenerationSettings.Builder builder = (new BiomeGenerationSettings.Builder()).withSurfaceBuilder(RUSTY_DESERT_SURFACE_BUILDER);

        builder.withFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, deerangle.space.planet.feature.Features.ROCK.get().withConfiguration(new RockFeatureConfig(new SimpleBlockStateProvider(MarsRegistry.REGOLITH.get().getDefaultState()), FeatureSpread.func_242253_a(3, 4))).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).chance(3));

        DefaultBiomeFeatures.withCavesAndCanyons(builder);
        DefaultBiomeFeatures.withDisks(builder);
        DefaultBiomeFeatures.withBadlandsGrass(builder);
        return (new Biome.Builder()).precipitation(Biome.RainType.NONE).category(Biome.Category.DESERT).depth(depth).scale(scale).temperature(2.0F).downfall(0.0F).setEffects((new BiomeAmbience.Builder()).setWaterColor(4159204).setWaterFogColor(329011).setFogColor(0xa67c50).withSkyColor(0xbaa274).setMoodSound(MoodSoundAmbience.DEFAULT_CAVE).build()).withMobSpawnSettings(mobSpawnInfo.copy()).withGenerationSettings(builder.build()).build();
    }

}
