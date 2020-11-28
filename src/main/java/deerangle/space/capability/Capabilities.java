package deerangle.space.capability;

import deerangle.space.main.SpaceMod;
import deerangle.space.planet.Planet;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Capabilities {

    @CapabilityInject(IWeatherCapability.class)
    public static Capability<IWeatherCapability> WEATHER_CAPABILITY = null;
    public static ResourceLocation WEATHER_CAPABILITY_LOCATION = new ResourceLocation(SpaceMod.MOD_ID, "weather");

    public static void register() {
        CapabilityManager.INSTANCE.register(IWeatherCapability.class, new IWeatherCapability.Storage(), DefaultWeatherCapability::new);
    }

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<World> event) {
        IForgeRegistry<Planet> planets = RegistryManager.ACTIVE.getRegistry(Planet.class);
        for(Planet p :planets.getValues()) {
            if(p.getDimensionKey() ==
                    event.getObject().getDimensionKey()) {

                WeatherProvider provider = new WeatherProvider(p.getAvailableWeathers(), p.getWeatherCycleMin(), p.getWeatherCycleMax());
                event.addCapability(WEATHER_CAPABILITY_LOCATION, provider);
                event.addListener(provider::invalidate);
            }
        }
    }

}
