package deerangle.space.planet;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import deerangle.space.planet.planets.venus.VenusRegistry;
import deerangle.space.planet.planets.venus.world.VenusBiomeProvider;
import deerangle.space.registry.PlanetRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.world.Dimension;
import net.minecraft.world.biome.BiomeRegistry;
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

}
