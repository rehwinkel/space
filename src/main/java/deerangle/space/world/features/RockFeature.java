package deerangle.space.world.features;

import com.mojang.serialization.Codec;
import deerangle.space.planets.mars.MarsRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

public class RockFeature extends Feature<NoFeatureConfig> {

    private static final BlockState[] BLOCK_TYPES = {
            MarsRegistry.REGOLITH.get().getDefaultState()
    };

    public RockFeature(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        float aX = 0.75f + rand.nextFloat() * 1.5f;
        float aZ = 0.75f + rand.nextFloat() * 1.5f;

        float offX = (rand.nextFloat() - rand.nextFloat()) / 4;
        float offZ = (rand.nextFloat() - rand.nextFloat()) / 4;

        float height_multiplier = 3 + rand.nextInt(4);

        for(int x = pos.getX() - 4; x < pos.getX() + 5; x++) {
            for(int z = pos.getZ() - 4; z < pos.getZ() + 5; z++) {
                if(Math.sqrt((x - pos.getX()) * (x - pos.getX()) + (z - pos.getZ()) * (z - pos.getZ())) < 5f) {
                    int height = (int) (getHeight((x - pos.getX()) / 4f, (z - pos.getZ()) / 4f, aX, aZ, offX, offZ) * height_multiplier + rand.nextFloat() / 2);
                    if (height > 0) {
                        for (int y = pos.getY() - height; y < pos.getY() + height; y++) {
                            reader.setBlockState(new BlockPos(x, y, z), BLOCK_TYPES[rand.nextInt(BLOCK_TYPES.length)], 4);
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
