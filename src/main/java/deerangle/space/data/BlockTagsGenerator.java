package deerangle.space.data;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.TagsProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.nio.file.Path;

public class BlockTagsGenerator extends TagsProvider<Block> {

    public BlockTagsGenerator(DataGenerator generatorIn, String modId, ExistingFileHelper existingFileHelper) {
        super(generatorIn, Registry.BLOCK, modId, existingFileHelper);
    }

    protected void registerTags() {
    }

    protected Path makePath(ResourceLocation id) {
        return this.generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/tags/blocks/" + id.getPath() + ".json");
    }

    public String getName() {
        return "Block Tags";
    }

}
