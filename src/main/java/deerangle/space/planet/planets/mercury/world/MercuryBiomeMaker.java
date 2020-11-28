package deerangle.space.planet.planets.mercury.world;

import net.minecraft.world.biome.*;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilders;

public class MercuryBiomeMaker {

    public static Biome makeVastBiome() {
        MobSpawnInfo.Builder mobSpawnInfo = new MobSpawnInfo.Builder();
        BiomeGenerationSettings.Builder builder = (new BiomeGenerationSettings.Builder()).withSurfaceBuilder(ConfiguredSurfaceBuilders.field_244169_a);

        DefaultBiomeFeatures.withCavesAndCanyons(builder);
        DefaultBiomeFeatures.withDisks(builder);
        DefaultBiomeFeatures.withBadlandsGrass(builder);

        return (new Biome.Builder()).precipitation(Biome.RainType.SNOW).category(Biome.Category.DESERT).depth(0.5f).scale(0.0f).temperature(2.0F).downfall(0.0F).setEffects((new BiomeAmbience.Builder()).setWaterColor(4159204).setWaterFogColor(329011).setFogColor(0xa67c50).withSkyColor(0xbaa274).setMoodSound(MoodSoundAmbience.DEFAULT_CAVE).build()).withMobSpawnSettings(mobSpawnInfo.copy()).withGenerationSettings(builder.build()).build();
    }

}
