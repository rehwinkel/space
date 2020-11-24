package deerangle.space.planets.feature;

import com.mojang.serialization.Codec;
import deerangle.space.planets.venus.VenusRegistry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class AlgaePatchFeature extends Feature<AlgaePathFeatureConfig> {

    public AlgaePatchFeature(Codec<AlgaePathFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, AlgaePathFeatureConfig config) {
        BlockPos.Mutable placementPos = pos.toMutable();
        int i = placementPos.getY() - 2;
        for (; i >= 0; i--) {
            placementPos.setY(i);
            if (reader.isAirBlock(placementPos) && reader.getBlockState(placementPos.down()).isSolid()) {
                break;
            }
        }
        if (i == 0) {
            return false;
        }
        BlockPos startPos = new BlockPos(placementPos);
        int radius = config.size.func_242259_a(rand);
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (x * x + z * z < (radius * 0.8f) * (radius * 0.8f)) {
                    placementPos.setAndOffset(startPos, x, 0, z);
                    if (reader.isAirBlock(placementPos) && reader.getBlockState(placementPos.down()).isSolid()) {
                        reader.setBlockState(placementPos, VenusRegistry.SLIMY_ALGAE.get().getDefaultState(), 2);
                    }
                }
            }
        }
        return true;
    }

}
