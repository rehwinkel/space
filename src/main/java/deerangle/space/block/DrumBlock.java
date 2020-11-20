package deerangle.space.block;

import deerangle.space.registry.MachineTypeRegistry;
import deerangle.space.stats.Stats;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class DrumBlock extends MachineBlock {

    private static final VoxelShape SHAPE = Block.makeCuboidShape(0, 0, 0, 16, 16, 16);

    public DrumBlock(AbstractBlock.Properties properties) {
        super(properties, () -> MachineTypeRegistry.DRUM, Stats.INTERACT_WITH_DRUM);
    }

    @Override
    protected VoxelShape[] getMachineShape(BlockState state) {
        return null;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return VoxelShapes.empty();
    }
}
