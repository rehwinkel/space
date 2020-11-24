package deerangle.space.planets.venus.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

public class CrystalBlock extends Block {

    public CrystalBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return VoxelShapes.empty();
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 1.0F;
    }
    
}
