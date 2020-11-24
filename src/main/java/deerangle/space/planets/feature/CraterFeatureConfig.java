package deerangle.space.planets.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class CraterFeatureConfig implements IFeatureConfig {
    public static final Codec<CraterFeatureConfig> CODEC = RecordCodecBuilder.create((builder) -> builder
            .group(BlockStateProvider.CODEC.fieldOf("fill_provider").forGetter(inst -> inst.fillBlock),
                    BlockStateProvider.CODEC.fieldOf("edge_provider").forGetter(inst -> inst.edgeBlock),
                    FeatureSpread.CODEC.fieldOf("radius").forGetter(inst -> inst.radius),
                    FeatureSpread.CODEC.fieldOf("depth").forGetter(inst -> inst.depth),
                    FeatureSpread.CODEC.fieldOf("edge_height").forGetter(inst -> inst.edgeHeight),
                    Codec.DOUBLE.fieldOf("fill_height").forGetter(inst -> inst.fillHeight))
            .apply(builder, CraterFeatureConfig::new));

    public FeatureSpread radius;
    public FeatureSpread depth;
    public FeatureSpread edgeHeight;
    public BlockStateProvider fillBlock;
    public BlockStateProvider edgeBlock;
    public double fillHeight;

    public CraterFeatureConfig(BlockStateProvider fillBlock, BlockStateProvider edgeBlock, FeatureSpread radius, FeatureSpread depth, FeatureSpread edgeHeight, double fillHeight) {
        this.edgeHeight = edgeHeight;
        this.radius = radius;
        this.depth = depth;
        this.fillBlock = fillBlock;
        this.edgeBlock = edgeBlock;
        this.fillHeight = fillHeight;
    }

    public CraterFeatureConfig(BlockStateProvider edgeBlock, FeatureSpread radius, FeatureSpread depth, FeatureSpread edgeHeight) {
        this(new SimpleBlockStateProvider(Blocks.AIR.getDefaultState()), edgeBlock, radius, depth, edgeHeight, 0.0f);
    }

    public CraterFeatureConfig(FeatureSpread radius, FeatureSpread depth, FeatureSpread edgeHeight) {
        this(new SimpleBlockStateProvider(Blocks.AIR.getDefaultState()),
                new SimpleBlockStateProvider(Blocks.AIR.getDefaultState()), radius, depth, edgeHeight, 0.0f);
    }

}
