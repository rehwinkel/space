package deerangle.space.data;

import deerangle.space.block.MachineBlock;
import deerangle.space.main.SpaceMod;
import deerangle.space.registry.MachineRegistry;
import deerangle.space.registry.ResourceRegistry;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStateGenerator extends BlockStateProvider {

    private static final ResourceLocation CONNECTOR_LOC = new ResourceLocation(SpaceMod.MOD_ID, "block/connector");
    private static final ResourceLocation CONNECTOR_SHORT_LOC = new ResourceLocation(SpaceMod.MOD_ID,
            "block/connector_short");
    private static final ResourceLocation CONNECTOR_LONG_LOC = new ResourceLocation(SpaceMod.MOD_ID,
            "block/connector_long");

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

    private class RotatedModel {

        private final ModelFile model;
        private final int x;
        private final int y;

        public RotatedModel(ModelFile model, int x, int y) {
            this.model = model;
            this.x = x;
            this.y = y;
        }

    }

    // connectors: north, south, east, west, up, down
    private void machineBlockWithItem(Block block, RotatedModel[] connectors) {
        ResourceLocation blockName = block.getRegistryName();
        ModelFile baseModelOff = models()
                .getExistingFile(new ResourceLocation(blockName.getNamespace(), "block/" + blockName.getPath()));
        ModelFile baseModelRunning = models().getExistingFile(
                new ResourceLocation(blockName.getNamespace(), "block/" + blockName.getPath() + "_running"));

        MultiPartBlockStateBuilder builder = getMultipartBuilder(block).part().modelFile(baseModelOff).addModel()
                .condition(MachineBlock.FACING, Direction.NORTH).condition(MachineBlock.RUNNING, false).end().part()
                .modelFile(baseModelOff).rotationY(180).addModel().condition(MachineBlock.FACING, Direction.SOUTH)
                .condition(MachineBlock.RUNNING, false).end().part().modelFile(baseModelOff).rotationY(270).addModel()
                .condition(MachineBlock.FACING, Direction.WEST).condition(MachineBlock.RUNNING, false).end().part()
                .modelFile(baseModelOff).rotationY(90).addModel().condition(MachineBlock.FACING, Direction.EAST)
                .condition(MachineBlock.RUNNING, false).end().part().modelFile(baseModelRunning).addModel()
                .condition(MachineBlock.FACING, Direction.NORTH).condition(MachineBlock.RUNNING, true).end().part()
                .modelFile(baseModelRunning).rotationY(180).addModel().condition(MachineBlock.FACING, Direction.SOUTH)
                .condition(MachineBlock.RUNNING, true).end().part().modelFile(baseModelRunning).rotationY(270)
                .addModel().condition(MachineBlock.FACING, Direction.WEST).condition(MachineBlock.RUNNING, true).end()
                .part().modelFile(baseModelRunning).rotationY(90).addModel()
                .condition(MachineBlock.FACING, Direction.EAST).condition(MachineBlock.RUNNING, true).end();
        this.addConnector(builder, connectors[0], MachineBlock.NORTH);
        this.addConnector(builder, connectors[1], MachineBlock.SOUTH);
        this.addConnector(builder, connectors[2], MachineBlock.EAST);
        this.addConnector(builder, connectors[3], MachineBlock.WEST);
        builder.part().modelFile(connectors[4].model).rotationX(connectors[4].x).rotationY(connectors[4].y).addModel()
                .condition(MachineBlock.UP, true).end().part().modelFile(connectors[5].model).rotationX(connectors[5].x)
                .rotationY(connectors[5].y).addModel().condition(MachineBlock.DOWN, true).end();
        simpleBlockItem(block, baseModelOff);
    }

    private void addConnector(MultiPartBlockStateBuilder builder, RotatedModel connector, BooleanProperty direction) {
        if (connector != null) {
            builder.part().modelFile(connector.model).rotationX(connector.x).rotationY(connector.y).addModel()
                    .condition(direction, true).condition(MachineBlock.FACING, Direction.NORTH).end();
            builder.part().modelFile(connector.model).rotationX(connector.x).rotationY(connector.y + 180).addModel()
                    .condition(direction, true).condition(MachineBlock.FACING, Direction.SOUTH).end();
            builder.part().modelFile(connector.model).rotationX(connector.x).rotationY(connector.y + 90).addModel()
                    .condition(direction, true).condition(MachineBlock.FACING, Direction.EAST).end();
            builder.part().modelFile(connector.model).rotationX(connector.x).rotationY(connector.y + 270).addModel()
                    .condition(direction, true).condition(MachineBlock.FACING, Direction.WEST).end();
        }
    }

    @Override
    protected void registerStatesAndModels() {
        ModelFile connector = models().getExistingFile(CONNECTOR_LOC);
        ModelFile connectorShort = models().getExistingFile(CONNECTOR_SHORT_LOC);
        ModelFile connectorLong = models().getExistingFile(CONNECTOR_LONG_LOC);
        simpleBlockWithItem(ResourceRegistry.COPPER_ORE.get());
        simpleBlockWithItem(ResourceRegistry.ALUMINIUM_ORE.get());
        machineBlockWithItem(MachineRegistry.COAL_GENERATOR.get(),
                new RotatedModel[]{null, new RotatedModel(connector, 0, 180), new RotatedModel(connectorShort, 0,
                        90), new RotatedModel(connectorShort, 0, 270), new RotatedModel(connectorLong, 90,
                        0), new RotatedModel(connector, 270, 0)});
    }

}
