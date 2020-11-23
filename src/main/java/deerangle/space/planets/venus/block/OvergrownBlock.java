package deerangle.space.planets.venus.block;

import deerangle.space.planets.venus.tags.BlockTags;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class OvergrownBlock extends Block {

    public OvergrownBlock(AbstractBlock.Properties properties) {
        super(properties);
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        BlockState blockstate = this.getDefaultState();

        for (int i = 0; i < 4; ++i) {
            //TODO: only when no block on top
            BlockPos blockpos = pos.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
            if (worldIn.getBlockState(blockpos).isIn(BlockTags.VENUS_OVERGROWABLE)) {
                worldIn.setBlockState(blockpos, blockstate);
            }
        }
    }

}
