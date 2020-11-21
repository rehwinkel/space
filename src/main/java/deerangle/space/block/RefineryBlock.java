package deerangle.space.block;

import deerangle.space.registry.MachineTypeRegistry;
import deerangle.space.stats.Stats;
import deerangle.space.util.VoxelShapeUtil;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.shapes.VoxelShape;

public class RefineryBlock extends MachineBlock {

    //TODO: fix shape
    private static final VoxelShape[] SHAPE = VoxelShapeUtil
            .horizontalShape(Block.makeCuboidShape(1, 2, 1, 13, 4, 5), Block.makeCuboidShape(13, 2, 1, 15, 11, 5),
                    Block.makeCuboidShape(0, 0, 4, 16, 14, 7), Block.makeCuboidShape(1, 0, 11, 3, 16, 4));

    public RefineryBlock(AbstractBlock.Properties properties) {
        super(properties, () -> MachineTypeRegistry.REFINERY, Stats.INTERACT_WITH_REFINERY);
    }

    @Override
    protected VoxelShape[] getMachineShape(BlockState state) {
        return SHAPE;
    }

}
