package deerangle.space.planet.planets.mercury.data;

import deerangle.space.planet.planets.mercury.MercuryRegistry;
import net.minecraft.block.Block;
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

    private ResourceLocation mercuryBlockTexture(Block block) {
        ResourceLocation name = block.getRegistryName();
        return new ResourceLocation(name.getNamespace(), "mercury/block/" + name.getPath());
    }

    private void rotatedBlockWithItem(Block block) {
        ModelFile model = models().cubeAll(block.getRegistryName().getPath(), mercuryBlockTexture(block));
        getVariantBuilder(block).partialState().setModels(new ConfiguredModel(model, 0, 90, false), new ConfiguredModel(model, 0, 180, false), new ConfiguredModel(model, 0, 270, false), new ConfiguredModel(model, 0, 0, false));
        ResourceLocation blockName = block.getRegistryName();
        simpleBlockItem(block, models().getExistingFile(new ResourceLocation(blockName.getNamespace(), "block/" + blockName.getPath())));
    }

    @Override
    protected void registerStatesAndModels() {
        rotatedBlockWithItem(MercuryRegistry.FIRESTONE.get());
        rotatedBlockWithItem(MercuryRegistry.PLANET_CORE.get());
    }
}
