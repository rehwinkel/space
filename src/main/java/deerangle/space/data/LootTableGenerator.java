package deerangle.space.data;

import deerangle.space.planet.planets.mars.MarsRegistry;
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
        normalBlock(ResourceRegistry.ILMENITE_ORE.get());
        normalBlock(ResourceRegistry.ALUMINIUM_BLOCK.get());
        normalBlock(ResourceRegistry.COPPER_BLOCK.get());
        normalBlock(ResourceRegistry.STEEL_BLOCK.get());
        normalBlock(ResourceRegistry.TITANIUM_BLOCK.get());
        normalBlock(ResourceRegistry.QUARTZ_SAND.get());
        normalBlock(ResourceRegistry.CONTROLLER.get());
        normalBlock(MarsRegistry.RUSTY_DUST.get());
        normalBlock(MarsRegistry.REGOLITH.get());
    }

}