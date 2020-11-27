package deerangle.space.planet;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import deerangle.space.planet.planets.mars.MarsRegistry;
import deerangle.space.planet.planets.venus.VenusRegistry;
import deerangle.space.planet.planets.venus.world.VenusBiomeProvider;
import deerangle.space.registry.PlanetRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.Dimension;
import net.minecraft.world.biome.BiomeRegistry;
import net.minecraft.world.biome.provider.OverworldBiomeProvider;
import net.minecraft.world.biome.provider.SingleBiomeProvider;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.NoiseChunkGenerator;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.NoiseSettings;
import net.minecraft.world.gen.settings.ScalingSettings;
import net.minecraft.world.gen.settings.SlideSettings;

import java.util.Optional;

public class DimensionMaker {

    public static Dimension makeVenusDimension() {
        return new Dimension(() -> PlanetRegistry.VENUS.getDimensionType(), new NoiseChunkGenerator(
                new VenusBiomeProvider(0, () -> BiomeRegistry.THE_VOID, ImmutableList.of(), new int[]{1}), -1,
                () -> new DimensionSettings(new DimensionStructuresSettings(Optional.empty(), ImmutableMap.of()),
                        new NoiseSettings(256, new ScalingSettings(1.0, 1.0, 1000.0, 160.0),
                                new SlideSettings(-10, 3, 0), new SlideSettings(-30, 0, 0), 1, 2, 1.0, -0.46875, true,
                                false, false, false), VenusRegistry.PULCHERITE.get().getDefaultState(),
                        Blocks.LAVA.getDefaultState(), -10, 0, 70, false)));

    }

    public static Dimension makeMarsDimension() {
        return new Dimension(() -> PlanetRegistry.MARS.getDimensionType(),
                new NoiseChunkGenerator(new SingleBiomeProvider(BiomeRegistry.THE_VOID), -1,
                        () -> new DimensionSettings(
                                new DimensionStructuresSettings(Optional.empty(), ImmutableMap.of()),
                                new NoiseSettings(256, new ScalingSettings(1.0, 1.0, 80, 160.0),
                                        new SlideSettings(-10, 3, 0), new SlideSettings(-30, 0, 0), 1, 2, 1.0, -0.46875,
                                        true, false, false, false), MarsRegistry.REGOLITH.get().getDefaultState(),
                                Blocks.WATER.getDefaultState(), -10, 0, 0, false)));
    }

    public static Dimension makeMercuryDimension() {
        //TODO
        return new Dimension(() -> PlanetRegistry.MERCURY.getDimensionType(),
                new NoiseChunkGenerator(new OverworldBiomeProvider(-1, false, false, WorldGenRegistries.BIOME), -1,
                        () -> new DimensionSettings(
                                new DimensionStructuresSettings(Optional.empty(), ImmutableMap.of()),
                                new NoiseSettings(256, new ScalingSettings(1.0, 1.0, 80, 160.0),
                                        new SlideSettings(-10, 3, 0), new SlideSettings(-30, 0, 0), 1, 2, 1.0, -0.46875,
                                        true, false, false, false), Blocks.DIAMOND_BLOCK.getDefaultState(),
                                Blocks.GREEN_TERRACOTTA.getDefaultState(), -10, 0, 0, false)));
    }

}
