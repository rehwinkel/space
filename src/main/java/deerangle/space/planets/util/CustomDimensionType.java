package deerangle.space.planets.util;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.IBiomeMagnifier;

import java.util.OptionalLong;

public class CustomDimensionType extends DimensionType {

    public CustomDimensionType(OptionalLong fixedTime, boolean hasSkyLight, boolean hasCeiling, boolean ultrawarm, boolean natural, double coordinateScale, boolean hasDragonFight, boolean piglinSafe, boolean bedWorks, boolean respawnAnchorWorks, boolean hasRaids, int logicalHeight, IBiomeMagnifier magnifier, ResourceLocation infiniburn, ResourceLocation effects, float ambientLight) {
        super(fixedTime, hasSkyLight, hasCeiling, ultrawarm, natural, coordinateScale, hasDragonFight, piglinSafe,
                bedWorks, respawnAnchorWorks, hasRaids, logicalHeight, magnifier, infiniburn, effects, ambientLight);
    }

}
