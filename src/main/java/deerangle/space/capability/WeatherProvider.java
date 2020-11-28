package deerangle.space.capability;

import deerangle.space.planet.Weather;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import java.util.List;
import java.util.function.Supplier;

public class WeatherProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundNBT> {

    private final DefaultWeatherCapability weather;
    private final LazyOptional<IWeatherCapability> weatherOpt;

    public WeatherProvider(List<Supplier<Weather>> weathers, int minDuration, int maxDuration) {
        this.weather = new DefaultWeatherCapability(weathers, minDuration, maxDuration);
        weatherOpt = LazyOptional.of(() -> weather);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return weatherOpt.cast();
    }

    @Override
    public CompoundNBT serializeNBT() {
        if (Capabilities.WEATHER_CAPABILITY == null) {
            return new CompoundNBT();
        } else {
            return (CompoundNBT) Capabilities.WEATHER_CAPABILITY.writeNBT(weather, null);
        }
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (Capabilities.WEATHER_CAPABILITY != null) {
            Capabilities.WEATHER_CAPABILITY.readNBT(weather, null, nbt);
        }
    }

    public void invalidate() {
        this.weatherOpt.invalidate();
    }

}
