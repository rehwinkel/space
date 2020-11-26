package deerangle.space.world.mars.biomes;

import deerangle.space.planets.mars.MarsRegistry;
import deerangle.space.registry.AbstractRegistry;
import deerangle.space.registry.ResourceRegistry;
import deerangle.space.registry.WorldRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.ConfiguredPlacement;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilders;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class MarsBiomeMaker {

    public static final SurfaceBuilderConfig RUSTY_DESERT_SURFACE_CONFIG = new SurfaceBuilderConfig(MarsRegistry.RUSTY_DUST.get().getDefaultState(), MarsRegistry.REGOLITH.get().getDefaultState(), MarsRegistry.RUSTY_DUST.get().getDefaultState());
    public static final ConfiguredSurfaceBuilder<SurfaceBuilderConfig> RUSTY_DESERT_SURFACE_BUILDER = WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_SURFACE_BUILDER, "rusty_desert", SurfaceBuilder.DEFAULT.func_242929_a(RUSTY_DESERT_SURFACE_CONFIG));

    //TODO: remove stronghold

    public static Biome makeRustyDesertBiome(float depth, float scale) {
        MobSpawnInfo.Builder mobSpawnInfo = new MobSpawnInfo.Builder();
        BiomeGenerationSettings.Builder biomeGenerationSettings = (new BiomeGenerationSettings.Builder()).withSurfaceBuilder(RUSTY_DESERT_SURFACE_BUILDER);

        DefaultBiomeFeatures.withCavesAndCanyons(biomeGenerationSettings);
        DefaultBiomeFeatures.withDisks(biomeGenerationSettings);
        DefaultBiomeFeatures.withBadlandsGrass(biomeGenerationSettings);
        return (new Biome.Builder()).precipitation(Biome.RainType.NONE).category(Biome.Category.DESERT)
                .depth(depth).scale(scale).temperature(2.0F).downfall(0.0F).setEffects((new BiomeAmbience.Builder()).setWaterColor(4159204)
                .setWaterFogColor(329011).setFogColor(0xa67c50).withSkyColor(0xbaa274).setMoodSound(MoodSoundAmbience.DEFAULT_CAVE).build())
                .withMobSpawnSettings(mobSpawnInfo.copy()).withGenerationSettings(biomeGenerationSettings.build()).build();
    }

}
