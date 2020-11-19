package deerangle.space.block;

import deerangle.space.stats.Stats;
import deerangle.space.util.VoxelShapeUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.particles.ParticleTypes;
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
        // TODO: if (stateIn.get(LIT)) {
        double baseX = (double) pos.getX() + 0.5D;
        double baseY = pos.getY() + 1.125D;
        double baseZ = (double) pos.getZ() + 0.5D;
        //TODO: if (rand.nextDouble() < 0.1D) { worldIn.playSound(d0, d1, d2, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false); }

        for (int i = 0; i < 4; i++) {
            double randOffX = rand.nextDouble() * 0.125D - 0.0625D;
            double randOffZ = 0.375D + (rand.nextDouble() * 0.125D - 0.0625D);
            this.spawnOffsetParticle(worldIn, stateIn.get(FACING), ParticleTypes.SMOKE, baseX, baseY, baseZ,
                    randOffX + 0.4375f, randOffZ);
            this.spawnOffsetParticle(worldIn, stateIn.get(FACING), ParticleTypes.SMOKE, baseX, baseY, baseZ,
                    randOffX - 0.4375f, randOffZ);
        }
    }

}
