package deerangle.space.planet.planets.venus.data;

import deerangle.space.planet.planets.venus.VenusRegistry;
import deerangle.space.planet.planets.venus.tags.BlockTags;
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
        this.getOrCreateBuilder(BlockTags.VENUS_GROUND).add(VenusRegistry.TURPIUM.get()).add(VenusRegistry.TURPIUM_COBBLESTONE.get()).add(VenusRegistry.PULCHERITE.get()).add(VenusRegistry.OVERGROWN_PULCHERITE.get()).add(VenusRegistry.GLOWING_TURPIUM.get());
        this.getOrCreateBuilder(BlockTags.VENUS_OVERGROWABLE).add(VenusRegistry.PULCHERITE.get());
        this.getOrCreateBuilder(BlockTags.CRYSTAL_GROUND).add(VenusRegistry.CRYSTAL_BLOCK.get());
    }

    protected Path makePath(ResourceLocation id) {
        return this.generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/tags/blocks/" + id.getPath() + ".json");
    }

    public String getName() {
        return "Block Tags";
    }

}
