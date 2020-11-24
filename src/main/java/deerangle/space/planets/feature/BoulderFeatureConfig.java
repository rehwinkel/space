package deerangle.space.planets.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class BoulderFeatureConfig implements IFeatureConfig {

    public static final Codec<BoulderFeatureConfig> CODEC = RecordCodecBuilder.create((builder) -> builder
            .group(BlockStateProvider.CODEC.fieldOf("block_provider").forGetter(inst -> inst.blockProvider),
                    FeatureSpread.CODEC.fieldOf("width").forGetter(inst -> inst.width))
            .apply(builder, BoulderFeatureConfig::new));

    public BlockStateProvider blockProvider;
    public FeatureSpread width;

    public BoulderFeatureConfig(BlockStateProvider blockProvider, FeatureSpread width) {
        this.blockProvider = blockProvider;
        this.width = width;
    }

}
