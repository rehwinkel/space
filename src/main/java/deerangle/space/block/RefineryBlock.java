package deerangle.space.block;

import deerangle.space.registry.MachineTypeRegistry;
import deerangle.space.stats.Stats;
import deerangle.space.util.VoxelShapeUtil;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.shapes.VoxelShape;

public class RefineryBlock extends MachineBlock {

    private static final VoxelShape[] SHAPE = VoxelShapeUtil.horizontalShape(Block.makeCuboidShape(1, 2, 1, 15, 4, 5), Block.makeCuboidShape(13, 4, 2, 14, 11, 5), Block.makeCuboidShape(0, 0, 4, 16, 1, 16), Block.makeCuboidShape(0, 1, 5, 16, 14, 11), Block.makeCuboidShape(12, 1, 11, 15, 5, 15), Block.makeCuboidShape(1, 1, 11, 4, 12, 15), Block.makeCuboidShape(1, 1, 12, 4, 16, 15));

    public RefineryBlock(AbstractBlock.Properties properties) {
        super(properties, () -> MachineTypeRegistry.REFINERY, Stats.INTERACT_WITH_REFINERY);
    }

    @Override
    protected VoxelShape[] getMachineShape(BlockState state) {
        return SHAPE;
    }

}
