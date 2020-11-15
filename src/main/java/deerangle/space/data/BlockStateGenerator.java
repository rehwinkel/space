package deerangle.space.data;

import deerangle.space.registry.ResourceRegistry;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStateGenerator extends BlockStateProvider {

    private final String mod_id;

    public BlockStateGenerator(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
        super(gen, modid, exFileHelper);
        this.mod_id = modid;
    }

    private void simpleBlockWithItem(Block block) {
        simpleBlock(block);
        ResourceLocation blockName = block.getRegistryName();
        simpleBlockItem(block, models().getExistingFile(
                new ResourceLocation(blockName.getNamespace(), "block/" + blockName.getPath())));
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlockWithItem(ResourceRegistry.COPPER_ORE.get());
        simpleBlockWithItem(ResourceRegistry.ALUMINIUM_ORE.get());
    }

}
