package deerangle.space.registry;

import deerangle.space.main.SpaceMod;
import deerangle.space.planet.DimensionMaker;
import deerangle.space.planet.Planet;
import deerangle.space.planet.planets.mars.world.MarsBiomeMaker;
import deerangle.space.planet.planets.venus.world.VenusBiomeMaker;
import deerangle.space.planet.render.AtmosphereRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class PlanetRegistry {

    public static Planet SUN;
    public static Planet VENUS;
    public static Planet MARS;

    @SubscribeEvent
    public static void registerPlanets(RegistryEvent.Register<Planet> event) {
        SUN = Planet.builder().skyTexture(new ResourceLocation("textures/environment/sun.png"), 30)
                .build(new ResourceLocation(SpaceMod.MOD_ID, "sun"));
        VENUS = Planet.builder().addPlanetInSky(() -> SUN).dayLength(118 * 24000).superhot()
                .fadingSunset(new Vector3f(0.90f, 0.67f, 0.22f)).dimensionMaker(DimensionMaker::makeVenusDimension)
                .atmosphere(AtmosphereRenderer::new).cloudHeight(80f)
                .addBiome(new ResourceLocation(SpaceMod.MOD_ID, "venus_hills"), VenusBiomeMaker::makeHillsBiome)
                .addBiome(new ResourceLocation(SpaceMod.MOD_ID, "venus_lava_river"), VenusBiomeMaker::makeRiverBiome)
                .build(new ResourceLocation(SpaceMod.MOD_ID, "venus"));
        //TODO: settings for mars
        MARS = Planet.builder().addPlanetInSky(() -> SUN).dayLength(24000).fullSunset(new Vector3f(1, 0, 1))
                .dimensionMaker(DimensionMaker::makeMarsDimension).atmosphere(AtmosphereRenderer::new)
                .addBiome(new ResourceLocation(SpaceMod.MOD_ID, "mars_desert"),
                        () -> MarsBiomeMaker.makeDesertBiome(0.25F, 0.025F))
                .build(new ResourceLocation(SpaceMod.MOD_ID, "mars"));
        event.getRegistry().registerAll(VENUS, MARS);
    }

}
