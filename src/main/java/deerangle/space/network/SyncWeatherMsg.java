package deerangle.space.network;

import deerangle.space.capability.Capabilities;
import deerangle.space.planet.Weather;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

import java.util.function.Supplier;

public class SyncWeatherMsg {

    private Weather currentWeather;

    public SyncWeatherMsg() {
    }

    public SyncWeatherMsg(Weather currentWeather) {
        this.currentWeather = currentWeather;
    }

    public static void serialize(SyncWeatherMsg msg, PacketBuffer packetBuffer) {
        packetBuffer.writeBoolean(msg.currentWeather == null);
        if (msg.currentWeather != null) {
            packetBuffer.writeResourceLocation(msg.currentWeather.getRegistryName());
        }
    }

    public static SyncWeatherMsg deserialize(PacketBuffer packetBuffer) {
        if (packetBuffer.readBoolean()) {
            return new SyncWeatherMsg(null);
        }
        IForgeRegistry<Weather> weatherRegistry = RegistryManager.ACTIVE.getRegistry(Weather.class);
        return new SyncWeatherMsg(weatherRegistry.getValue(packetBuffer.readResourceLocation()));
    }

    public static void handle(SyncWeatherMsg msg, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            Minecraft.getInstance().world.getCapability(Capabilities.WEATHER_CAPABILITY).ifPresent(cap -> {
                cap.setCurrentWeather(msg.currentWeather);
            });
        });
        contextSupplier.get().setPacketHandled(true);
    }

}
