package deerangle.space.world;

import deerangle.space.capability.Capabilities;
import deerangle.space.network.PacketHandler;
import deerangle.space.network.SyncWeatherMsg;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WorldEvents {

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent event) {
        if (!event.world.isRemote()) {
            event.world.getCapability(Capabilities.WEATHER_CAPABILITY).ifPresent(cap -> {
                int current = cap.getCurrentWeatherTimeout();
                cap.setCurrentWeatherTimeout(current - 1);
                if (cap.getAvailableWeathers().size() > 0) {
                    if (current <= 1) {
                        cap.setCurrentWeatherTimeout(cap.getMinWeatherTimeout() + event.world.getRandom().nextInt(cap.getMaxWeatherTimeout() - cap.getMinWeatherTimeout()));
                        int index = event.world.getRandom().nextInt(cap.getAvailableWeathers().size() + 1);
                        if (index == cap.getAvailableWeathers().size()) {
                            cap.setCurrentWeather(null);
                        } else {
                            cap.setCurrentWeather(cap.getAvailableWeathers().get(index).get());
                        }
                        PacketHandler.INSTANCE.send(PacketDistributor.DIMENSION.with(event.world::getDimensionKey), new SyncWeatherMsg(cap.getCurrentWeather()));
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public static void joinWorld(EntityJoinWorldEvent event) {
        if (!event.getWorld().isRemote && event.getEntity() instanceof ServerPlayerEntity) {
            event.getWorld().getCapability(Capabilities.WEATHER_CAPABILITY).ifPresent(cap -> {
                PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getEntity()), new SyncWeatherMsg(cap.getCurrentWeather()));
            });
        }
    }

}
