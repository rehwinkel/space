package deerangle.space.block;

import deerangle.space.block.entity.MachineTileEntity;
import deerangle.space.machine.Machine;
import deerangle.space.machine.element.MachineType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.IParticleData;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.Map;
import java.util.function.Supplier;

public abstract class MachineBlock extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty NORTH = SixWayBlock.NORTH;
    public static final BooleanProperty EAST = SixWayBlock.EAST;
    public static final BooleanProperty SOUTH = SixWayBlock.SOUTH;
    public static final BooleanProperty WEST = SixWayBlock.WEST;
    public static final BooleanProperty UP = SixWayBlock.UP;
    public static final BooleanProperty DOWN = SixWayBlock.DOWN;
    public static final BooleanProperty RUNNING = BooleanProperty.create("running");
    protected static final Map<Direction, BooleanProperty> FACING_TO_PROPERTY_MAP = SixWayBlock.FACING_TO_PROPERTY_MAP;
    protected final ResourceLocation interactStat;
    protected final Supplier<MachineType<?>> machineType;

    public MachineBlock(Properties properties, Supplier<MachineType<?>> machineType, ResourceLocation interactStat) {
        super(properties);
        this.machineType = machineType;
        this.interactStat = interactStat;
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(RUNNING, false).with(UP, false).with(NORTH, false).with(SOUTH, false).with(EAST, false).with(WEST, false).with(DOWN, false));
    }

    protected abstract VoxelShape[] getMachineShape(BlockState state);

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return getMachineShape(state)[state.get(FACING).getHorizontalIndex()];
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) {
            return ActionResultType.SUCCESS;
        } else {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof MachineTileEntity) {
                NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, ((MachineTileEntity) tileEntity)::writeGuiPacket);
                player.addStat(interactStat);
            }
            return ActionResultType.CONSUME;
        }
    }

    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof MachineTileEntity) {
            Machine machine = ((MachineTileEntity) tileEntity).getMachine();
            if (!worldIn.isRemote) {
                machine.dropInventoryItems(worldIn, pos);
                ItemStack itemstack = new ItemStack(this);
                CompoundNBT nbt = machine.write(new CompoundNBT());
                if (!nbt.isEmpty()) {
                    itemstack.setTagInfo("BlockEntityTag", nbt);
                }

                ItemEntity itemEntity = new ItemEntity(worldIn, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, itemstack);
                itemEntity.setDefaultPickupDelay();
                worldIn.addEntity(itemEntity);
            }
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    public boolean canConnect(MachineTileEntity te, Direction side, BlockState state) {
        Block block = state.getBlock();
        return block instanceof DuctBlock && te.getCapability(((DuctBlock) block).getDuctCapability(), side).isPresent();
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        // don't worry, connections are handled in onBlockPlacedBy
        Direction facing = context.getPlacementHorizontalFacing().getOpposite();
        return super.getStateForPlacement(context).with(FACING, facing);
    }

    public BlockState updatePostPlacement(BlockState stateIn, Direction side, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        Direction facing = stateIn.get(FACING);
        Direction rotated = side;
        if (side.getAxis() != Direction.Axis.Y) {
            switch (facing) {
                case NORTH:
                    rotated = side;
                    break;
                case SOUTH:
                    rotated = side.rotateY().rotateY();
                    break;
                case EAST:
                    rotated = side.rotateYCCW();
                    break;
                case WEST:
                    rotated = side.rotateY();
                    break;
            }
        }
        BooleanProperty prop = FACING_TO_PROPERTY_MAP.get(rotated);
        TileEntity tileEntity = worldIn.getTileEntity(currentPos);
        assert tileEntity instanceof MachineTileEntity;
        return stateIn.with(prop, this.canConnect((MachineTileEntity) tileEntity, side, facingState));
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        world.setBlockState(pos, Block.getValidBlockForPosition(world.getBlockState(pos), world, pos)); // updates all sides for connections
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new MachineTileEntity(this.getRegistryName().getPath(), this.machineType.get());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING, RUNNING, NORTH, SOUTH, EAST, WEST, UP, DOWN);
    }

    @OnlyIn(Dist.CLIENT)
    protected void spawnOffsetParticle(World worldIn, Direction dir, IParticleData particle, double baseX, double baseY, double baseZ, double x, double z) {
        if (dir.getAxis() == Direction.Axis.X) {
            double b = -x;
            x = z;
            z = b;
        }
        if (dir.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
            x = -x;
            z = -z;
        }
        worldIn.addParticle(particle, baseX + x, baseY, baseZ + z, 0.0D, 0.0D, 0.0D);
    }

}
