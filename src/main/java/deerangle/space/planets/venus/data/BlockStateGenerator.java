package deerangle.space.planets.venus.data;

import deerangle.space.main.SpaceMod;
import deerangle.space.planets.venus.VenusRegistry;
import net.minecraft.block.*;
import net.minecraft.data.DataGenerator;
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

    private void grassBlock(Block block) {
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
        grassBlock(VenusRegistry.SHRIEKGRASS.get());
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
        //TODO: vein model for bacteria, overgrown
    }

}
