package deerangle.space.planets.venus.world;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.SimplexNoiseGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class VenusBiomeProvider extends BiomeProvider {
    public static final Codec<VenusBiomeProvider> CODEC = RecordCodecBuilder
            .create((providerInstance) -> providerInstance
                    .group(Codec.LONG.fieldOf("seed").forGetter(biomeProvider -> biomeProvider.seed),
                            Biome.BIOME_CODEC.fieldOf("river_biome")
                                    .forGetter(biomeProvider -> biomeProvider.riverBiome),
                            Biome.BIOMES_CODEC.fieldOf("biomes")
                                    .forGetter((checkerProvider) -> checkerProvider.otherBiomes),
                            Codec.INT_STREAM.fieldOf("biome_weights")
                                    .forGetter((biomeProvider) -> Arrays.stream(biomeProvider.weights.clone())))
                    .apply(providerInstance, VenusBiomeProvider::new));

    private final SimplexNoiseGenerator simplexRng;
    private long seed;
    private Supplier<Biome> riverBiome;
    private List<Supplier<Biome>> otherBiomes;
    private int[] weights;

    public VenusBiomeProvider(long seed, Supplier<Biome> riverBiome, List<Supplier<Biome>> biomes, IntStream weights) {
        this(seed, riverBiome, biomes, weights.toArray());
    }

    public VenusBiomeProvider(long seed, Supplier<Biome> riverBiome, List<Supplier<Biome>> biomes, int[] weights) {
        super(Stream.concat(biomes.stream(), ImmutableList.of(riverBiome).stream()));
        this.seed = seed;
        this.simplexRng = new SimplexNoiseGenerator(new Random(seed));
        this.riverBiome = riverBiome;
        this.otherBiomes = biomes;
        this.weights = weights;
    }

    @Override
    protected Codec<? extends BiomeProvider> getBiomeProviderCodec() {
        return CODEC;
    }

    @Override
    public BiomeProvider getBiomeProvider(long seed) {
        return new VenusBiomeProvider(seed, this.riverBiome, this.otherBiomes, this.weights);
    }

    @Override
    public Biome getNoiseBiome(int x, int y, int z) {
        double rand = simplexRng.getValue(x / 30d, z / 30d);
        return rand > -0.07 && rand < 0.07 ? riverBiome.get() : otherBiomes.get(0).get();
    }

}
