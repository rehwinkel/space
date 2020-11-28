package deerangle.space.planet.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class RockFeatureConfig implements IFeatureConfig {

    public static final Codec<RockFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(BlockStateProvider.CODEC.fieldOf("block").forGetter(config -> config.block), FeatureSpread.CODEC.fieldOf("height").forGetter(config -> config.heightMultiplier)).apply(instance, RockFeatureConfig::new));

    public BlockStateProvider block;
    public FeatureSpread heightMultiplier;

    public RockFeatureConfig(BlockStateProvider block, FeatureSpread heightMultiplier) {
        this.block = block;
        this.heightMultiplier = heightMultiplier;
    }

}
