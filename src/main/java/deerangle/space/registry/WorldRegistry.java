package deerangle.space.registry;

import deerangle.space.world.features.RockFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.fml.RegistryObject;

public class WorldRegistry extends AbstractRegistry {

    public static RegistryObject<RockFeature> ROCK = FEATURES.register("rock", () -> new RockFeature(NoFeatureConfig.field_236558_a_));

    public static void register() {

    }
}
