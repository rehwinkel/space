package deerangle.space.planets;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import deerangle.space.main.SpaceMod;
import deerangle.space.planets.util.CustomDimensionType;
import deerangle.space.planets.venus.VenusRegistry;
import deerangle.space.planets.venus.world.VenusBiomeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.Dimension;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.NoiseChunkGenerator;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.NoiseSettings;
import net.minecraft.world.gen.settings.ScalingSettings;
import net.minecraft.world.gen.settings.SlideSettings;

import java.util.Optional;
import java.util.OptionalLong;
import java.util.function.Supplier;

public class DimensionMaker {

    public static final ResourceLocation VENUS_DIMENSION_TYPE = new ResourceLocation(SpaceMod.MOD_ID, "venus");

    public static Dimension makeVenusDimension() {
        return new Dimension(
                () -> new CustomDimensionType(OptionalLong.empty(), true, false, true, false, 1.0D, false, false, false,
                        true, false, 256, ColumnFuzzedBiomeMagnifier.INSTANCE, BlockTags.INFINIBURN_OVERWORLD.getName(),
                        VENUS_DIMENSION_TYPE, 0.0F), new NoiseChunkGenerator(
                new VenusBiomeProvider(0, pseudoBiome(VenusRegistry.VENUS_LAVA_RIVER),
                        ImmutableList.of(pseudoBiome(VenusRegistry.VENUS_HILLS)), new int[]{1}), -1,
                () -> new DimensionSettings(new DimensionStructuresSettings(Optional.empty(), ImmutableMap.of()),
                        new NoiseSettings(256, new ScalingSettings(1.0, 1.0, 1000.0, 160.0),
                                new SlideSettings(-10, 3, 0), new SlideSettings(-30, 0, 0), 1, 2, 1.0, -0.46875, true,
                                false, false, false), VenusRegistry.PULCHERITE.get().getDefaultState(),
                        Blocks.LAVA.getDefaultState(), -10, 0, 70, false)));

    }

    private static Supplier<Biome> pseudoBiome(RegistryKey<Biome> key) {
        return () -> new Biome.Builder().precipitation(Biome.RainType.NONE).category(Biome.Category.NONE).depth(0)
                .scale(0).temperature(0).downfall(0).setEffects(
                        new BiomeAmbience.Builder().setWaterFogColor(0).withSkyColor(0).setWaterColor(0).setFogColor(0)
                                .build()).withMobSpawnSettings(new MobSpawnInfo.Builder().copy())
                .withGenerationSettings(BiomeGenerationSettings.DEFAULT_SETTINGS).build()
                .setRegistryName(key.getLocation());
    }

}
