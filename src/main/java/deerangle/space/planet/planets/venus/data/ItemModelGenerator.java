package deerangle.space.planet.planets.venus.data;

import deerangle.space.planet.planets.venus.VenusRegistry;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModelGenerator extends ItemModelProvider {

    public ItemModelGenerator(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator, modid, existingFileHelper);
    }

    private void addFlatItem(Item item) {
        getBuilder(item.getRegistryName().getPath()).parent(getExistingFile(new ResourceLocation("item/generated"))).texture("layer0", item.getRegistryName().getNamespace() + ":venus/item/" + item.getRegistryName().getPath());
    }

    private void addFlatItemForBlock(Block block) {
        getBuilder(block.getRegistryName().getPath()).parent(getExistingFile(new ResourceLocation("item/generated"))).texture("layer0", block.getRegistryName().getNamespace() + ":venus/block/" + block.getRegistryName().getPath());
    }

    @Override
    protected void registerModels() {
        addFlatItem(VenusRegistry.MUSIC_DISC_LOVE.get());
        addFlatItem(VenusRegistry.MUSIC_DISC_SPICY_AND_SOUR.get());
        addFlatItem(VenusRegistry.SULFUR.get());
        addFlatItem(VenusRegistry.SLIMY_ALGAE.get().asItem());
        addFlatItem(VenusRegistry.SHRIEKWOOD_DOOR_ITEM.get());
        addFlatItem(VenusRegistry.CRYSTAL.get().asItem());
        addFlatItemForBlock(VenusRegistry.SHRIEKGRASS.get());
        addFlatItemForBlock(VenusRegistry.VENUS_BACTERIA.get());
        addFlatItemForBlock(VenusRegistry.SHRIEKWOOD_SAPLING.get());
    }

}
