package deerangle.space.planet.planets.venus.block;

import deerangle.space.planet.planets.venus.tags.BlockTags;
import deerangle.space.planet.planets.venus.world.ShriekwoodTree;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SaplingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;

public class ShriekwoodSaplingBlock extends SaplingBlock {

    public ShriekwoodSaplingBlock(AbstractBlock.Properties properties) {
        super(new ShriekwoodTree(), properties);
    }

    protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return state.isIn(BlockTags.VENUS_GROUND);
    }

    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.down();
        return this.isValidGround(worldIn.getBlockState(blockpos), worldIn, blockpos);
    }

}
