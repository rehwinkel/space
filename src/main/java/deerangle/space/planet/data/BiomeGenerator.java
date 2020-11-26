package deerangle.space.planet.data;

import deerangle.space.planet.Planet;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

import java.util.Map;
import java.util.function.Supplier;

public class BiomeGenerator extends AbstractBiomeGenerator {

    public BiomeGenerator(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void addBiomes(Map<ResourceLocation, Biome> biomeMap) {
        IForgeRegistry<Planet> planetRegistry = RegistryManager.ACTIVE.getRegistry(Planet.class);
        for (Map.Entry<RegistryKey<Planet>, Planet> entry : planetRegistry.getEntries()) {
            for (Supplier<Biome> biomeMaker : entry.getValue().getBiomeMakers()) {
                Biome b = biomeMaker.get();
                biomeMap.put(b.getRegistryName(), b);
            }
        }
    }

}
