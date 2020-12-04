package deerangle.space.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class PipeBlock extends DuctBlock {

    public PipeBlock(AbstractBlock.Properties properties) {
        super(properties);
    }

    @Override
    protected Capability<?> getDuctCapability() {
        return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
    }

    @Override
    protected boolean canConnectDuct(Block block) {
        return block instanceof PipeBlock;
    }

}
