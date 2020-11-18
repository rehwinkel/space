package deerangle.space.block;

import deerangle.space.stats.Stats;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class CoalGeneratorBlock extends MachineBlock {

    private static final VoxelShape SHAPE = VoxelShapes.or(Block.makeCuboidShape(1, 3, 0, 15, 14, 16));

    public CoalGeneratorBlock(Properties properties) {
        super(properties, Stats.INTERACT_WITH_COAL_GENERATOR);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

}
