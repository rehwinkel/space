package deerangle.space.planet;

import deerangle.space.planet.render.AtmosphereRenderer;
import deerangle.space.planet.util.CustomDimensionType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.Dimension;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.*;
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
    private final Function<Planet, AtmosphereRenderer> atmosphereRenderer;
    private final Supplier<Dimension> dimensionMaker;
    private final Collection<Supplier<Biome>> biomeMakers;
    private final Collection<Supplier<Planet>> skyPlanets;

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

    public Function<Planet, AtmosphereRenderer> getAtmosphereRenderer() {
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

    public static class Builder {

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
        private Function<Planet, AtmosphereRenderer> atmosphereRenderer;

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
        }

        public Planet build(ResourceLocation location) {
            return new Planet(location, this);
        }

        public Builder addPlanetInSky(Supplier<Planet> planetSupplier) {
            this.skyPlanets.add(planetSupplier);
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

        public Builder atmosphere(Function<Planet, AtmosphereRenderer> renderer) {
            this.atmosphereRenderer = renderer;
            return this;
        }

    }

}
