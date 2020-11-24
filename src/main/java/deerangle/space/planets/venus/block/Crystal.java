package deerangle.space.planets.venus.block;

import deerangle.space.planets.venus.VenusRegistry;
import deerangle.space.planets.venus.tags.BlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

public class Crystal extends Block {
    private static final VoxelShape SHAPE = Block.makeCuboidShape(1, 0, 1, 15, 13, 15);

    public Crystal(Properties properties) {
        super(properties);
    }

    protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return state.isIn(BlockTags.CRYSTAL_GROUND);
    }

    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (worldIn.getBlockState(currentPos.north()).isIn(VenusRegistry.CRYSTAL_BLOCK.get())) {
            //TODO: rotate
        }
        return stateIn;
    }
  
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        //TODO: use facing direction
        return isValidGround(worldIn.getBlockState(pos.down()), worldIn, pos.down()) || 
                isValidGround(worldIn.getBlockState(pos.east()), worldIn, pos.east()) ||
                isValidGround(worldIn.getBlockState(pos.west()), worldIn, pos.west()) ||
                isValidGround(worldIn.getBlockState(pos.up()), worldIn, pos.up()) ||
                isValidGround(worldIn.getBlockState(pos.north()), worldIn, pos.north()) ||
                isValidGround(worldIn.getBlockState(pos.south()), worldIn, pos.south());
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
