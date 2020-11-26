package deerangle.space.planet.util;

import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.ColumnFuzzedBiomeMagnifier;

import java.util.OptionalLong;

public class CustomDimensionType extends DimensionType {

    private final long dayLength;

    public CustomDimensionType(boolean hasSkyLight, boolean ultrawarm, boolean natural, ResourceLocation effects, float ambientLight, long dayLength) {
        super(OptionalLong.empty(), hasSkyLight, false, ultrawarm, natural, 1.0D, false, false, true, true, false, 256,
                ColumnFuzzedBiomeMagnifier.INSTANCE, BlockTags.INFINIBURN_OVERWORLD.getName(), effects, ambientLight);
        this.dayLength = dayLength;
    }

    @Override
    public float getCelestrialAngleByTime(long dayTime) {
        double d0 = MathHelper.frac((double) dayTime / ((double) dayLength) - 0.25D);
        double d1 = 0.5D - Math.cos(d0 * Math.PI) / 2.0D;
        return (float) (d0 * 2.0D + d1) / 3.0F;
    }

}
