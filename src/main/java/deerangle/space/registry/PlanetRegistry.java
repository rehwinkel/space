package deerangle.space.registry;

import deerangle.space.main.SpaceMod;
import deerangle.space.planet.DimensionMaker;
import deerangle.space.planet.Planet;
import deerangle.space.planet.planets.venus.render.VenusAtmosphereRenderer;
import deerangle.space.planet.planets.venus.world.VenusBiomeMaker;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class PlanetRegistry {

    public static Planet SUN;
    public static Planet VENUS;

    @SubscribeEvent
    public static void registerPlanets(RegistryEvent.Register<Planet> event) {
        SUN = Planet.builder().skyTexture(new ResourceLocation("textures/environment/sun.png"), 30)
                .build(new ResourceLocation(SpaceMod.MOD_ID, "sun"));
        VENUS = Planet.builder().addPlanetInSky(() -> SUN).dayLength(118 * 24000).superhot()
                .dimensionMaker(DimensionMaker::makeVenusDimension).atmosphere(VenusAtmosphereRenderer::new)
                .addBiome(new ResourceLocation(SpaceMod.MOD_ID, "venus_hills"), VenusBiomeMaker::makeHillsBiome)
                .addBiome(new ResourceLocation(SpaceMod.MOD_ID, "venus_lava_river"), VenusBiomeMaker::makeRiverBiome)
                .build(new ResourceLocation(SpaceMod.MOD_ID, "venus"));
        event.getRegistry().registerAll(VENUS);
    }

}
