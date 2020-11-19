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

    private void facingBlockWithItem(Block block) {
        ResourceLocation blockName = block.getRegistryName();
        ResourceLocation parent = new ResourceLocation(blockName.getNamespace(), "block/" + blockName.getPath());
        getVariantBuilder(block).partialState().with(MachineBlock.FACING, Direction.NORTH).modelForState()
                .modelFile(models().getExistingFile(parent)).addModel().partialState()
                .with(MachineBlock.FACING, Direction.SOUTH).modelForState().rotationY(180)
                .modelFile(models().getExistingFile(parent)).addModel().partialState()
                .with(MachineBlock.FACING, Direction.EAST).modelForState().rotationY(90)
                .modelFile(models().getExistingFile(parent)).addModel().partialState()
                .with(MachineBlock.FACING, Direction.WEST).modelForState().rotationY(270)
                .modelFile(models().getExistingFile(parent)).addModel();
        simpleBlockItem(block, models().getExistingFile(parent));
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlockWithItem(ResourceRegistry.COPPER_ORE.get());
        simpleBlockWithItem(ResourceRegistry.ALUMINIUM_ORE.get());
        facingBlockWithItem(MachineRegistry.COAL_GENERATOR.get());
    }

}
