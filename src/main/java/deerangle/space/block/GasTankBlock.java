package deerangle.space.block;

import deerangle.space.registry.MachineTypeRegistry;
import deerangle.space.stats.Stats;
import deerangle.space.util.VoxelShapeUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.shapes.VoxelShape;

public class GasTankBlock extends MachineBlock {

    private static final VoxelShape[] SHAPE = VoxelShapeUtil.horizontalShape(Block.makeCuboidShape(3, 0, 3, 13, 15, 13), Block.makeCuboidShape(1, 11, 5, 15, 14, 11));

    public GasTankBlock(Properties properties) {
        super(properties, () -> MachineTypeRegistry.GAS_TANK, Stats.INTERACT_WITH_GAS_TANK);
    }

    @Override
    protected VoxelShape[] getMachineShape(BlockState state) {
        return SHAPE;
    }

}
