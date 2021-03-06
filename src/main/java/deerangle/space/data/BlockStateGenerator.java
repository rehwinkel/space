package deerangle.space.data;

import deerangle.space.block.DuctBlock;
import deerangle.space.block.MachineBlock;
import deerangle.space.main.SpaceMod;
import deerangle.space.registry.FluidRegistry;
import deerangle.space.registry.MachineRegistry;
import deerangle.space.registry.ResourceRegistry;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.fluid.Fluid;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStateGenerator extends BlockStateProvider {

    private static final ResourceLocation CONNECTOR_SHORT_LOC = new ResourceLocation(SpaceMod.MOD_ID, "block/connector_short");
    private static final ResourceLocation CONNECTOR_LONG_LOC = new ResourceLocation(SpaceMod.MOD_ID, "block/connector_long");
    private static final ResourceLocation CONNECTOR_LONG3_LOC = new ResourceLocation(SpaceMod.MOD_ID, "block/connector_long3");

    public BlockStateGenerator(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
        super(gen, modid, exFileHelper);
    }

    private void simpleBlockWithItem(Block block) {
        simpleBlock(block);
        ResourceLocation blockName = block.getRegistryName();
        simpleBlockItem(block, models().getExistingFile(new ResourceLocation(blockName.getNamespace(), "block/" + blockName.getPath())));
    }

    private void existingBlockWithItem(Block block) {
        ResourceLocation blockName = block.getRegistryName();
        ResourceLocation parent = new ResourceLocation(blockName.getNamespace(), "block/" + blockName.getPath());
        getVariantBuilder(block).partialState().setModels(new ConfiguredModel(models().getExistingFile(parent)));
        simpleBlockItem(block, models().getExistingFile(parent));
    }

    // connectors: north, south, east, west, up, down
    private void machineBlockWithItem(Block block, RotatedModel[] connectors, boolean hasRunningVariant) {
        ResourceLocation blockName = block.getRegistryName();
        ModelFile baseModelOff = models().getExistingFile(new ResourceLocation(blockName.getNamespace(), "block/" + blockName.getPath()));

        MultiPartBlockStateBuilder builder = getMultipartBuilder(block);
        if (hasRunningVariant) {
            ModelFile baseModelRunning = models().getExistingFile(new ResourceLocation(blockName.getNamespace(), "block/" + blockName.getPath() + "_running"));

            builder.part().modelFile(baseModelOff).addModel().condition(MachineBlock.FACING, Direction.NORTH).condition(MachineBlock.RUNNING, false).end().part().modelFile(baseModelOff).rotationY(180).addModel().condition(MachineBlock.FACING, Direction.SOUTH).condition(MachineBlock.RUNNING, false).end().part().modelFile(baseModelOff).rotationY(270).addModel().condition(MachineBlock.FACING, Direction.WEST).condition(MachineBlock.RUNNING, false).end().part().modelFile(baseModelOff).rotationY(90).addModel().condition(MachineBlock.FACING, Direction.EAST).condition(MachineBlock.RUNNING, false).end().part().modelFile(baseModelRunning).addModel().condition(MachineBlock.FACING, Direction.NORTH).condition(MachineBlock.RUNNING, true).end().part().modelFile(baseModelRunning).rotationY(180).addModel().condition(MachineBlock.FACING, Direction.SOUTH).condition(MachineBlock.RUNNING, true).end().part().modelFile(baseModelRunning).rotationY(270).addModel()
                    .condition(MachineBlock.FACING, Direction.WEST).condition(MachineBlock.RUNNING, true).end().part().modelFile(baseModelRunning).rotationY(90).addModel().condition(MachineBlock.FACING, Direction.EAST).condition(MachineBlock.RUNNING, true).end();
        } else {
            builder.part().modelFile(baseModelOff).addModel().condition(MachineBlock.FACING, Direction.NORTH).end().part().modelFile(baseModelOff).rotationY(180).addModel().condition(MachineBlock.FACING, Direction.SOUTH).end().part().modelFile(baseModelOff).rotationY(270).addModel().condition(MachineBlock.FACING, Direction.WEST).end().part().modelFile(baseModelOff).rotationY(90).addModel().condition(MachineBlock.FACING, Direction.EAST).end();
        }
        this.addConnector(builder, connectors[0], MachineBlock.NORTH);
        this.addConnector(builder, connectors[1], MachineBlock.SOUTH);
        this.addConnector(builder, connectors[2], MachineBlock.EAST);
        this.addConnector(builder, connectors[3], MachineBlock.WEST);
        if (connectors[4] != null) {
            builder.part().modelFile(connectors[4].model).rotationX(connectors[4].x).rotationY(connectors[4].y).addModel().condition(MachineBlock.UP, true).end();
        }
        if (connectors[5] != null) {
            builder.part().modelFile(connectors[5].model).rotationX(connectors[5].x).rotationY(connectors[5].y).addModel().condition(MachineBlock.DOWN, true).end();
        }
        simpleBlockItem(block, baseModelOff);
    }

    private void cableBlockWithItem(Block block) {
        ResourceLocation blockName = block.getRegistryName();
        ModelFile baseModel = models().getExistingFile(new ResourceLocation(blockName.getNamespace(), "block/" + blockName.getPath()));
        ModelFile connector = models().getExistingFile(new ResourceLocation(blockName.getNamespace(), "block/" + blockName.getPath() + "_connector"));
        ModelFile connectorSelf = models().getExistingFile(new ResourceLocation(blockName.getNamespace(), "block/" + blockName.getPath() + "_connector_self"));

        MultiPartBlockStateBuilder builder = getMultipartBuilder(block);
        builder.part().modelFile(baseModel).addModel().end().part().modelFile(connector).rotationY(0).addModel().condition(DuctBlock.NORTH, DuctBlock.Connection.OTHER).end().part().modelFile(connector).rotationY(180).addModel().condition(DuctBlock.SOUTH, DuctBlock.Connection.OTHER).end().part().modelFile(connector).rotationY(90).addModel().condition(DuctBlock.EAST, DuctBlock.Connection.OTHER).end().part().modelFile(connector).rotationY(270).addModel().condition(DuctBlock.WEST, DuctBlock.Connection.OTHER).end().part().modelFile(connector).rotationX(270).addModel().condition(DuctBlock.UP, DuctBlock.Connection.OTHER).end().part().modelFile(connector).rotationX(90).addModel().condition(DuctBlock.DOWN, DuctBlock.Connection.OTHER).end().part().modelFile(connectorSelf).rotationY(0).addModel().condition(DuctBlock.NORTH, DuctBlock.Connection.SELF).end().part().modelFile(connectorSelf).rotationY(180).addModel().condition(DuctBlock.SOUTH, DuctBlock.Connection.SELF).end().part()
                .modelFile(connectorSelf).rotationY(90).addModel().condition(DuctBlock.EAST, DuctBlock.Connection.SELF).end().part().modelFile(connectorSelf).rotationY(270).addModel().condition(DuctBlock.WEST, DuctBlock.Connection.SELF).end().part().modelFile(connectorSelf).rotationX(270).addModel().condition(DuctBlock.UP, DuctBlock.Connection.SELF).end().part().modelFile(connectorSelf).rotationX(90).addModel().condition(DuctBlock.DOWN, DuctBlock.Connection.SELF).end();

        ModelFile inventoryModel = models().getExistingFile(new ResourceLocation(blockName.getNamespace(), "block/" + blockName.getPath() + "_inventory"));
        simpleBlockItem(block, inventoryModel);
    }

    private void addConnector(MultiPartBlockStateBuilder builder, RotatedModel connector, BooleanProperty direction) {
        if (connector != null) {
            builder.part().modelFile(connector.model).rotationX(connector.x).rotationY(connector.y).addModel().condition(direction, true).condition(MachineBlock.FACING, Direction.NORTH).end();
            builder.part().modelFile(connector.model).rotationX(connector.x).rotationY(connector.y + 180).addModel().condition(direction, true).condition(MachineBlock.FACING, Direction.SOUTH).end();
            builder.part().modelFile(connector.model).rotationX(connector.x).rotationY(connector.y + 90).addModel().condition(direction, true).condition(MachineBlock.FACING, Direction.EAST).end();
            builder.part().modelFile(connector.model).rotationX(connector.x).rotationY(connector.y + 270).addModel().condition(direction, true).condition(MachineBlock.FACING, Direction.WEST).end();
        }
    }

    private void fluidBlock(Fluid fluid) {
        Block block = fluid.getDefaultState().getBlockState().getBlock();
        getVariantBuilder(block).partialState().setModels(new ConfiguredModel(models().getBuilder(blockName(block)).texture("particle", new ResourceLocation(SpaceMod.MOD_ID, "block/" + block.getRegistryName().getPath() + "_still"))));
    }

    private void blockWithTopAndBottomTexture(Block block) {
        ResourceLocation base = blockTexture(block);
        ModelFile baseModel = models().cubeBottomTop(block.getRegistryName().getPath(), new ResourceLocation(base.getNamespace(), base.getPath() + "_side"), new ResourceLocation(base.getNamespace(), base.getPath() + "_bottom"), new ResourceLocation(base.getNamespace(), base.getPath() + "_top"));
        getVariantBuilder(block).partialState().setModels(new ConfiguredModel(baseModel));
        simpleBlockItem(block, baseModel);
    }

    private String blockName(Block block) {
        return block.getRegistryName().getNamespace() + ":block/" + block.getRegistryName().getPath();
    }

    @Override
    protected void registerStatesAndModels() {
        ModelFile connectorShort = models().getExistingFile(CONNECTOR_SHORT_LOC);
        ModelFile connectorLong = models().getExistingFile(CONNECTOR_LONG_LOC);
        ModelFile connectorLong3 = models().getExistingFile(CONNECTOR_LONG3_LOC);
        simpleBlockWithItem(ResourceRegistry.COPPER_ORE.get());
        simpleBlockWithItem(ResourceRegistry.ALUMINIUM_ORE.get());
        simpleBlockWithItem(ResourceRegistry.ILMENITE_ORE.get());
        simpleBlockWithItem(ResourceRegistry.ALUMINIUM_BLOCK.get());
        simpleBlockWithItem(ResourceRegistry.COPPER_BLOCK.get());
        simpleBlockWithItem(ResourceRegistry.STEEL_BLOCK.get());
        simpleBlockWithItem(ResourceRegistry.TITANIUM_BLOCK.get());
        simpleBlockWithItem(ResourceRegistry.QUARTZ_SAND.get());
        blockWithTopAndBottomTexture(ResourceRegistry.CONTROLLER.get());
        fluidBlock(FluidRegistry.CRUDE_OIL.get());
        fluidBlock(FluidRegistry.KEROSENE.get());
        fluidBlock(FluidRegistry.ACID.get());
        cableBlockWithItem(MachineRegistry.CABLE.get());
        cableBlockWithItem(MachineRegistry.TRANSPORTER.get());
        cableBlockWithItem(MachineRegistry.PIPE.get());
        machineBlockWithItem(MachineRegistry.COAL_GENERATOR.get(), new RotatedModel[]{null, null, new RotatedModel(connectorShort, 0, 90), new RotatedModel(connectorShort, 0, 270), new RotatedModel(connectorLong, 270, 0), null}, true);
        machineBlockWithItem(MachineRegistry.BLAST_FURNACE.get(), new RotatedModel[]{null, new RotatedModel(connectorLong3, 0, 180), new RotatedModel(connectorShort, 0, 90), new RotatedModel(connectorShort, 0, 270), null, null}, true);
        machineBlockWithItem(MachineRegistry.COMBUSTION_GENERATOR.get(), new RotatedModel[]{null, new RotatedModel(connectorShort, 0, 180), null, null, null, null}, false);
        machineBlockWithItem(MachineRegistry.GAS_TANK.get(), new RotatedModel[]{new RotatedModel(connectorLong3, 0, 0), new RotatedModel(connectorLong3, 0, 180), new RotatedModel(connectorLong3, 0, 90), new RotatedModel(connectorLong3, 0, 270), new RotatedModel(connectorShort, 270, 0), new RotatedModel(connectorShort, 90, 0)}, false);
        machineBlockWithItem(MachineRegistry.DRUM.get(), new RotatedModel[]{null, null, null, null, new RotatedModel(connectorShort, 270, 0), null}, false);
        machineBlockWithItem(MachineRegistry.BATTERY_PACK.get(), new RotatedModel[]{new RotatedModel(connectorShort, 0, 0), new RotatedModel(connectorShort, 0, 180), new RotatedModel(connectorShort, 0, 90), new RotatedModel(connectorShort, 0, 270), null, null}, false);
        machineBlockWithItem(MachineRegistry.REFINERY.get(), new RotatedModel[]{new RotatedModel(models().getExistingFile(new ResourceLocation(SpaceMod.MOD_ID, "block/refinery_connector_north")), 0, 0), new RotatedModel(models().getExistingFile(new ResourceLocation(SpaceMod.MOD_ID, "block/refinery_connector_south")), 0, 0), null, null, new RotatedModel(models().getExistingFile(new ResourceLocation(SpaceMod.MOD_ID, "block/refinery_connector_top")), 0, 0), null}, false);
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

}
