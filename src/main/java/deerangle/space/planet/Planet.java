package deerangle.space.planet;

import deerangle.space.planet.render.AbstractAtmosphereRenderer;
import deerangle.space.planet.util.CustomDimensionType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.Dimension;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Planet extends ForgeRegistryEntry<Planet> {

    private final ResourceLocation skyTexture;
    private final int skySize;
    private final float orbitTilt;
    private final float orbitDirection;
    private final boolean hasSkyLight;
    private final boolean superhot;
    private final boolean natural;
    private final long dayLength;
    private final float ambientLight;
    private final Function<Planet, AbstractAtmosphereRenderer> atmosphereRenderer;
    private final Supplier<Dimension> dimensionMaker;
    private final Collection<Supplier<Biome>> biomeMakers;
    private final Collection<Supplier<Planet>> skyPlanets;
    private final BiConsumer<Float, float[]> sunsetColors;
    private final OptionalDouble sunsetAlpha;
    private final BiFunction<Float, Float, Vector3d> cloudColor;
    private final float cloudHeight;

    private Planet(ResourceLocation location, Builder builder) {
        this.setRegistryName(location);
        this.skyTexture = builder.skyTextureLocation;
        this.skySize = builder.skySize;
        this.orbitTilt = builder.orbitTilt;
        this.orbitDirection = builder.orbitAngle;
        this.hasSkyLight = builder.skyLight;
        this.superhot = builder.superhot;
        this.natural = builder.natural;
        this.dayLength = builder.dayLength;
        this.ambientLight = builder.ambientLight;
        this.atmosphereRenderer = builder.atmosphereRenderer;
        this.dimensionMaker = builder.dimensionMaker;
        this.biomeMakers = builder.biomeMakers.entrySet().stream()
                .map(entry -> (Supplier<Biome>) () -> entry.getValue().get().setRegistryName(entry.getKey()))
                .collect(Collectors.toList());
        this.skyPlanets = builder.skyPlanets;
        this.sunsetColors = builder.sunsetColors;
        this.sunsetAlpha = builder.sunsetAlpha;
        this.cloudColor = builder.cloudColor;
        this.cloudHeight = builder.cloudHeight;
    }

    public Collection<Supplier<Planet>> getSkyPlanets() {
        return skyPlanets;
    }

    public float getOrbitDirection() {
        return this.orbitDirection;
    }

    public float getOrbitTilt() {
        return this.orbitTilt;
    }

    public float getSkyPosition(float planetPosition) {
        return planetPosition;
    }

    public float getSizeInSky() {
        return this.skySize;
    }

    public ResourceLocation getSkyTexture() {
        return skyTexture;
    }

    public DimensionType getDimensionType() {
        return new CustomDimensionType(hasSkyLight, superhot, natural, this.getRegistryName(), ambientLight, dayLength);
    }

    public Function<Planet, AbstractAtmosphereRenderer> getAtmosphereRenderer() {
        return this.atmosphereRenderer;
    }

    public Supplier<Dimension> getDimensionMaker() {
        return this.dimensionMaker;
    }

    public Collection<Supplier<Biome>> getBiomeMakers() {
        return this.biomeMakers;
    }

    public static Planet.Builder builder() {
        return new Builder();
    }

    public float getDayLength() {
        return this.dayLength;
    }

    public BiConsumer<Float, float[]> getSunsetColors() {
        return this.sunsetColors;
    }

    public OptionalDouble getSunsetAlpha() {
        return this.sunsetAlpha;
    }

    public BiFunction<Float, Float, Vector3d> getCloudColor() {
        return this.cloudColor;
    }

    public float getCloudHeight() {
        return this.cloudHeight;
    }

    public static class Builder {

        public BiConsumer<Float, float[]> sunsetColors;
        public OptionalDouble sunsetAlpha;
        public BiFunction<Float, Float, Vector3d> cloudColor;
        public float cloudHeight;
        private ResourceLocation skyTextureLocation;
        private final Map<ResourceLocation, Supplier<Biome>> biomeMakers;
        private final List<Supplier<Planet>> skyPlanets;
        private Supplier<Dimension> dimensionMaker;
        private int dayLength;
        private boolean skyLight;
        private boolean superhot;
        private boolean natural;
        private float ambientLight;
        private float orbitAngle;
        private float orbitTilt;
        private int skySize;
        private Function<Planet, AbstractAtmosphereRenderer> atmosphereRenderer;

        private Builder() {
            this.biomeMakers = new HashMap<>();
            this.skyPlanets = new ArrayList<>();
            this.dimensionMaker = null;
            this.dayLength = 24000;
            this.skyLight = true;
            this.superhot = false;
            this.natural = false;
            this.ambientLight = 0.0F;
            this.skySize = 30;
            this.orbitAngle = -90.0f;
            this.orbitTilt = 0.0f;
            this.atmosphereRenderer = null;
            this.sunsetAlpha = OptionalDouble.empty();
            this.sunsetColors = (factor, array) -> {
                array[0] = factor * 0.3F + 0.7F;
                array[1] = factor * factor * 0.7F + 0.2F;
                array[2] = factor * factor * 0.0F + 0.2F;
            };
            this.cloudColor = (partialTicks, celestialAngle) -> {
                float angleCosine = MathHelper.cos(celestialAngle * ((float) Math.PI * 2F)) * 2.0F + 0.5F;
                angleCosine = MathHelper.clamp(angleCosine, 0.0F, 1.0F);
                float red = 1.0F;
                float green = 1.0F;
                float blue = 1.0F;

                red = red * (angleCosine * 0.9F + 0.1F);
                green = green * (angleCosine * 0.9F + 0.1F);
                blue = blue * (angleCosine * 0.85F + 0.15F);
                return new Vector3d(red, green, blue);
            };
            this.cloudHeight = Float.NaN;
        }

        public Planet build(ResourceLocation location) {
            return new Planet(location, this);
        }

        public Builder cloudHeight(float height) {
            this.cloudHeight = height;
            return this;
        }

        public Builder addPlanetInSky(Supplier<Planet> planetSupplier) {
            this.skyPlanets.add(planetSupplier);
            return this;
        }

        public Builder noSunset() {
            this.sunsetAlpha = OptionalDouble.of(0.0);
            this.sunsetColors = (factor, array) -> {
                array[0] = 0;
                array[1] = 0;
                array[2] = 0;
            };
            return this;
        }

        public Builder fullSunset(Vector3f color) {
            this.sunsetAlpha = OptionalDouble.of(1.0);
            this.sunsetColors = (factor, array) -> {
                array[0] = color.getX();
                array[1] = color.getY();
                array[2] = color.getZ();
            };
            return this;
        }

        public Builder fadingSunset(Vector3f color) {
            this.sunsetAlpha = OptionalDouble.empty();
            this.sunsetColors = (factor, array) -> {
                array[0] = color.getX();
                array[1] = color.getY();
                array[2] = color.getZ();
            };
            return this;
        }

        public Builder skyTexture(ResourceLocation location, int size) {
            this.skyTextureLocation = location;
            this.skySize = size;
            return this;
        }

        public Builder dimensionMaker(Supplier<Dimension> maker) {
            this.dimensionMaker = maker;
            return this;
        }

        public Builder addBiome(ResourceLocation registryKey, Supplier<Biome> biomeSupplier) {
            this.biomeMakers.put(registryKey, biomeSupplier);
            return this;
        }

        public Builder dayLength(int length) {
            this.dayLength = length;
            return this;
        }

        public Builder noSkyLight() {
            this.skyLight = false;
            return this;
        }

        public Builder superhot() {
            this.superhot = true;
            return this;
        }

        public Builder natural() {
            this.natural = true;
            return this;
        }

        public Builder ambientLight(float ambientLight) {
            this.ambientLight = ambientLight;
            return this;
        }

        public Builder orbit(float directionAngle, float tiltAngle) {
            this.orbitAngle = directionAngle;
            this.orbitTilt = tiltAngle;
            return this;
        }

        public Builder atmosphere(Function<Planet, AbstractAtmosphereRenderer> renderer) {
            this.atmosphereRenderer = renderer;
            return this;
        }

    }

}
