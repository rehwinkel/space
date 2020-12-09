package deerangle.space.block;

import deerangle.space.block.entity.MachineTileEntity;
import deerangle.space.main.ConfigData;
import deerangle.space.registry.MachineTypeRegistry;
import deerangle.space.stats.Stats;
import deerangle.space.util.VoxelShapeUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.Random;

public class BlastFurnaceBlock extends MachineBlock {

    public static final BooleanProperty TOP_HALF = BooleanProperty.create("top_half");
    private static final VoxelShape[] SHAPE = VoxelShapeUtil.horizontalShape(Block.makeCuboidShape(1, 0, 1, 15, 9, 13), Block.makeCuboidShape(3, 9, 2, 13, 16, 12), Block.makeCuboidShape(5, 16, 4, 11, 28, 10), Block.makeCuboidShape(2, 0, 13, 4, 8, 16), Block.makeCuboidShape(4, 10, 12, 6, 17, 15), Block.makeCuboidShape(11, 21, 6, 14, 32, 8));
    private static final VoxelShape[] TOP_SHAPE = VoxelShapeUtil.horizontalShape(-1.0, Block.makeCuboidShape(1, 0, 1, 15, 9, 13), Block.makeCuboidShape(3, 9, 2, 13, 16, 12), Block.makeCuboidShape(5, 16, 4, 11, 28, 10), Block.makeCuboidShape(2, 0, 13, 4, 8, 16), Block.makeCuboidShape(4, 10, 12, 6, 17, 15), Block.makeCuboidShape(11, 21, 6, 14, 32, 8));

    public BlastFurnaceBlock(Properties properties) {
        super(properties, () -> MachineTypeRegistry.BLAST_FURNACE, Stats.INTERACT_WITH_BLAST_FURNACE);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(RUNNING, false).with(UP, false).with(NORTH, false).with(SOUTH, false).with(EAST, false).with(WEST, false).with(DOWN, false).with(TOP_HALF, false));
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if (state.get(TOP_HALF)) {
            return null;
        }
        return new MachineTileEntity(this.getRegistryName().getPath(), this.machineType.get());
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) {
            return ActionResultType.SUCCESS;
        } else {
            TileEntity tileEntity;
            if (state.get(TOP_HALF)) {
                tileEntity = worldIn.getTileEntity(pos.down());
            } else {
                tileEntity = worldIn.getTileEntity(pos);
            }
            if (tileEntity instanceof MachineTileEntity) {
                NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, ((MachineTileEntity) tileEntity)::writeGuiPacket);
                player.addStat(interactStat);
            }
            return ActionResultType.CONSUME;
        }
    }

    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        world.setBlockState(pos.up(), state.with(TOP_HALF, true), 3);
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

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (ConfigData.doMachineParticles) {
            if (stateIn.get(RUNNING) && !stateIn.get(TOP_HALF)) {
                double baseX = (double) pos.getX() + 0.5D;
                double baseY = pos.getY() + 0.125D;
                double baseZ = (double) pos.getZ() + 0.5D;
                if (rand.nextDouble() < 0.1D) {
                    worldIn.playSound(baseX, baseY, baseZ, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
                }

                Direction direction = stateIn.get(FACING);
                Direction.Axis axis = direction.getAxis();
                double n = rand.nextDouble() * 0.6D - 0.3D;
                double fireOffX = axis == Direction.Axis.X ? (double) direction.getXOffset() * 0.52D : n;
                double fireOffY = rand.nextDouble() * 6.0D / 16.0D;
                double fireOffZ = axis == Direction.Axis.Z ? (double) direction.getZOffset() * 0.52D : n;
                worldIn.addParticle(ParticleTypes.FLAME, baseX + fireOffX, baseY + fireOffY, baseZ + fireOffZ, 0.0D, 0.0D, 0.0D);

                for (int i = 0; i < 4; i++) {
                    this.spawnOffsetParticle(worldIn, stateIn.get(FACING), ParticleTypes.LARGE_SMOKE, baseX, baseY + 1.8D, baseZ, 0, 0);
                }
            }
        }
    }

}
