package deerangle.space.planets.venus.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class AlgaeBlock extends Block {
    protected static final VoxelShape SMALL_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
    protected static final VoxelShape LARGE_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
    private static final int TRIES = 3;

    public AlgaeBlock(AbstractBlock.Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return LARGE_SHAPE;
    }

    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SMALL_SHAPE;
    }

    @Override
    public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return VoxelShapes.empty();
    }

    public VoxelShape getCollisionShape(BlockState state, IBlockReader reader, BlockPos pos) {
        return LARGE_SHAPE;
    }

    public VoxelShape getRayTraceShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        return LARGE_SHAPE;
    }

    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super
                .updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.down()).isSolidSide(worldIn, pos.down(), Direction.UP);
    }

    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        int light = worldIn.getLight(pos);
        if (light > 11) {
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
        if (light < 6) {
            for (int i = 0; i < TRIES; i++) {
                Direction growDirection = Direction.Plane.HORIZONTAL.random(rand);
                BlockPos growPos = pos.offset(growDirection);
                if (worldIn.getBlockState(growPos).isAir(worldIn, growPos)) {
                    if (this.isValidPosition(null, worldIn, growPos)) {
                        worldIn.setBlockState(growPos, this.getDefaultState());
                        break;
                    } else if (worldIn.getBlockState(growPos.down()).isAir(worldIn, growPos.down()) && this
                            .isValidPosition(null, worldIn, growPos.down())) {
                        worldIn.setBlockState(growPos.down(), this.getDefaultState());
                        break;
                    }
                } else if (worldIn.getBlockState(growPos.up()).isAir(worldIn, growPos.up()) && this
                        .isValidPosition(null, worldIn, growPos.up())) {
                    worldIn.setBlockState(growPos.up(), this.getDefaultState());
                    break;
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        worldIn.addParticle(new BlockParticleData(ParticleTypes.BLOCK, stateIn), pos.getX() + rand.nextDouble(),
                pos.getY() + 0.125D, pos.getZ() + rand.nextDouble(), 0.0D, 0.0D, 0.0D);
    }

}
