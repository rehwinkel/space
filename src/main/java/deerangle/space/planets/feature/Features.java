package deerangle.space.planets.feature;

import deerangle.space.main.SpaceMod;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SphereReplaceConfig;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Features {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister
            .create(ForgeRegistries.FEATURES, SpaceMod.MOD_ID);

    public static final RegistryObject<Feature<BoulderFeatureConfig>> BOULDER = FEATURES
            .register("boulder", () -> new BoulderFeature(BoulderFeatureConfig.CODEC));

    public static final RegistryObject<Feature<CraterFeatureConfig>> CRATER = FEATURES
            .register("crater", () -> new CraterFeature(CraterFeatureConfig.CODEC));

    public static final RegistryObject<Feature<SphereReplaceConfig>> DISK = FEATURES
            .register("disk", () -> new DiskFeature(SphereReplaceConfig.field_236516_a_));

    public static final RegistryObject<Feature<AlgaePathFeatureConfig>> ALGAE_PATCH = FEATURES
            .register("algae_patch", () -> new AlgaePatchFeature(AlgaePathFeatureConfig.CODEC));

}
