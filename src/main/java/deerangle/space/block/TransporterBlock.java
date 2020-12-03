package deerangle.space.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

public class TransporterBlock extends DuctBlock {

    public TransporterBlock(AbstractBlock.Properties properties) {
        super(properties);
    }

    @Override
    protected Capability<?> getDuctCapability() {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Override
    protected boolean canConnectDuct(Block block) {
        return block instanceof TransporterBlock;
    }

}
