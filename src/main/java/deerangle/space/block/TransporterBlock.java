package deerangle.space.block;

import deerangle.space.tags.BlockTags;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.tags.ITag;

public class TransporterBlock extends DuctBlock {

    public TransporterBlock(AbstractBlock.Properties properties) {
        super(properties);
    }

    @Override
    protected ITag<Block> getMachineTag() {
        return BlockTags.ITEM_MACHINES;
    }

    @Override
    protected boolean canConnectDuct(Block block) {
        return block instanceof TransporterBlock;
    }

}
