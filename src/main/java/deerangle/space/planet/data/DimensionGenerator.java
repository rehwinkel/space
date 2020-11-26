package deerangle.space.planet.data;

import deerangle.space.planet.Planet;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.Dimension;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

import java.util.Map;

public class DimensionGenerator extends AbstractDimensionGenerator {

    public DimensionGenerator(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void addDimensions(Map<ResourceLocation, Dimension> dimensionMap) {
        IForgeRegistry<Planet> planetRegistry = RegistryManager.ACTIVE.getRegistry(Planet.class);
        for (Map.Entry<RegistryKey<Planet>, Planet> entry : planetRegistry.getEntries()) {
            if (entry.getValue().getDimensionMaker() != null) {
                dimensionMap.put(entry.getKey().getLocation(), entry.getValue().getDimensionMaker().get());
            }
        }
    }

}
