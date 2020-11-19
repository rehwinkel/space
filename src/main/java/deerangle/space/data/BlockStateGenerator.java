package deerangle.space.data;

import deerangle.space.block.MachineBlock;
import deerangle.space.registry.MachineRegistry;
import deerangle.space.registry.ResourceRegistry;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStateGenerator extends BlockStateProvider {

    public BlockStateGenerator(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
        super(gen, modid, exFileHelper);
    }

    private void simpleBlockWithItem(Block block) {
        simpleBlock(block);
        ResourceLocation blockName = block.getRegistryName();
        simpleBlockItem(block, models().getExistingFile(
                new ResourceLocation(blockName.getNamespace(), "block/" + blockName.getPath())));
    }

    private void existingBlockWithItem(Block block) {
        ResourceLocation blockName = block.getRegistryName();
        ResourceLocation parent = new ResourceLocation(blockName.getNamespace(), "block/" + blockName.getPath());
        getVariantBuilder(block).partialState().setModels(new ConfiguredModel(models().getExistingFile(parent)));
        simpleBlockItem(block, models().getExistingFile(parent));
    }

    private void machineBlockWithItem(Block block) {
        ResourceLocation blockName = block.getRegistryName();
        ModelFile baseModelOff = models()
                .getExistingFile(new ResourceLocation(blockName.getNamespace(), "block/" + blockName.getPath()));
        ModelFile baseModelRunning = models().getExistingFile(
                new ResourceLocation(blockName.getNamespace(), "block/" + blockName.getPath() + "_running"));

        getMultipartBuilder(block)
                .part().modelFile(baseModelOff).addModel().condition(MachineBlock.FACING, Direction.NORTH).condition(MachineBlock.RUNNING, false).end()
                .part().modelFile(baseModelOff).rotationY(180).addModel().condition(MachineBlock.FACING, Direction.SOUTH).condition(MachineBlock.RUNNING, false).end()
                .part().modelFile(baseModelOff).rotationY(270).addModel().condition(MachineBlock.FACING, Direction.WEST).condition(MachineBlock.RUNNING, false).end()
                .part().modelFile(baseModelOff).rotationY(90).addModel().condition(MachineBlock.FACING, Direction.EAST).condition(MachineBlock.RUNNING, false).end()
                .part().modelFile(baseModelRunning).addModel().condition(MachineBlock.FACING, Direction.NORTH).condition(MachineBlock.RUNNING, true).end()
                .part().modelFile(baseModelRunning).rotationY(180).addModel().condition(MachineBlock.FACING, Direction.SOUTH).condition(MachineBlock.RUNNING, true).end()
                .part().modelFile(baseModelRunning).rotationY(270).addModel().condition(MachineBlock.FACING, Direction.WEST).condition(MachineBlock.RUNNING, true).end()
                .part().modelFile(baseModelRunning).rotationY(90).addModel().condition(MachineBlock.FACING, Direction.EAST).condition(MachineBlock.RUNNING, true).end();
        simpleBlockItem(block, baseModelOff);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlockWithItem(ResourceRegistry.COPPER_ORE.get());
        simpleBlockWithItem(ResourceRegistry.ALUMINIUM_ORE.get());
        machineBlockWithItem(MachineRegistry.COAL_GENERATOR.get());
    }

}
