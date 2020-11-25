package deerangle.space.planets.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class BetterLakeFeatureConfig implements IFeatureConfig {

    public static final Codec<BetterLakeFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(BlockStateProvider.CODEC.fieldOf("fluid_provider").forGetter(inst -> inst.fluidBlock),
                    BlockStateProvider.CODEC.fieldOf("edge_provider").forGetter(inst -> inst.edgeBlock))
            .apply(instance, BetterLakeFeatureConfig::new));

    public BlockStateProvider fluidBlock;
    public BlockStateProvider edgeBlock;

    public BetterLakeFeatureConfig(BlockStateProvider fluidBlock, BlockStateProvider edgeBlock) {
        this.fluidBlock = fluidBlock;
        this.edgeBlock = edgeBlock;
    }
}
