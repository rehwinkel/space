package deerangle.space.block;

import deerangle.space.stats.Stats;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.Random;

public class CoalGeneratorBlock extends MachineBlock {

    private static final VoxelShape SHAPE = VoxelShapes.or(Block.makeCuboidShape(1, 3, 0, 15, 14, 16));

    public CoalGeneratorBlock(Properties properties) {
        super(properties, Stats.INTERACT_WITH_COAL_GENERATOR);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
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
