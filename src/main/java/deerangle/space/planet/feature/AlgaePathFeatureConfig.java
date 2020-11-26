package deerangle.space.planet.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class AlgaePathFeatureConfig implements IFeatureConfig {

    public static final Codec<AlgaePathFeatureConfig> CODEC = RecordCodecBuilder
            .create((builder) -> builder.group(FeatureSpread.CODEC.fieldOf("size").forGetter(inst -> inst.size))
                    .apply(builder, AlgaePathFeatureConfig::new));

    public FeatureSpread size;

    public AlgaePathFeatureConfig(FeatureSpread size) {
        this.size = size;
    }

}
