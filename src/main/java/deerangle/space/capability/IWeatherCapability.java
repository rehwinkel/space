package deerangle.space.capability;

import deerangle.space.planet.Weather;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.registries.RegistryManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public interface IWeatherCapability {

    List<Supplier<Weather>> getAvailableWeathers();

    int getMinWeatherTimeout();

    int getMaxWeatherTimeout();

    int getCurrentWeatherTimeout();

    Weather getCurrentWeather();

    void setAvailableWeathers(List<Supplier<Weather>> weathers);

    void setMinWeatherTimeout(int min);

    void setMaxWeatherTimeout(int max);

    void setCurrentWeatherTimeout(int timeout);

    void setCurrentWeather(Weather weather);

    class Storage implements Capability.IStorage<IWeatherCapability> {

        @Override
        public INBT writeNBT(Capability<IWeatherCapability> capability, IWeatherCapability instance, Direction side) {
            CompoundNBT nbt = new CompoundNBT();
            ListNBT list = new ListNBT();
            for (Supplier<Weather> w : instance.getAvailableWeathers()) {
                list.add(StringNBT.valueOf(w.get().getRegistryName().toString()));
            }
            nbt.put("Available", list);
            if (instance.getCurrentWeather() != null) {
                nbt.putString("Current", instance.getCurrentWeather().getRegistryName().toString());
            }
            nbt.putInt("Timeout", instance.getCurrentWeatherTimeout());
            nbt.putInt("MaxTime", instance.getMaxWeatherTimeout());
            nbt.putInt("MinTime", instance.getMinWeatherTimeout());
            return nbt;
        }

        @Override
        public void readNBT(Capability<IWeatherCapability> capability, IWeatherCapability instance, Direction side, INBT rawNbt) {
            CompoundNBT nbt = (CompoundNBT) rawNbt;
            ListNBT available = nbt.getList("Available", 8);
            List<Supplier<Weather>> availableWeathers = new ArrayList<>();
            available.forEach(weatherNbt -> availableWeathers.add(() -> RegistryManager.ACTIVE.getRegistry(Weather.class).getValue(new ResourceLocation(weatherNbt.getString()))));
            Weather current = null;
            if (nbt.contains("Current")) {
                current = RegistryManager.ACTIVE.getRegistry(Weather.class).getValue(new ResourceLocation(nbt.getString("Current")));
            }
            int timeout = nbt.getInt("Timeout");
            int max = nbt.getInt("MaxTime");
            int min = nbt.getInt("MinTime");
            instance.setAvailableWeathers(availableWeathers);
            instance.setCurrentWeather(current);
            instance.setCurrentWeatherTimeout(timeout);
            instance.setMaxWeatherTimeout(max);
            instance.setMinWeatherTimeout(min);
        }

    }

}
