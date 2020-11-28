package deerangle.space.capability;

import com.google.common.collect.ImmutableList;
import deerangle.space.planet.Weather;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class DefaultWeatherCapability implements IWeatherCapability {

    private List<Supplier<Weather>> availableWeathers;
    private int minWeatherTimeout;
    private int maxWeatherTimeout;
    private int currentWeatherTimeout;
    private Weather currentWeather;

    public DefaultWeatherCapability(List<Supplier<Weather>> availableWeathers, int minWeatherTimeout, int maxWeatherTimeout) {
        this.availableWeathers = availableWeathers;
        this.minWeatherTimeout = minWeatherTimeout;
        this.maxWeatherTimeout = maxWeatherTimeout;
        this.currentWeather = null;
        this.currentWeatherTimeout = calculateTimeout(new Random());
    }

    public DefaultWeatherCapability() {
        this(ImmutableList.of(), 0, 1);
    }

    public int calculateTimeout(Random random) {
        return this.getMinWeatherTimeout() + random.nextInt(this.getMaxWeatherTimeout() - this.getMinWeatherTimeout());
    }

    @Override
    public List<Supplier<Weather>> getAvailableWeathers() {
        return this.availableWeathers;
    }

    @Override
    public int getMinWeatherTimeout() {
        return this.minWeatherTimeout;
    }

    @Override
    public int getMaxWeatherTimeout() {
        return this.maxWeatherTimeout;
    }

    @Override
    public int getCurrentWeatherTimeout() {
        return this.currentWeatherTimeout;
    }

    @Override
    public Weather getCurrentWeather() {
        return this.currentWeather;
    }

    @Override
    public void setAvailableWeathers(List<Supplier<Weather>> availableWeathers) {
        this.availableWeathers = availableWeathers;
    }

    @Override
    public void setMinWeatherTimeout(int minWeatherTimeout) {
        this.minWeatherTimeout = minWeatherTimeout;
    }

    @Override
    public void setMaxWeatherTimeout(int maxWeatherTimeout) {
        this.maxWeatherTimeout = maxWeatherTimeout;
    }

    @Override
    public void setCurrentWeatherTimeout(int currentWeatherTimeout) {
        this.currentWeatherTimeout = currentWeatherTimeout;
    }

    @Override
    public void setCurrentWeather(Weather currentWeather) {
        this.currentWeather = currentWeather;
    }

}
