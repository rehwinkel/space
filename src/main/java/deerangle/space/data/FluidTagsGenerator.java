package deerangle.space.data;

import deerangle.space.registry.FluidRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.TagsProvider;
import net.minecraft.fluid.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.nio.file.Path;

public class FluidTagsGenerator extends TagsProvider<Fluid> {

    public FluidTagsGenerator(DataGenerator generatorIn, String modId, ExistingFileHelper existingFileHelper) {
        super(generatorIn, Registry.FLUID, modId, existingFileHelper);
    }

    protected void registerTags() {
        this.getOrCreateBuilder(FluidTags.WATER)
                .add(FluidRegistry.CRUDE_OIL.get(), FluidRegistry.CRUDE_OIL_FLOWING.get());
    }

    protected Path makePath(ResourceLocation id) {
        return this.generator.getOutputFolder()
                .resolve("data/" + id.getNamespace() + "/tags/fluids/" + id.getPath() + ".json");
    }

    public String getName() {
        return "Fluid Tags";
    }

}
