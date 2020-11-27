package deerangle.space.planet.planets.mercury.data;

import deerangle.space.data.AbstractLootTableGenerator;
import deerangle.space.planet.planets.mercury.MercuryRegistry;
import net.minecraft.data.DataGenerator;

public class LootTableGenerator extends AbstractLootTableGenerator {
    public LootTableGenerator(DataGenerator generator, String mod_id) {
        super(generator, mod_id);
    }

    @Override
    protected void populate() {
        normalBlock(MercuryRegistry.FIRESTONE.get());
        normalBlock(MercuryRegistry.PLANET_CORE.get());
    }
}
