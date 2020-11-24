package deerangle.space.planets.feature;

import deerangle.space.main.SpaceMod;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Features {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister
            .create(ForgeRegistries.FEATURES, SpaceMod.MOD_ID);

    public static final RegistryObject<Feature<BoulderFeatureConfig>> BOULDER = FEATURES
            .register("boulder", () -> new BoulderFeature(BoulderFeatureConfig.CODEC));

}
