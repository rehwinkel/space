package deerangle.space.block;

import deerangle.space.main.ConfigData;
import deerangle.space.registry.MachineTypeRegistry;
import deerangle.space.stats.Stats;
import deerangle.space.util.VoxelShapeUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;

import java.util.Random;

public class CombustionGeneratorBlock extends MachineBlock {

    private static final VoxelShape[] SHAPE = VoxelShapeUtil.horizontalShape(Block.makeCuboidShape(3, 0, 3, 13, 3, 16), Block.makeCuboidShape(1, 3, 3, 15, 12, 16), Block.makeCuboidShape(7, 8, 5, 9, 16, 7), Block.makeCuboidShape(5, 3, 0, 11, 9, 4));

    public CombustionGeneratorBlock(Properties properties) {
        super(properties, () -> MachineTypeRegistry.COMBUSTION_GENERATOR, Stats.INTERACT_WITH_COMBUSTION_GENERATOR);
    }

    @Override
    protected VoxelShape[] getMachineShape(BlockState state) {
        return SHAPE;
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (ConfigData.doMachineParticles) {
            if (stateIn.get(RUNNING)) {
                double baseX = (double) pos.getX() + 0.5D;
                double baseY = pos.getY() + 1.125D;
                double baseZ = (double) pos.getZ() + 0.5D;

                for (int i = 0; i < 5; i++) {
                    double randOffX = rand.nextDouble() * 0.125D - 0.0625D;
                    double randOffZ = rand.nextDouble() * 0.125D - 0.0625D;
                    this.spawnOffsetParticle(worldIn, stateIn.get(FACING), ParticleTypes.SMOKE, baseX, baseY, baseZ, randOffX, randOffZ - 0.125D);
                }
            }
        }
    }

}
