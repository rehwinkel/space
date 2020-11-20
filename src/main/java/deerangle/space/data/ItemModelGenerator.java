package deerangle.space.data;

import deerangle.space.registry.FluidRegistry;
import deerangle.space.registry.ResourceRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.DynamicBucketModel;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.loaders.DynamicBucketModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModelGenerator extends ItemModelProvider {

    public ItemModelGenerator(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator, modid, existingFileHelper);
    }

    private void addFlatItem(Item item) {
        getBuilder(item.getRegistryName().getPath()).parent(getExistingFile(new ResourceLocation("item/generated")))
                .texture("layer0", item.getRegistryName().getNamespace() + ":item/" + item.getRegistryName().getPath());
    }

    private void addBucketItem(BucketItem item) {
        getBuilder(item.getRegistryName().getPath())
                .parent(new ModelFile.UncheckedModelFile(new ResourceLocation("forge", "item/bucket")))
                .customLoader(DynamicBucketModelBuilder::begin).fluid(item.getFluid());
    }

    @Override
    protected void registerModels() {
        addFlatItem(ResourceRegistry.STEEL_INGOT.get());
        addBucketItem(FluidRegistry.CRUDE_OIL_BUCKET.get());
    }

}
