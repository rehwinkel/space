package deerangle.space.registry;

import com.google.common.collect.ImmutableList;
import deerangle.space.main.SpaceMod;
import deerangle.space.planet.DimensionMaker;
import deerangle.space.planet.Planet;
import deerangle.space.planet.planets.mars.world.MarsBiomeMaker;
import deerangle.space.planet.planets.mercury.world.MercuryBiomeMaker;
import deerangle.space.planet.planets.venus.world.VenusBiomeMaker;
import deerangle.space.planet.render.AtmosphereRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class PlanetRegistry {

    public static Planet MERCURY;
    public static Planet VENUS;
    public static Planet MARS;

    @SubscribeEvent
    public static void registerPlanets(RegistryEvent.Register<Planet> event) {
        Planet venusMarsSun = Planet.builder().skyTexture(new ResourceLocation("textures/environment/sun.png"), 30).build(new ResourceLocation(SpaceMod.MOD_ID, "sun"));
        Planet mercurySun = Planet.builder().skyTexture(new ResourceLocation("textures/environment/sun.png"), 50).build(new ResourceLocation(SpaceMod.MOD_ID, "sun"));
        MERCURY = Planet.builder().addPlanetInSky(() -> mercurySun).dayLength(59 * 24000).superhot().fadingSunset(new Vector3f(0.90f, 0.67f, 0.22f)).dimensionMaker(DimensionMaker::makeMercuryDimension).atmosphere(AtmosphereRenderer::new).addBiome(new ResourceLocation(SpaceMod.MOD_ID, "mercury_vast"), MercuryBiomeMaker::makeVastBiome).build(new ResourceLocation(SpaceMod.MOD_ID, "mercury"));
        VENUS = Planet.builder().addPlanetInSky(() -> venusMarsSun).weather(ImmutableList.of(() -> WeatherRegistry.ACID_RAIN), 5 * 1200, 10 * 1200).dayLength(118 * 24000).superhot().fadingSunset(new Vector3f(0.90f, 0.67f, 0.22f)).dimensionMaker(DimensionMaker::makeVenusDimension).atmosphere(AtmosphereRenderer::new).cloudHeight(99.5f).addBiome(new ResourceLocation(SpaceMod.MOD_ID, "venus_hills"), VenusBiomeMaker::makeHillsBiome).addBiome(new ResourceLocation(SpaceMod.MOD_ID, "venus_lava_river"), VenusBiomeMaker::makeRiverBiome).build(new ResourceLocation(SpaceMod.MOD_ID, "venus"));
        //TODO: settings for mars
        MARS = Planet.builder().addPlanetInSky(() -> venusMarsSun).dayLength(24000).fullSunset(new Vector3f(1, 0, 1)).dimensionMaker(DimensionMaker::makeMarsDimension).atmosphere(AtmosphereRenderer::new).addBiome(new ResourceLocation(SpaceMod.MOD_ID, "mars_desert"), () -> MarsBiomeMaker.makeDesertBiome(0.25F, 0.025F)).build(new ResourceLocation(SpaceMod.MOD_ID, "mars"));
        event.getRegistry().registerAll(MERCURY, VENUS, MARS);
    }

    //TODO: rain particles in WorldRenderer#addRainParticles

}
