package deerangle.space.block;

import deerangle.space.main.ConfigData;
import deerangle.space.stats.Stats;
import deerangle.space.util.VoxelShapeUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;

import java.util.Random;

public class CoalGeneratorBlock extends MachineBlock {

    private static final VoxelShape[] SHAPE = VoxelShapeUtil
            .horizontalShape(Block.makeCuboidShape(2, 0, 1, 14, 3, 16), Block.makeCuboidShape(1, 3, 0, 15, 14, 16),
                    Block.makeCuboidShape(14, 10, 13, 16, 16, 15), Block.makeCuboidShape(0, 10, 13, 2, 16, 15),
                    Block.makeCuboidShape(0, 4, 2, 1, 9, 10), Block.makeCuboidShape(5, 14, 4, 8, 16, 11));

    public CoalGeneratorBlock(Properties properties) {
        super(properties, Stats.INTERACT_WITH_COAL_GENERATOR);
    }

    @Override
    protected VoxelShape[] getMachineShape() {
        return SHAPE;
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (ConfigData.doMachineParticles) {
            if (stateIn.get(RUNNING)) {
                double baseX = (double) pos.getX() + 0.5D;
                double baseY = pos.getY() + 0.25D;
                double baseZ = (double) pos.getZ() + 0.5D;
                if (rand.nextDouble() < 0.1D) {
                    worldIn.playSound(baseX, baseY, baseZ, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS,
                            1.0F, 1.0F, false);
                }

                Direction direction = stateIn.get(FACING);
                Direction.Axis axis = direction.getAxis();
                double n = rand.nextDouble() * 0.6D - 0.3D;
                double fireOffX = axis == Direction.Axis.X ? (double) direction.getXOffset() * 0.52D : n;
                double fireOffY = rand.nextDouble() * 6.0D / 16.0D;
                double fireOffZ = axis == Direction.Axis.Z ? (double) direction.getZOffset() * 0.52D : n;
                worldIn.addParticle(ParticleTypes.FLAME, baseX + fireOffX, baseY + fireOffY, baseZ + fireOffZ, 0.0D,
                        0.0D, 0.0D);

                for (int i = 0; i < 4; i++) {
                    double randOffX = rand.nextDouble() * 0.125D - 0.0625D;
                    double randOffZ = 0.375D + (rand.nextDouble() * 0.125D - 0.0625D);
                    this.spawnOffsetParticle(worldIn, stateIn.get(FACING), ParticleTypes.SMOKE, baseX, baseY + 0.875D,
                            baseZ, randOffX + 0.4375f, randOffZ);
                    this.spawnOffsetParticle(worldIn, stateIn.get(FACING), ParticleTypes.SMOKE, baseX, baseY + 0.875D,
                            baseZ, randOffX - 0.4375f, randOffZ);
                }
            }
        }
    }

}
