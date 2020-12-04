package deerangle.space.registry;

import deerangle.space.main.SpaceMod;
import deerangle.space.planet.Weather;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class WeatherRegistry {

    public static Weather ACID_RAIN;
    public static Weather RAIN;

    @SubscribeEvent
    public static void registerWeathers(RegistryEvent.Register<Weather> event) {
        ACID_RAIN = new Weather(new ResourceLocation(SpaceMod.MOD_ID, "acid_rain"), ParticleTypes.RAIN).setRegistryName(new ResourceLocation(SpaceMod.MOD_ID, "acid_rain"));
        RAIN = new Weather(new ResourceLocation("rain"), ParticleTypes.RAIN).setRegistryName(new ResourceLocation(SpaceMod.MOD_ID, "rain"));
        event.getRegistry().registerAll(ACID_RAIN, RAIN);
    }

}
