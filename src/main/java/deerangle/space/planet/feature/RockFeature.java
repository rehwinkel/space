package deerangle.space.planet.feature;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class RockFeature extends Feature<RockFeatureConfig> {

    public RockFeature(Codec<RockFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, RockFeatureConfig config) {
        float aX = 0.75f + rand.nextFloat() * 1.5f;
        float aZ = 0.75f + rand.nextFloat() * 1.5f;

        float offX = (rand.nextFloat() - rand.nextFloat()) / 4;
        float offZ = (rand.nextFloat() - rand.nextFloat()) / 4;

        float height_multiplier = config.heightMultiplier.func_242259_a(rand) ;

        BlockPos.Mutable placementPos = new BlockPos.Mutable();
        for (int x = -4; x <= 4; x++) {
            for (int z = -4; z <= 4; z++) {
                if (x * x + z * z < 5f * 5f) {
                    int height = (int) (getHeight(x / 4f, z / 4f, aX, aZ, offX, offZ) * height_multiplier + rand
                            .nextFloat() * 0.5f);
                    if (height > 0) {
                        for (int y = -height; y < height; y++) {
                            placementPos.setAndOffset(pos, x, y, z);
                            reader.setBlockState(placementPos, config.block.getBlockState(rand, placementPos), 2);
                        }
                    }
                }
            }
        }
        return true;
    }

    private static float getHeight(float relX, float relZ, float aX, float aZ, float offX, float offZ) {
        float parX = -aX * (relX - offX) * (relX - offX) + 1;
        float parZ = -aZ * (relZ - offZ) * (relZ - offZ) + 1;

        return (parX >= 0 && parZ >= 0) ? parX * parZ : 0;
    }

}
