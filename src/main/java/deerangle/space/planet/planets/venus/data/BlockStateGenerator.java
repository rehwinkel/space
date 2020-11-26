package deerangle.space.planet.planets.venus.data;

import deerangle.space.main.SpaceMod;
import deerangle.space.planet.planets.venus.VenusRegistry;
import deerangle.space.planet.planets.venus.block.CrystalBlock;
import net.minecraft.block.*;
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

    private void basicBlock(Block block) {
        getVariantBuilder(block).partialState().setModels(
                new ConfiguredModel(models().cubeAll(block.getRegistryName().getPath(), venusBlockTexture(block))));
    }

    private void rotatedBlock(Block block) {
        this.rotatedBlock(block, models().cubeAll(block.getRegistryName().getPath(), venusBlockTexture(block)));
    }

    private void rotatedBlock(Block block, ModelFile baseModel) {
        getVariantBuilder(block).partialState()
                .setModels(new ConfiguredModel(baseModel, 0, 0, false), new ConfiguredModel(baseModel, 0, 90, false),
                        new ConfiguredModel(baseModel, 0, 180, false), new ConfiguredModel(baseModel, 0, 270, false));
    }

    private ResourceLocation venusBlockTexture(Block block) {
        ResourceLocation name = block.getRegistryName();
        return new ResourceLocation(name.getNamespace(), "venus/block/" + name.getPath());
    }

    private void algaeBlock(Block block) {
        getVariantBuilder(block).partialState().setModels(new ConfiguredModel(
                models().singleTexture(block.getRegistryName().getPath(),
                        new ResourceLocation("space", "venus/block/slimy_algae"), "algae", venusBlockTexture(block))));
    }

    private void rockBlock(Block block, ResourceLocation texture) {
        ModelFile firstModel = models().singleTexture(block.getRegistryName().getPath(),
                new ResourceLocation(SpaceMod.MOD_ID, "venus/block/rock"), "all", texture);
        ModelFile secondModel = models().singleTexture(block.getRegistryName().getPath() + "_secondary",
                new ResourceLocation(SpaceMod.MOD_ID, "venus/block/rock_secondary"), "all", texture);
        getVariantBuilder(block).partialState()
                .setModels(new ConfiguredModel(firstModel, 0, 0, false), new ConfiguredModel(firstModel, 0, 90, false),
                        new ConfiguredModel(secondModel, 0, 0, false), new ConfiguredModel(secondModel, 0, 90, false));
    }

    private void venusLogBlock(Block block) {
        ResourceLocation base = venusBlockTexture(block);
        axisBlock((RotatedPillarBlock) block, base, new ResourceLocation(base.getNamespace(), base.getPath() + "_top"));
    }

    private void venusWoodBlock(Block block, ResourceLocation base) {
        axisBlock((RotatedPillarBlock) block, base, base);
    }

    private void venusSlabBlock(Block block, Block source) {
        slabBlock((SlabBlock) block, source.getRegistryName(), venusBlockTexture(source));
    }

    private void venusStairsBlock(Block block, ResourceLocation texture) {
        String baseName = block.getRegistryName().toString();
        ModelFile stairs = models().stairs(baseName, texture, texture, texture);
        ModelFile stairsInner = models().stairsInner(baseName + "_inner", texture, texture, texture);
        ModelFile stairsOuter = models().stairsOuter(baseName + "_outer", texture, texture, texture);
        stairsBlock((StairsBlock) block, stairs, stairsInner, stairsOuter);
    }

    private void basicBlockItem(Block block) {
        ResourceLocation blockLoc = block.getRegistryName();
        itemModels().getBuilder(blockLoc.getPath()).parent(models()
                .getExistingFile(new ResourceLocation(blockLoc.getNamespace(), "block/" + blockLoc.getPath())));
    }

    private void crossBlock(Block block) {
        getVariantBuilder(block).partialState().setModels(
                new ConfiguredModel(models().cross(block.getRegistryName().getPath(), venusBlockTexture(block))));
    }

    private void overgrownBlock(Block block, ResourceLocation bottomTexture) {
        ResourceLocation base = venusBlockTexture(block);
        ModelFile baseModel = models().cubeBottomTop(block.getRegistryName().getPath(), base, bottomTexture,
                new ResourceLocation(base.getNamespace(), base.getPath() + "_top"));
        rotatedBlock(block, baseModel);
    }

    @Override
    protected void registerStatesAndModels() {
        basicBlock(VenusRegistry.PULCHERITE.get());
        basicBlockItem(VenusRegistry.PULCHERITE.get());
        rotatedBlock(VenusRegistry.PULCHERITE_TURF.get());
        basicBlockItem(VenusRegistry.PULCHERITE_TURF.get());
        basicBlock(VenusRegistry.PULCHERITE_COAL.get());
        basicBlockItem(VenusRegistry.PULCHERITE_COAL.get());
        basicBlock(VenusRegistry.PULCHERITE_SULFUR.get());
        basicBlockItem(VenusRegistry.PULCHERITE_SULFUR.get());
        basicBlock(VenusRegistry.PULCHERITE_ILMENITE.get());
        basicBlockItem(VenusRegistry.PULCHERITE_ILMENITE.get());
        basicBlock(VenusRegistry.PULCHERITE_BRICKS.get());
        basicBlockItem(VenusRegistry.PULCHERITE_BRICKS.get());
        basicBlock(VenusRegistry.POLISHED_PULCHERITE.get());
        basicBlockItem(VenusRegistry.POLISHED_PULCHERITE.get());
        rotatedBlock(VenusRegistry.TURPIUM.get());
        basicBlockItem(VenusRegistry.TURPIUM.get());
        rotatedBlock(VenusRegistry.GLOWING_TURPIUM.get());
        basicBlockItem(VenusRegistry.GLOWING_TURPIUM.get());
        basicBlock(VenusRegistry.TURPIUM_COBBLESTONE.get());
        basicBlockItem(VenusRegistry.TURPIUM_COBBLESTONE.get());
        basicBlock(VenusRegistry.SHRIEKWOOD_PLANKS.get());
        basicBlockItem(VenusRegistry.SHRIEKWOOD_PLANKS.get());
        basicBlock(VenusRegistry.SHRIEKWOOD_LEAVES.get());
        basicBlockItem(VenusRegistry.SHRIEKWOOD_LEAVES.get());
        crossBlock(VenusRegistry.SHRIEKGRASS.get());
        crossBlock(VenusRegistry.SHRIEKWOOD_SAPLING.get());
        directionalCrossBlock(VenusRegistry.CRYSTAL.get());
        basicBlock(VenusRegistry.CRYSTAL_BLOCK.get());
        basicBlockItem(VenusRegistry.CRYSTAL_BLOCK.get());
        vineBlock(VenusRegistry.VENUS_BACTERIA.get(), venusBlockTexture(VenusRegistry.VENUS_BACTERIA.get()));
        overgrownBlock(VenusRegistry.OVERGROWN_PULCHERITE.get(), venusBlockTexture(VenusRegistry.PULCHERITE.get()));
        basicBlockItem(VenusRegistry.OVERGROWN_PULCHERITE.get());
        venusStairsBlock(VenusRegistry.SHRIEKWOOD_STAIRS.get(),
                venusBlockTexture(VenusRegistry.SHRIEKWOOD_PLANKS.get()));
        basicBlockItem(VenusRegistry.SHRIEKWOOD_STAIRS.get());
        venusSlabBlock(VenusRegistry.SHRIEKWOOD_SLAB.get(), VenusRegistry.SHRIEKWOOD_PLANKS.get());
        basicBlockItem(VenusRegistry.SHRIEKWOOD_SLAB.get());
        doorBlock((DoorBlock) VenusRegistry.SHRIEKWOOD_DOOR.get(),
                new ResourceLocation(SpaceMod.MOD_ID, "venus/block/shriekwood_door_bottom"),
                new ResourceLocation(SpaceMod.MOD_ID, "venus/block/shriekwood_door_top"));
        venusLogBlock(VenusRegistry.SHRIEKWOOD_LOG.get());
        basicBlockItem(VenusRegistry.SHRIEKWOOD_LOG.get());
        venusWoodBlock(VenusRegistry.SHRIEKWOOD_WOOD.get(), venusBlockTexture(VenusRegistry.SHRIEKWOOD_LOG.get()));
        basicBlockItem(VenusRegistry.SHRIEKWOOD_WOOD.get());
        rockBlock(VenusRegistry.TURPIUM_ROCK.get(), venusBlockTexture(VenusRegistry.TURPIUM.get()));
        basicBlockItem(VenusRegistry.TURPIUM_ROCK.get());
        algaeBlock(VenusRegistry.SLIMY_ALGAE.get());
    }

    private void directionalCrossBlock(Block block) {
        ModelFile model = models().cross(block.getRegistryName().getPath(), venusBlockTexture(block));
        getVariantBuilder(block).partialState().with(CrystalBlock.FACING, Direction.UP).modelForState().modelFile(model)
                .addModel().partialState().with(CrystalBlock.FACING, Direction.DOWN).modelForState().modelFile(model)
                .rotationX(180).addModel().partialState().with(CrystalBlock.FACING, Direction.EAST).modelForState()
                .modelFile(model).rotationX(90).rotationY(90).addModel().partialState()
                .with(CrystalBlock.FACING, Direction.WEST).modelForState().modelFile(model).rotationX(90).rotationY(270)
                .addModel().partialState().with(CrystalBlock.FACING, Direction.NORTH).modelForState().modelFile(model)
                .rotationX(90).addModel().partialState().with(CrystalBlock.FACING, Direction.SOUTH).modelForState()
                .modelFile(model).rotationX(90).rotationY(180).addModel();
    }

    private void vineBlock(Block block, ResourceLocation texture) {
        ResourceLocation base = block.getRegistryName();
        getVariantBuilder(block).forAllStates(blockState -> {
            boolean up = blockState.get(VineBlock.UP);
            boolean opposite = blockState.get(VineBlock.EAST) == blockState.get(VineBlock.WEST) && blockState
                    .get(VineBlock.NORTH) == blockState.get(VineBlock.SOUTH) && blockState
                    .get(VineBlock.EAST) != blockState.get(VineBlock.NORTH);
            int i = (blockState.get(VineBlock.WEST) ? 1 : 0) + (blockState.get(VineBlock.EAST) ? 1 : 0) + (blockState
                    .get(VineBlock.NORTH) ? 1 : 0) + (blockState.get(VineBlock.SOUTH) ? 1 : 0);
            String appendix = "_" + i + (up ? "u" : "") + ((opposite && i != 4) ? "_opposite" : "");
            if (i == 0) {
                if (up) {
                    appendix = "_u";
                } else {
                    appendix = "_1";
                }
            }
            ResourceLocation loc = new ResourceLocation(base.getNamespace(), base.getPath() + appendix);
            ResourceLocation parent = new ResourceLocation(SpaceMod.MOD_ID, "block/vinelike" + appendix);
            int yRot = 0;
            if (i == 1) {
                if (blockState.get(VineBlock.NORTH)) {
                    yRot = 180;
                } else if (blockState.get(VineBlock.EAST)) {
                    yRot = 270;
                } else if (blockState.get(VineBlock.WEST)) {
                    yRot = 90;
                }
            } else if (i == 2) {
                if (blockState.get(VineBlock.NORTH) == blockState.get(VineBlock.SOUTH) && blockState
                        .get(VineBlock.NORTH)) {
                    yRot = 90;
                } else if (blockState.get(VineBlock.SOUTH) == blockState.get(VineBlock.EAST) && blockState
                        .get(VineBlock.SOUTH)) {
                    yRot = 90;
                } else if (blockState.get(VineBlock.NORTH) == blockState.get(VineBlock.WEST) && blockState
                        .get(VineBlock.NORTH)) {
                    yRot = 270;
                } else if (blockState.get(VineBlock.SOUTH) == blockState.get(VineBlock.WEST) && blockState
                        .get(VineBlock.SOUTH)) {
                    yRot = 180;
                }
            } else if (i == 3) {
                if (!blockState.get(VineBlock.NORTH)) {
                    yRot = 90;
                } else if (!blockState.get(VineBlock.SOUTH)) {
                    yRot = 270;
                } else if (!blockState.get(VineBlock.EAST)) {
                    yRot = 180;
                }
            }
            return new ConfiguredModel[]{new ConfiguredModel(
                    models().singleTexture(loc.getPath(), parent, "texture", texture), 0, yRot, false)};
        });
    }

}
