package deerangle.space.data;

import deerangle.space.registry.ResourceRegistry;
import net.minecraft.data.DataGenerator;

public class LootTableGenerator extends AbstractLootTableGenerator {

    public LootTableGenerator(DataGenerator generator, String mod_id) {
        super(generator, mod_id);
    }

    @Override
    protected void populate() {
        normalBlock(ResourceRegistry.COPPER_ORE.get());
        normalBlock(ResourceRegistry.ALUMINIUM_ORE.get());
    }

}
