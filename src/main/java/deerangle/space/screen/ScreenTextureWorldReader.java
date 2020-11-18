package deerangle.space.screen;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.lighting.WorldLightManager;

public class ScreenTextureWorldReader implements IBlockDisplayReader {
    @Override
    public float func_230487_a_(Direction p_230487_1_, boolean p_230487_2_) {
        return 0;
    }

    @Override
    public WorldLightManager getLightManager() {
        return null;
    }

    @Override
    public int getBlockColor(BlockPos blockPosIn, ColorResolver colorResolverIn) {
        return colorResolverIn.getColor(WorldGenRegistries.BIOME.getOrThrow(Biomes.PLAINS), 0, 0);
    }

    @Override
    public TileEntity getTileEntity(BlockPos pos) {
        return null;
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        return null;
    }

    @Override
    public FluidState getFluidState(BlockPos pos) {
        return null;
    }
}
