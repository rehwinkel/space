package deerangle.space.planets.venus.data;

import deerangle.space.data.AbstractLootTableGenerator;
import deerangle.space.planets.venus.VenusRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Items;
import net.minecraft.world.gen.feature.Feature;

public class LootTableGenerator extends AbstractLootTableGenerator {

    public LootTableGenerator(DataGenerator generator, String mod_id) {
        super(generator, mod_id);
    }

    @Override
    protected void populate() {
        normalBlock(VenusRegistry.PULCHERITE.get());
        normalBlock(VenusRegistry.PULCHERITE_TURF.get());
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
        normalBlock(VenusRegistry.SLIMY_ALGAE.get());
        slabBlock(VenusRegistry.SHRIEKWOOD_SLAB.get());
        grassBlock(VenusRegistry.SHRIEKGRASS.get());
        oreBlock(VenusRegistry.PULCHERITE_COAL.get(), Items.COAL);
        oreBlock(VenusRegistry.PULCHERITE_SULFUR.get(), VenusRegistry.SULFUR.get());
        doorBlock(VenusRegistry.SHRIEKWOOD_DOOR.get(), VenusRegistry.SHRIEKWOOD_DOOR_ITEM.get());
        normalBlock(VenusRegistry.OVERGROWN_PULCHERITE.get(), VenusRegistry.PULCHERITE.get());
        normalBlock(VenusRegistry.TURPIUM.get(), VenusRegistry.TURPIUM_COBBLESTONE.get());
        normalBlock(VenusRegistry.SHRIEKWOOD_SAPLING.get());
        leavesBlock(VenusRegistry.SHRIEKWOOD_LEAVES.get(), VenusRegistry.SHRIEKWOOD_SAPLING.get());
    }

}