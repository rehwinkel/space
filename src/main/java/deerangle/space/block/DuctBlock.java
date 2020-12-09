package deerangle.space.block;

import com.google.common.collect.Maps;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.capabilities.Capability;

import java.util.Map;

public abstract class DuctBlock extends Block {

    public static final EnumProperty<Connection> NORTH = EnumProperty.create("north", Connection.class);
    public static final EnumProperty<Connection> EAST = EnumProperty.create("east", Connection.class);
    public static final EnumProperty<Connection> SOUTH = EnumProperty.create("south", Connection.class);
    public static final EnumProperty<Connection> WEST = EnumProperty.create("west", Connection.class);
    public static final EnumProperty<Connection> UP = EnumProperty.create("up", Connection.class);
    public static final EnumProperty<Connection> DOWN = EnumProperty.create("down", Connection.class);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final Map<Direction, EnumProperty<Connection>> FACING_TO_PROPERTY_MAP = Util.make(Maps.newEnumMap(Direction.class), (directions) -> {
        directions.put(Direction.NORTH, NORTH);
        directions.put(Direction.EAST, EAST);
        directions.put(Direction.SOUTH, SOUTH);
        directions.put(Direction.WEST, WEST);
        directions.put(Direction.UP, UP);
        directions.put(Direction.DOWN, DOWN);
    });
    private final VoxelShape[] SHAPES = makeShapes(4);

    //TODO: actually transmit items/fluids/energy

    public DuctBlock(AbstractBlock.Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(UP, Connection.NONE).with(NORTH, Connection.NONE).with(SOUTH, Connection.NONE).with(EAST, Connection.NONE).with(WEST, Connection.NONE).with(DOWN, Connection.NONE).with(WATERLOGGED, false));
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
            if (state.get(NORTH) != Connection.NONE) {
                shape = VoxelShapes.or(shape, northShape);
            }
            if (state.get(SOUTH) != Connection.NONE) {
                shape = VoxelShapes.or(shape, southShape);
            }
            if (state.get(EAST) != Connection.NONE) {
                shape = VoxelShapes.or(shape, eastShape);
            }
            if (state.get(WEST) != Connection.NONE) {
                shape = VoxelShapes.or(shape, westShape);
            }
            if (state.get(UP) != Connection.NONE) {
                shape = VoxelShapes.or(shape, upShape);
            }
            if (state.get(DOWN) != Connection.NONE) {
                shape = VoxelShapes.or(shape, downShape);
            }
            shapes[getIndex(state)] = shape;
        }
        return shapes;
    }

    private int getIndex(BlockState state) {
        return (state.get(NORTH) != Connection.NONE ? 0b1 : 0) | (state.get(SOUTH) != Connection.NONE ? 0b10 : 0) | (state.get(EAST) != Connection.NONE ? 0b100 : 0) | (state.get(WEST) != Connection.NONE ? 0b1000 : 0) | (state.get(UP) != Connection.NONE ? 0b10000 : 0) | (state.get(DOWN) != Connection.NONE ? 0b100000 : 0);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPES[getIndex(state)];
    }

    protected abstract Capability<?> getDuctCapability();

    protected abstract boolean canConnectDuct(Block block);

    public Connection getConnectionType(BlockState state, IBlockReader world, BlockPos pos, Direction direction) {
        TileEntity te = world.getTileEntity(pos);
        if (te != null) {
            return te.getCapability(this.getDuctCapability(), direction).isPresent() ? Connection.OTHER : Connection.NONE;
        }
        return this.canConnectDuct(state.getBlock()) ? Connection.SELF : Connection.NONE;
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

        return super.getStateForPlacement(context).with(NORTH, this.getConnectionType(northState, iblockreader, northPos, Direction.SOUTH)).with(EAST, this.getConnectionType(eastState, iblockreader, eastPos, Direction.WEST)).with(SOUTH, this.getConnectionType(southState, iblockreader, southPos, Direction.NORTH)).with(WEST, this.getConnectionType(westState, iblockreader, westPos, Direction.EAST)).with(UP, this.getConnectionType(upState, iblockreader, upPos, Direction.DOWN)).with(DOWN, this.getConnectionType(downState, iblockreader, downPos, Direction.UP)).with(WATERLOGGED, fluidstate.getFluid() == Fluids.WATER);
    }

    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }

        return stateIn.with(FACING_TO_PROPERTY_MAP.get(facing), this.getConnectionType(facingState, worldIn, currentPos.offset(facing), facing.getOpposite()));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN, WATERLOGGED);
    }

    public enum Connection implements IStringSerializable {
        NONE, SELF, OTHER;

        @Override
        public String getString() {
            switch (this) {
                case NONE:
                    return "none";
                case SELF:
                    return "self";
                case OTHER:
                    return "other";
            }
            return "";
        }
    }
}
