package deerangle.space.planets.venus.world;

import deerangle.space.planets.venus.VenusRegistry;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class VenusBiomeMaker {

    private static int getSkyColorWithTemperatureModifier(float temperature) {
        float v = temperature / 3.0F;
        v = MathHelper.clamp(v, -1.0F, 1.0F);
        return MathHelper.hsvToRGB(0.62222224F - v * 0.05F, 0.5F + v * 0.1F, 1.0F);
    }

    public static Biome makeHillsBiome() {
        float depth = 0.6f;
        float scale = 0.1f;
        MobSpawnInfo.Builder mobSpawnSettings = new MobSpawnInfo.Builder();
        BiomeGenerationSettings.Builder generationSettings = new BiomeGenerationSettings.Builder().withSurfaceBuilder(
                SurfaceBuilder.DEFAULT.func_242929_a(
                        new SurfaceBuilderConfig(VenusRegistry.PULCHERITE_TURF.get().getDefaultState(),
                                VenusRegistry.PULCHERITE_TURF.get().getDefaultState(),
                                VenusRegistry.PULCHERITE_TURF.get().getDefaultState())));
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
                .scale(scale).temperature(4.0F).downfall(2.0F).setEffects(
                        (new BiomeAmbience.Builder()).setWaterColor(0x577545).setWaterFogColor(0x4aad36)
                                .setFogColor(0xd6c156).withSkyColor(0xc2c27e)
                                .setMoodSound(MoodSoundAmbience.DEFAULT_CAVE).build())
                .withMobSpawnSettings(mobSpawnSettings.copy()).withGenerationSettings(generationSettings.build())
                .build();
    }

}
