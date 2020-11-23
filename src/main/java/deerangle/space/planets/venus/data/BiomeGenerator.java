package deerangle.space.planets.venus.data;

import deerangle.space.planets.AbstractBiomeGenerator;
import deerangle.space.planets.venus.VenusRegistry;
import deerangle.space.planets.venus.world.VenusBiomeMaker;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.biome.Biome;

import java.util.Map;

public class BiomeGenerator extends AbstractBiomeGenerator {

    public BiomeGenerator(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void addBiomes(Map<RegistryKey<Biome>, Biome> biomeMap) {
        biomeMap.put(VenusRegistry.VENUS_HILLS, VenusBiomeMaker.makeHillsBiome());
    }

}
