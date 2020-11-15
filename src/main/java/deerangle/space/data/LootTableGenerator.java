package deerangle.space.data;

import deerangle.space.block.BlockRegistry;
import net.minecraft.data.DataGenerator;

public class LootTableGenerator extends AbstractLootTableGenerator {

    public LootTableGenerator(DataGenerator generator, String mod_id) {
        super(generator, mod_id);
    }

    @Override
    protected void populate() {
        normalBlock(BlockRegistry.COPPER_ORE.get());
        normalBlock(BlockRegistry.ALUMINIUM_ORE.get());
    }

}
