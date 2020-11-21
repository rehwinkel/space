package deerangle.space.data;

import deerangle.space.registry.FluidRegistry;
import deerangle.space.registry.ResourceRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
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
        addBucketItem(FluidRegistry.KEROSENE_BUCKET.get());
        addFlatItem(ResourceRegistry.COPPER_INGOT.get());
        addFlatItem(ResourceRegistry.ALUMINIUM_INGOT.get());
        addFlatItem(ResourceRegistry.TITANIUM_INGOT.get());
        addFlatItem(ResourceRegistry.STEEL_NUGGET.get());
        addFlatItem(ResourceRegistry.COPPER_NUGGET.get());
        addFlatItem(ResourceRegistry.ALUMINIUM_NUGGET.get());
        addFlatItem(ResourceRegistry.TITANIUM_NUGGET.get());
        addFlatItem(ResourceRegistry.ALUMINIUM_PLATE.get());
        addFlatItem(ResourceRegistry.COPPER_PLATE.get());
        addFlatItem(ResourceRegistry.IRON_PLATE.get());
        addFlatItem(ResourceRegistry.STEEL_PLATE.get());
        addFlatItem(ResourceRegistry.BATTERY.get());
        addFlatItem(ResourceRegistry.CYLINDER.get());
        addFlatItem(ResourceRegistry.IGNITION_COIL.get());
        addFlatItem(ResourceRegistry.INDUSTRIAL_PISTON.get());
        addFlatItem(ResourceRegistry.COPPER_TUBE.get());
        addFlatItem(ResourceRegistry.IRON_TUBE.get());
        addFlatItem(ResourceRegistry.IRON_DUST.get());
        addFlatItem(ResourceRegistry.QUARTZ_DUST.get());
        addFlatItem(ResourceRegistry.STEEL_ROD.get());
        addFlatItem(ResourceRegistry.HEAT_RESISTENT_GLASS.get());
        addFlatItem(ResourceRegistry.MACHINE_BASE.get());
        addFlatItem(ResourceRegistry.SILICA_TILE.get());
        addFlatItem(ResourceRegistry.PLATED_ROCKET_BODY.get());
        addFlatItem(ResourceRegistry.ROCKET_CONE.get());
        addFlatItem(ResourceRegistry.ROCKET_FIN.get());
        addFlatItem(ResourceRegistry.ROCKET_THRUSTER.get());
    }

}
