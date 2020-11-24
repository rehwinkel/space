package deerangle.space.planets.feature;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.feature.AbstractSphereReplaceConfig;
import net.minecraft.world.gen.feature.SphereReplaceConfig;

public class DiskFeature extends AbstractSphereReplaceConfig {

    public DiskFeature(Codec<SphereReplaceConfig> codec) {
        super(codec);
    }

}
