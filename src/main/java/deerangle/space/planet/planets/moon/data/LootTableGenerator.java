package deerangle.space.planet.planets.moon.data;

import deerangle.space.data.AbstractLootTableGenerator;
import deerangle.space.planet.planets.moon.MoonRegistry;
import net.minecraft.data.DataGenerator;

public class LootTableGenerator extends AbstractLootTableGenerator {
    public LootTableGenerator(DataGenerator generator, String mod_id) {
        super(generator, mod_id);
    }

    @Override
    protected void populate() {
        normalBlock(MoonRegistry.DUST.get());
    }
}
