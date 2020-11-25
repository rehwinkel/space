package deerangle.space.block;

import deerangle.space.tags.BlockTags;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.tags.ITag;

public class PipeBlock extends DuctBlock {

    public PipeBlock(AbstractBlock.Properties properties) {
        super(properties);
    }

    @Override
    protected ITag<Block> getMachineTag() {
        return BlockTags.FLUID_MACHINES;
    }

    @Override
    protected boolean canConnectDuct(Block block) {
        return block instanceof PipeBlock;
    }

}
