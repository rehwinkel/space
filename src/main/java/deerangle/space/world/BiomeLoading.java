package deerangle.space.world;

import deerangle.space.registry.BiomeRegistry;
import deerangle.space.registry.WorldRegistry;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BiomeLoading {

    @SubscribeEvent
    public static void biomeLoading(BiomeLoadingEvent event) {
        if(event.getName().toString().equals(BiomeRegistry.RUSTY_DESERT.get().getRegistryName().toString())) {
            event.getGeneration().withFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, WorldRegistry.ROCK.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG)
                    .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(0, 0.075f, 2))));
        }
    }
}
