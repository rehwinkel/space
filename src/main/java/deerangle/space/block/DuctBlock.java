package deerangle.space.block;

import deerangle.space.block.entity.MachineTileEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.ITag;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import java.util.Map;

public abstract class DuctBlock extends Block {

    public static final BooleanProperty NORTH = SixWayBlock.NORTH;
    public static final BooleanProperty EAST = SixWayBlock.EAST;
    public static final BooleanProperty SOUTH = SixWayBlock.SOUTH;
    public static final BooleanProperty WEST = SixWayBlock.WEST;
    public static final BooleanProperty UP = SixWayBlock.UP;
    public static final BooleanProperty DOWN = SixWayBlock.DOWN;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    // TODO: connect transporters to vanilla and other mods

    protected static final Map<Direction, BooleanProperty> FACING_TO_PROPERTY_MAP = SixWayBlock.FACING_TO_PROPERTY_MAP;
    private final VoxelShape[] SHAPES = makeShapes(4);

    public DuctBlock(AbstractBlock.Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(UP, false).with(NORTH, false).with(SOUTH, false)
                .with(EAST, false).with(WEST, false).with(DOWN, false).with(WATERLOGGED, false));
    }

    private VoxelShape[] makeShapes(int width) {
        int edge = (16 - width) / 2;
        VoxelShape[] shapes = new VoxelShape[64];
        VoxelShape coreShape = Block.makeCuboidShape(edge, edge, edge, 16 - edge, 16 - edge, 16 - edge);
        VoxelShape northShape = Block.makeCuboidShape(edge, edge, 0, 16 - edge, 16 - edge, edge);
        VoxelShape southShape = Block.makeCuboidShape(edge, edge, 16 - edge, 16 - edge, 16 - edge, 16);
        VoxelShape westShape = Block.makeCuboidShape(0, edge, edge, edge, 16 - edge, 16 - edge);
        VoxelShape eastShape = Block.makeCuboidShape(16 - edge, edge, edge, 16, 16 - edge, 16 - edge);
        VoxelShape downShape = Block.makeCuboidShape(edge, 0, edge, 16 - edge, edge, 16 - edge);
        VoxelShape upShape = Block.makeCuboidShape(edge, 16 - edge, edge, 16 - edge, 16, 16 - edge);
        for (BlockState state : this.stateContainer.getValidStates()) {
            VoxelShape shape = coreShape;
            if (state.get(NORTH)) {
                shape = VoxelShapes.or(shape, northShape);
            }
            if (state.get(SOUTH)) {
                shape = VoxelShapes.or(shape, southShape);
            }
            if (state.get(EAST)) {
                shape = VoxelShapes.or(shape, eastShape);
            }
            if (state.get(WEST)) {
                shape = VoxelShapes.or(shape, westShape);
            }
            if (state.get(UP)) {
                shape = VoxelShapes.or(shape, upShape);
            }
            if (state.get(DOWN)) {
                shape = VoxelShapes.or(shape, downShape);
            }
            shapes[getIndex(state)] = shape;
        }
        return shapes;
    }

    private int getIndex(BlockState state) {
        return (state.get(NORTH) ? 0b1 : 0) | (state.get(SOUTH) ? 0b10 : 0) | (state.get(EAST) ? 0b100 : 0) | (state
                .get(WEST) ? 0b1000 : 0) | (state.get(UP) ? 0b10000 : 0) | (state.get(DOWN) ? 0b100000 : 0);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPES[getIndex(state)];
    }

    protected abstract ITag<Block> getMachineTag();

    protected abstract boolean canConnectDuct(Block block);

    public boolean canConnect(BlockState state, IBlockReader world, BlockPos pos, Direction direction) {
        Block block = state.getBlock();
        if (block.isIn(this.getMachineTag())) {
            MachineTileEntity te = ((MachineTileEntity) world.getTileEntity(pos));
            if (te != null) {
                return te.getMachine().getSideConfig().acceptsCableFrom(state.get(MachineBlock.FACING), direction);
            }
        }
        return this.canConnectDuct(block);
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        IBlockReader iblockreader = context.getWorld();
        BlockPos blockpos = context.getPos();
        FluidState fluidstate = context.getWorld().getFluidState(context.getPos());
        BlockPos northPos = blockpos.north();
        BlockPos eastPos = blockpos.east();
        BlockPos southPos = blockpos.south();
        BlockPos westPos = blockpos.west();
        BlockPos upPos = blockpos.up();
        BlockPos downPos = blockpos.down();
        BlockState northState = iblockreader.getBlockState(northPos);
        BlockState eastState = iblockreader.getBlockState(eastPos);
        BlockState southState = iblockreader.getBlockState(southPos);
        BlockState westState = iblockreader.getBlockState(westPos);
        BlockState upState = iblockreader.getBlockState(upPos);
        BlockState downState = iblockreader.getBlockState(downPos);

        return super.getStateForPlacement(context)
                .with(NORTH, this.canConnect(northState, iblockreader, northPos, Direction.SOUTH))
                .with(EAST, this.canConnect(eastState, iblockreader, eastPos, Direction.WEST))
                .with(SOUTH, this.canConnect(southState, iblockreader, southPos, Direction.NORTH))
                .with(WEST, this.canConnect(westState, iblockreader, westPos, Direction.EAST))
                .with(UP, this.canConnect(upState, iblockreader, upPos, Direction.DOWN))
                .with(DOWN, this.canConnect(downState, iblockreader, downPos, Direction.UP))
                .with(WATERLOGGED, fluidstate.getFluid() == Fluids.WATER);
    }

    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }

        return stateIn.with(FACING_TO_PROPERTY_MAP.get(facing),
                this.canConnect(facingState, worldIn, currentPos.offset(facing), facing.getOpposite()));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN, WATERLOGGED);
    }

}
