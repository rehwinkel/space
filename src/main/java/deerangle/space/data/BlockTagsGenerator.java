package deerangle.space.data;

import deerangle.space.registry.MachineRegistry;
import deerangle.space.tags.BlockTags;
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
        this.getOrCreateBuilder(BlockTags.ENERGY_MACHINES).add(MachineRegistry.BATTERY_PACK.get())
                .add(MachineRegistry.REFINERY.get()).add(MachineRegistry.COAL_GENERATOR.get())
                .add(MachineRegistry.COMBUSTION_GENERATOR.get());
        this.getOrCreateBuilder(BlockTags.ITEM_MACHINES).add(MachineRegistry.COAL_GENERATOR.get())
                .add(MachineRegistry.BLAST_FURNACE.get());
        this.getOrCreateBuilder(BlockTags.FLUID_MACHINES).add(MachineRegistry.DRUM.get())
                .add(MachineRegistry.GAS_TANK.get()).add(MachineRegistry.REFINERY.get())
                .add(MachineRegistry.COMBUSTION_GENERATOR.get());
    }

    protected Path makePath(ResourceLocation id) {
        return this.generator.getOutputFolder()
                .resolve("data/" + id.getNamespace() + "/tags/blocks/" + id.getPath() + ".json");
    }

    public String getName() {
        return "Block Tags";
    }

}
