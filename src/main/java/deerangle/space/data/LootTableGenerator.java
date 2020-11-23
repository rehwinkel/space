package deerangle.space.data;

import deerangle.space.planets.mars.MarsRegistry;
import deerangle.space.planets.venus.VenusRegistry;
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
        normalBlock(VenusRegistry.PULCHERITE.get());
        normalBlock(VenusRegistry.PULCHERITE_BRICKS.get());
        normalBlock(VenusRegistry.POLISHED_PULCHERITE.get());
        normalBlock(VenusRegistry.GLOWING_TURPIUM.get());
        normalBlock(VenusRegistry.TURPIUM_COBBLESTONE.get());
        normalBlock(VenusRegistry.SHRIEKWOOD_WOOD.get());
        normalBlock(VenusRegistry.SHRIEKWOOD_LOG.get());
        normalBlock(VenusRegistry.SHRIEKWOOD_PLANKS.get());
        normalBlock(VenusRegistry.SHRIEKWOOD_STAIRS.get());
        normalBlock(VenusRegistry.TURPIUM_ROCK.get());
        normalBlock(VenusRegistry.PULCHERITE_ILMENITE.get());
        normalBlock(VenusRegistry.OVERGROWN_PULCHERITE.get(), VenusRegistry.PULCHERITE.get());
        normalBlock(VenusRegistry.TURPIUM.get(), VenusRegistry.TURPIUM_COBBLESTONE.get());
        //TODO: Shriekwood slab, door, leaves, sapling; shriekgrass, slimy algae, pulcherite coal and sulfur ore
    }

}