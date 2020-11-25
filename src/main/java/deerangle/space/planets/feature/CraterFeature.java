package deerangle.space.planets.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class CraterFeature extends Feature<CraterFeatureConfig> {

    public CraterFeature(Codec<CraterFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, CraterFeatureConfig config) {
        double depth = config.depth.func_242259_a(rand);
        double waterHeight = config.fillHeight * depth + 1;
        double yStart;
        double r;
        {
            double x = config.radius.func_242259_a(rand);
            double a = Math.atan(depth / x);
            double b = Math.PI / 2.0 - a;
            double e = Math.sqrt(x * x + depth * depth) / 2.0;
            double z = e * (1.0 / Math.cos(a));
            yStart = Math.tan(b) * (x - z);
            r = yStart + depth;
        }
        BlockPos.Mutable placePos = new BlockPos.Mutable();
        int scanRadius = (int) Math.ceil(r);
        for (int z = -scanRadius; z <= scanRadius; z++) {
            for (int x = -scanRadius; x <= scanRadius; x++) {
                for (int y = -scanRadius; y <= scanRadius; y++) {
                    double randoRad = r * r * (0.9 + rand.nextDouble() * 0.1);
                    if (x * x + y * y + z * z < randoRad) {
                        placePos.setAndOffset(pos, x, -y + (int) yStart, z);
                        if (y > scanRadius - waterHeight) {
                            reader.setBlockState(placePos, config.fillBlock.getBlockState(rand, placePos), 2);
                        } else {
                            reader.setBlockState(placePos, Blocks.AIR.getDefaultState(), 2);
                        }
                    }
                }
            }
        }
        return true;
    }

}
