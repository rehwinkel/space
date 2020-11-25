package deerangle.space.planets.data;

import deerangle.space.planets.DimensionMaker;
import deerangle.space.planets.venus.VenusRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.Dimension;

import java.util.Map;

public class DimensionGenerator extends AbstractDimensionGenerator {

    public DimensionGenerator(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void addDimensions(Map<RegistryKey<Dimension>, Dimension> dimensionMap) {
        dimensionMap.put(VenusRegistry.DIMENSION, DimensionMaker.makeVenusDimension());
    }

}
