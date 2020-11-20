package deerangle.space.data;

import deerangle.space.registry.ResourceRegistry;
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
        getBuilder(item.getRegistryName().getPath()).parent(getExistingFile(new ResourceLocation("item/generated")))
                .texture("layer0", item.getRegistryName().getNamespace() + ":item/" + item.getRegistryName().getPath());
    }

    @Override
    protected void registerModels() {
        addFlatItem(ResourceRegistry.STEEL_INGOT.get());
    }

}
