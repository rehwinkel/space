package deerangle.space.planets.venus.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class RockBlock extends Block {

    private static final VoxelShape SHAPE = Block.makeCuboidShape(1, 0, 1, 15, 10, 15);
    private static final VoxelShape COLLISION_SHAPE = Block.makeCuboidShape(1, 0, 1, 14, 4, 14);

    public RockBlock(AbstractBlock.Properties properties) {
        super(properties); //TODO: maybe damage when walking
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader reader, BlockPos pos) {
        return COLLISION_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return COLLISION_SHAPE;
    }

}
