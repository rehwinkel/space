package deerangle.space.block;

import deerangle.space.registry.MachineTypeRegistry;
import deerangle.space.stats.Stats;
import deerangle.space.util.VoxelShapeUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;

public class BlastFurnaceBlock extends MachineBlock {

    private static final VoxelShape[] SHAPE = VoxelShapeUtil
            .horizontalShape(Block.makeCuboidShape(1, 0, 1, 15, 9, 13), Block.makeCuboidShape(3, 9, 2, 13, 16, 12),
                    Block.makeCuboidShape(5, 16, 4, 11, 28, 10), Block.makeCuboidShape(2, 0, 13, 4, 8, 16),
                    Block.makeCuboidShape(4, 10, 12, 6, 17, 15), Block.makeCuboidShape(11, 21, 6, 14, 32, 8));

    private static final VoxelShape[] TOP_SHAPE = VoxelShapeUtil
            .horizontalShape(-1.0, Block.makeCuboidShape(1, 0, 1, 15, 9, 13),
                    Block.makeCuboidShape(3, 9, 2, 13, 16, 12), Block.makeCuboidShape(5, 16, 4, 11, 28, 10),
                    Block.makeCuboidShape(2, 0, 13, 4, 8, 16), Block.makeCuboidShape(4, 10, 12, 6, 17, 15),
                    Block.makeCuboidShape(11, 21, 6, 14, 32, 8));

    //TODO: blocking faces
    public static final BooleanProperty TOP_HALF = BooleanProperty.create("top_half");

    public BlastFurnaceBlock(Properties properties) {
        super(properties, () -> MachineTypeRegistry.BLAST_FURNACE, Stats.INTERACT_WITH_BLAST_FURNACE);
        this.setDefaultState(
                this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(RUNNING, false).with(UP, false)
                        .with(NORTH, false).with(SOUTH, false).with(EAST, false).with(WEST, false).with(DOWN, false)
                        .with(TOP_HALF, false));
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        worldIn.setBlockState(pos.up(), state.with(TOP_HALF, true), 3);
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        if (state.get(TOP_HALF)) {
            worldIn.setBlockState(pos.down(), Blocks.AIR.getDefaultState(), 35);
        } else {
            worldIn.setBlockState(pos.up(), Blocks.AIR.getDefaultState(), 35);
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(TOP_HALF);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return state.get(TOP_HALF) ? BlockRenderType.INVISIBLE : super.getRenderType(state);
    }

    @Override
    protected VoxelShape[] getMachineShape(BlockState state) {
        if (state.get(TOP_HALF)) {
            return TOP_SHAPE;
        } else {
            return SHAPE;
        }
    }

}
