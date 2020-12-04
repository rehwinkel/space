package deerangle.space.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

public class CableBlock extends DuctBlock {

    public CableBlock(AbstractBlock.Properties properties) {
        super(properties);
    }

    @Override
    protected Capability<?> getDuctCapability() {
        return CapabilityEnergy.ENERGY;
    }

    @Override
    protected boolean canConnectDuct(Block block) {
        return block instanceof CableBlock;
    }

}
