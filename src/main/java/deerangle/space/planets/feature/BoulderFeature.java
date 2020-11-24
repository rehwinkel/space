package deerangle.space.planets.feature;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class BoulderFeature extends Feature<BoulderFeatureConfig> {

    public BoulderFeature(Codec<BoulderFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, BoulderFeatureConfig config) {
        int width = config.width.func_242259_a(rand) + 1;
        while (true) {
            label46:
            {
                if (pos.getY() > width) {
                    if (reader.isAirBlock(pos.down().down())) {
                        break label46;
                    }
                }

                if (pos.getY() <= width) {
                    return false;
                }

                for (int l = 0; l < width; ++l) {
                    int i = rand.nextInt(width - 1);
                    int j = rand.nextInt(width - 1);
                    int k = rand.nextInt(width - 1);
                    float f = (float) (i + j + k) * 0.333F + 0.5F;

                    for (BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-i, -j, -k), pos.add(i, j, k))) {
                        if (blockpos.distanceSq(pos) <= (double) (f * f)) {
                            reader.setBlockState(blockpos.up(), config.blockProvider.getBlockState(rand, blockpos), 4);
                        }
                    }

                    pos = pos.add(-1 + rand.nextInt(width - 1), -rand.nextInt(width - 1), -1 + rand.nextInt(width - 1));
                }

                return true;
            }

            pos = pos.down();
        }
    }

}
